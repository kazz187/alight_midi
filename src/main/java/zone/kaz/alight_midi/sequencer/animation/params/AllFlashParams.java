package zone.kaz.alight_midi.sequencer.animation.params;

public class AllFlashParams {

    private double rate = 1.0;
    private int[] stripeIds = null;
    private int[] color = {0xff, 0xff, 0xff};

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

    public int[] getColor() {
        return color;
    }

}
