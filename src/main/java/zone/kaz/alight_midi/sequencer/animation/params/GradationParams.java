package zone.kaz.alight_midi.sequencer.animation.params;

public class GradationParams {

    private double rate = 1.0;
    private int[] stripeIds = null;
    private int[] fromColor = {0xff, 0x00, 0x00};
    private int[] toColor = {0xff, 0x99, 0x00};
    private boolean reverse = false;

    public GradationParams() {}

    public double getRate() {
        return rate;
    }

    public int[] getStripeIds() {
        return stripeIds;
    }

    public void setStripeIds(int[] stripeIds) {
        this.stripeIds = stripeIds;
    }

    public int[] getFromColor() {
        return fromColor;
    }

    public int[] getToColor() {
        return toColor;
    }

    public boolean getReverse() {
        return reverse;
    }

}
