package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;
import zone.kaz.alight_midi.sequencer.animation.util.Color;
import zone.kaz.alight_midi.sequencer.animation.util.RandomColor;

import java.util.ArrayList;
import java.util.List;

public class Random extends Animation {

    private Color color;
    private int[] positions;

    public Random() {
        super();
    }

    public Random(long startTick, int tickSize, DeviceBuffer deviceBuffer, String params) {
        super(startTick, tickSize, deviceBuffer, params);
    }

    @Override
    public void init() {
        Color[] colorList = new Color[3];
        colorList[0] = new Color(0, 0xff, 0xff);
        colorList[1] = new Color(0xff, 0, 0xff);
        colorList[2] = new Color(0xff, 0xff, 0);
        RandomColor randomColor = new RandomColor(colorList);
        this.color = randomColor.getNext();

        List<Stripe> stripes = deviceBuffer.getStripes();
        positions = new int[stripes.size()];
        java.util.Random rand = new java.util.Random();
        int i = 0;
        for (Stripe stripe : stripes) {
            positions[i] = rand.nextInt(stripe.getBuffer().length/3);
            i++;
        }
    }

    @Override
    public void setTick(long tick) {
        ArrayList<Stripe> stripes = deviceBuffer.getStripes();
        int i = 0;
        long pos = tick - startTick;
        double alpha = 1;
        if (pos > tickSize / 2) {
            alpha = (double) (tickSize - pos) / (tickSize / 2);
        }
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            buffer[positions[i]*3]   = (byte) (color.getG() * alpha);
            buffer[positions[i]*3+1] = (byte) (color.getB() * alpha);
            buffer[positions[i]*3+2] = (byte) (color.getR() * alpha);
            i++;
        }
    }

}
