package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;

import java.util.ArrayList;

public class AllFlashAnimation extends Animation {

    public AllFlashAnimation(long startTick, int tickSize, DeviceBuffer deviceBuffer) {
        super(startTick, tickSize, deviceBuffer);
    }

    @Override
    public void setTick(long tick) {
        ArrayList<Stripe> stripes = deviceBuffer.getStripes();
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) 0xff;
            }
        }
    }

}
