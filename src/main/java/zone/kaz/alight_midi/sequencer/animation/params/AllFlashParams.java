package zone.kaz.alight_midi.sequencer.animation.params;

public class AllFlashParams {

    private double rate = 1.0;
    private int[] stripeIds = null;

    public AllFlashParams() {}

    public double getRate() {
        return rate;
    }

    public int[] getStripeIds() {
        return stripeIds;
    }

    public void setStripeIds(int[] stripeIds) {
        this.stripeIds = stripeIds;
    }

}
