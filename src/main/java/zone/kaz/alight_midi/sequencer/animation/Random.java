package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;

import java.util.ArrayList;
import java.util.List;

public class Random extends Animation {

    private int r = 0, g = 0, b = 0;
    private int[] positions;

    public Random() {
        super();
    }

    public Random(long startTick, int tickSize, DeviceBuffer deviceBuffer, String params) {
        super(startTick, tickSize, deviceBuffer, params);
    }

    @Override
    public void init() {
        java.util.Random rand = new java.util.Random();
        int color = rand.nextInt(3);
        switch (color) {
            case 0:
                r = 0; g = 0xff; b = 0xff;
                break;
            case 1:
                r = 0xff; g = 0; b = 0xff;
                break;
            case 2:
                r = 0xff; g = 0xff; b = 0;
                break;
        }

        List<Stripe> stripes = deviceBuffer.getStripes();
        positions = new int[stripes.size()];
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
            buffer[positions[i]*3]   = (byte) (g * alpha);
            buffer[positions[i]*3+1] = (byte) (b * alpha);
            buffer[positions[i]*3+2] = (byte) (r * alpha);
            i++;
        }
    }

}
