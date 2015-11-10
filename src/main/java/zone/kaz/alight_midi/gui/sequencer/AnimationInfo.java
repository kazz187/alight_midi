package zone.kaz.alight_midi.gui.sequencer;

import com.google.common.reflect.ClassPath;
import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.sequencer.Animation;

public class AnimationInfo implements SequencerInfo {

    private ClassPath.ClassInfo classInfo;
    private Class<? extends Animation> animationClass;

    public AnimationInfo(ClassPath.ClassInfo classInfo) {
        this.classInfo = classInfo;
        try {
            animationClass = (Class<? extends Animation>) Class.forName(classInfo.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Animation createAnimation(long startTick, int tickSize, DeviceBuffer deviceBuffer) {
        try {
            Animation animation = animationClass.newInstance();
            animation.setStartTick(startTick);
            animation.setTickSize(tickSize);
            animation.setDeviceBuffer(deviceBuffer);
            return animation;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return this.classInfo.getSimpleName();
    }

}
