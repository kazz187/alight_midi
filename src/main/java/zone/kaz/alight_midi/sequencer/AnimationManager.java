package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;

import java.util.ArrayList;

@Singleton
public class AnimationManager {

    private ArrayList<Animation> animationList = new ArrayList<>();

    public void create(Animation animation) {
        animationList.add(animation);
    }

    public void setTick(int tick) {
        for (Animation animation : animationList) {
            animation.setTick(tick);
        }
    }

}
