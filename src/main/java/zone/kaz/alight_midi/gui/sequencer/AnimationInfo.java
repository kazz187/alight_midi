package zone.kaz.alight_midi.gui.sequencer;

import com.google.common.reflect.ClassPath;

public class AnimationInfo implements SequencerInfo {

    private ClassPath.ClassInfo classInfo;

    public AnimationInfo(ClassPath.ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return this.classInfo.getSimpleName();
    }

}
