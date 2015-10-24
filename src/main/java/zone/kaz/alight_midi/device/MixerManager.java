package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.main.MainController;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.AnimationManager;
import zone.kaz.alight_midi.sequencer.MixerChannel;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class MixerManager {

    private AnimationManager animationManager = DIContainer.get(AnimationManager.class);
    private DeviceBufferManager deviceBufferManager = DIContainer.get(DeviceBufferManager.class);
    private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
    private LedDeviceManager ledDeviceManager = DIContainer.get(LedDeviceManager.class);

    public void run() {
        MainController controller = (MainController) controllerManager.get(MainController.class);
        double filter1 = (controller.getFader1() / 100) * (1.0 - controller.getCrossFader() / 100);
        double filter2 = (controller.getFader2() / 100) * (controller.getCrossFader() / 100);

        HashMap<String, DeviceBuffer> mixedBufferMap = new HashMap<>();
        Set<String> deviceNames = deviceBufferManager.getDeviceNames();
        for (String deviceName : deviceNames) {
            mixedBufferMap.put(deviceName, deviceBufferManager.createDeviceBuffer(deviceName));
        }
        HashMap<String, DeviceBuffer> mixedBufferMap1
                = runChannel(MixerChannel.CHANNEL1, filter1);
        HashMap<String, DeviceBuffer> mixedBufferMap2
                = runChannel(MixerChannel.CHANNEL2, filter2);
        for (String deviceName : mixedBufferMap1.keySet()) {
            DeviceBuffer mixedBuffer = mixedBufferMap.get(deviceName);
            mixedBuffer.add(mixedBufferMap1.get(deviceName));
        }
        for (String deviceName : mixedBufferMap2.keySet()) {
            DeviceBuffer mixedBuffer = mixedBufferMap.get(deviceName);
            mixedBuffer.add(mixedBufferMap2.get(deviceName));
        }
        double masterFilter = controller.getMasterFader() / 100;
        for (DeviceBuffer mixedBuffer : mixedBufferMap.values()) {
            mixedBuffer.multiply(masterFilter);
            String deviceName = mixedBuffer.getDeviceInfo().getName();
            LedDevice ledDevice = ledDeviceManager.getDevice(deviceName);
            ledDevice.send(mixedBuffer);
        }
    }

    private HashMap<String, DeviceBuffer> runChannel(MixerChannel channel, double filter) {
        HashMap<String, DeviceBuffer> mixedBufferMap = new HashMap<>();
        CopyOnWriteArrayList<Animation> animationList
                = animationManager.getChannelAnimationList(channel);
        for (Animation animation : animationList) {
            DeviceBuffer deviceBuffer = animation.getDeviceBuffer();
            String name = deviceBuffer.getDeviceInfo().getName();
            DeviceBuffer mixedBuffer;
            if (!mixedBufferMap.containsKey(name)) {
                mixedBuffer = deviceBufferManager.createDeviceBuffer(name);
                mixedBufferMap.put(name, mixedBuffer);
            } else {
                mixedBuffer = mixedBufferMap.get(name);
            }
            mixedBuffer.add(animation.getDeviceBuffer());
        }
        for (DeviceBuffer mixedBuffer : mixedBufferMap.values()) {
            mixedBuffer.multiply(filter);
        }
        return mixedBufferMap;
    }

}
