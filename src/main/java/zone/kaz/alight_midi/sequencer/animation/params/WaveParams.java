package zone.kaz.alight_midi.sequencer.animation.params;

public class WaveParams {

    private boolean reverse = false;
    private double rate = 1.0;
    private double startX = 10;
    private int[] stripeIds = null;

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

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public int[] getStripeIds() {
        return stripeIds;
    }

    public void setStripeIds(int[] stripeIds) {
        this.stripeIds = stripeIds;
    }

}
