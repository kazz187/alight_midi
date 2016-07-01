package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.LedDevice;
import zone.kaz.alight_midi.device.LedDeviceManager;
import zone.kaz.alight_midi.inject.DIContainer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class AnimationManager {

    private CopyOnWriteArrayList<Animation> animationList1 = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Animation> animationList2 = new CopyOnWriteArrayList<>();
    private LedDeviceManager ledDeviceManager = DIContainer.get(LedDeviceManager.class);

    public boolean register(Animation animation, MixerChannel channel) {
        CopyOnWriteArrayList<Animation> animationList = getChannelAnimationList(channel);
        if (animationList != null) {
            animation.init();
            animationList.add(animation);
            return true;
        }
        return false;
    }

    public CopyOnWriteArrayList<Animation> getChannelAnimationList(MixerChannel channel) {
        switch (channel) {
            case CHANNEL1:
                return animationList1;
            case CHANNEL2:
                return animationList2;
            default:
                break;
        }
        return null;
    }

    public void setTick(long tick) {
        setTickChannel(tick, animationList1);
        setTickChannel(tick, animationList2);
    }

    private void setTickChannel(long tick, CopyOnWriteArrayList<Animation> animationList) {
        List<Animation> removeList = new LinkedList<>();
        animationList.stream().filter(animation -> animation.isEnd(tick)).forEach(animation -> {
            animation.reset();
            removeList.add(animation);
        });
        animationList.removeAll(removeList);
        for (Animation animation : animationList) {
            animation.setTick(tick);
        }
    }

    public void removeAll() {
        removeAllChannel(animationList1);
        removeAllChannel(animationList2);
    }

    private void removeAllChannel(CopyOnWriteArrayList<Animation> animationList) {
        for (Animation animation : animationList) {
            animation.reset();
            String deviceName = animation.getDeviceBuffer().getDeviceInfo().getName();
            LedDevice device = ledDeviceManager.getDevice(deviceName);
            if (device != null) {
                device.send(animation.getDeviceBuffer());
            }
            animationList.remove(animation);
        }
    }

}
