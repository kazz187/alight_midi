package zone.kaz.alight_midi.sequencer.animation.params;

public class WaveParams {

    private boolean reverse = false;
    private double rate = 1.0;

    public WaveParams() {}

    public double getRate() {
        return rate;
    }

    public boolean getReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

}
