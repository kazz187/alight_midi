package zone.kaz.alight_midi.gui.sequencer;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.animation.*;

public class AnimationInfo implements SequencerInfo {

    private String animationName;

    public AnimationInfo(String animationName) {
        this.animationName = animationName;
    }

    public Animation createAnimation(long startTick, int tickSize, DeviceBuffer deviceBuffer, String params) {
        Animation animation;
        switch (animationName) {
            case "AllFlash":
                animation = new AllFlash();
                break;
            case "Gradation":
                animation = new Gradation();
                break;
            case "ParabolaWave":
                animation = new ParabolaWave();
                break;
            case "Random":
                animation = new Random();
                break;
            case "Wave":
                animation = new Wave();
                break;
            default:
                return null;
        }
        animation.setStartTick(startTick);
        animation.setTickSize(tickSize);
        animation.setDeviceBuffer(deviceBuffer);
        animation.setParams(params);
        return animation;
    }

    @Override
    public String toString() {
        return this.animationName;
    }

}
