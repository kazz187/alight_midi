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
            alpha = (double) (tickSize - pos) / (tickSize / 2.0);
        }
        byte r = (byte) (allFlashParams.getColor()[0] * alpha);
        byte g = (byte) (allFlashParams.getColor()[1] * alpha);
        byte b = (byte) (allFlashParams.getColor()[2] * alpha);
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length / 3; i++) {
                buffer[i*3]   = r;
                buffer[i*3+1] = g;
                buffer[i*3+2] = b;
            }
        }
    }

}
