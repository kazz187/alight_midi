package zone.kaz.alight_midi.gui.sequencer;

import com.google.common.reflect.ClassPath;

public class AnimationItem implements SequencerItem {

    private ClassPath.ClassInfo classInfo;

    public AnimationItem(ClassPath.ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public String toString() {
        return this.classInfo.getSimpleName();
    }

}
