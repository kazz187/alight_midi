package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.sequencer.Animation;

import java.util.ArrayList;
import java.util.Random;

public class Wave extends Animation {

    private int r = 0, g = 0, b = 0;

    public Wave() {
        super();
    }

    public Wave(long startTick, int tickSize, DeviceBuffer deviceBuffer, String params) {
        super(startTick, tickSize, deviceBuffer, params);
    }

    @Override
    public void init() {
        Random rand = new Random();
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
    }

    @Override
    public void setTick(long tick) {
        ArrayList<Stripe> stripes = deviceBuffer.getStripes();
        long pos = tick - startTick;
        double posRate = (double) pos / tickSize;
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length / 3; i++) {
                double rate = (double) i / (buffer.length / 3);
                double diff = Math.abs(posRate - rate);
                buffer[i*3]   = (byte) (g * Math.max((1-diff)*2-1, 0));
                buffer[i*3+1] = (byte) (b * Math.max((1-diff)*2-1, 0));
                buffer[i*3+2] = (byte) (r * Math.max((1-diff)*2-1, 0));
            }
        }
    }

}
