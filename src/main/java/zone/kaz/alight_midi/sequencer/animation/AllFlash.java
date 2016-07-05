package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.animation.params.AllFlashParams;
import zone.kaz.alight_midi.sequencer.animation.util.ParamsLoader;

import java.util.ArrayList;

public class AllFlash extends Animation {

    private AllFlashParams allFlashParams;
    private ArrayList<Stripe> stripes;

    public AllFlash() {
        super();
    }

    @Override
    public void init() {
        allFlashParams = new ParamsLoader<AllFlashParams>(this).load(AllFlashParams.class);
        this.tickSize *= allFlashParams.getRate();
        stripes = deviceBuffer.getStripes(allFlashParams.getStripeIds());
    }

    @Override
    public void setTick(long tick) {
        long pos = tick - startTick;
        double alpha = 1;
        if (pos > tickSize / 2) {
            alpha = (double) (tickSize - pos) / (tickSize / 2);
        }
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) (0xff * alpha);
            }
        }
    }

}
