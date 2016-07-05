package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.animation.params.GradationParams;
import zone.kaz.alight_midi.sequencer.animation.params.WaveParams;
import zone.kaz.alight_midi.sequencer.animation.util.Color;
import zone.kaz.alight_midi.sequencer.animation.util.ParamsLoader;
import zone.kaz.alight_midi.sequencer.animation.util.RandomColor;

import java.util.ArrayList;

public class Gradation extends Animation {

    protected GradationParams gradationParams;
    protected ArrayList<Stripe> stripes;

    public Gradation() {
        super();
    }

    @Override
    public void init() {
        gradationParams = new ParamsLoader<GradationParams>(this).load(GradationParams.class);
        this.tickSize *= gradationParams.getRate();
        stripes = deviceBuffer.getStripes(gradationParams.getStripeIds());
    }

    @Override
    public void setTick(long tick) {
        double posRate = calcPosRate(tick);
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            int fromR = gradationParams.getFromColor()[0];
            int fromG = gradationParams.getFromColor()[1];
            int fromB = gradationParams.getFromColor()[2];
            int toR = gradationParams.getToColor()[0];
            int toG = gradationParams.getToColor()[1];
            int toB = gradationParams.getToColor()[2];
            byte r = (byte) (fromR + (toR - fromR) * posRate);
            byte g = (byte) (fromG + (toG - fromG) * posRate);
            byte b = (byte) (fromB + (toB - fromB) * posRate);
            for (int i = 0; i < buffer.length / 3; i++) {
                buffer[i*3]   = r;
                buffer[i*3+1] = g;
                buffer[i*3+2] = b;
            }
        }
    }

    protected double calcPosRate(long tick) {
        long pos = tick - startTick;
        double posRate = (double) pos / tickSize;
        if (gradationParams.getReverse()) {
            posRate = 1 - posRate;
        }
        return posRate;
    }

}
