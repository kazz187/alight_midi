package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.animation.params.WaveParams;
import zone.kaz.alight_midi.sequencer.animation.util.Color;
import zone.kaz.alight_midi.sequencer.animation.util.ParamsLoader;
import zone.kaz.alight_midi.sequencer.animation.util.RandomColor;

import java.util.ArrayList;

public class Wave extends Animation {

    protected Color color;
    protected WaveParams waveParams;

    public Wave() {
        super();
    }

    @Override
    public void init() {
        waveParams = new ParamsLoader<WaveParams>(this).load(WaveParams.class);
        Color[] colorList = {
                new Color(0, 0xff, 0xff),
                new Color(0xff, 0, 0xff),
                new Color(0xff, 0xff, 0),
        };
        RandomColor randomColor = new RandomColor(colorList);
        this.color = randomColor.getNext();
        this.tickSize *= waveParams.getRate();
    }

    @Override
    public void setTick(long tick) {
        ArrayList<Stripe> stripes = deviceBuffer.getStripes(waveParams.getStripeIds());
        double posRate = calcPosRate(tick);
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length / 3; i++) {
                double rate = ((double) i / (buffer.length / 3)) * 1.1 - 0.05;
                double diff = Math.abs(posRate - rate);
                buffer[i*3]   = (byte) (color.getG() * Math.max((1-diff)*2-1, 0));
                buffer[i*3+1] = (byte) (color.getB() * Math.max((1-diff)*2-1, 0));
                buffer[i*3+2] = (byte) (color.getR() * Math.max((1-diff)*2-1, 0));
            }
        }
    }

    protected double calcPosRate(long tick) {
        long pos = tick - startTick;
        double posRate = (double) pos / tickSize;
        if (waveParams.getReverse()) {
            posRate = 1 - posRate;
        }
        return posRate;
    }

}
