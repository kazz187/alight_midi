package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.LedDevice;
import zone.kaz.alight_midi.device.LedDeviceManager;
import zone.kaz.alight_midi.inject.DIContainer;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class AnimationManager {

    private CopyOnWriteArrayList<Animation> animationList = new CopyOnWriteArrayList<>();
    private LedDeviceManager ledDeviceManager = DIContainer.get(LedDeviceManager.class);

    public void register(Animation animation) {
        animationList.add(animation);
    }

    public void setTick(long tick) {
        ArrayList<Animation> removeList = new ArrayList<>();
        for (Animation animation : animationList) {
            animation.setTick(tick);
            if (animation.isEnd(tick)) {
                removeList.add(animation);
            }
            String deviceKey = animation.getDeviceBuffer().getDeviceKey();
            LedDevice device = ledDeviceManager.getDevice(deviceKey);
            if (device != null) {
                device.send(animation.getDeviceBuffer());
            }
        }
        for (Animation animation : removeList) {
            animation.reset();
            animationList.remove(animation);
            String deviceKey = animation.getDeviceBuffer().getDeviceKey();
            LedDevice device = ledDeviceManager.getDevice(deviceKey);
            if (device != null) {
                device.send(animation.getDeviceBuffer());
            }
        }
    }

    public void removeAll() {
        for (Animation animation : animationList) {
            animation.reset();
            String deviceKey = animation.getDeviceBuffer().getDeviceKey();
            LedDevice device = ledDeviceManager.getDevice(deviceKey);
            if (device != null) {
                device.send(animation.getDeviceBuffer());
            }
            animationList.remove(animation);
        }
    }
}
