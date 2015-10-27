package zone.kaz.alight_midi.sequencer.animation;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.main.MainController;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.Animation;

import java.util.ArrayList;

public class AllFlash extends Animation {

    public AllFlash(long startTick, int tickSize, DeviceBuffer deviceBuffer) {
        super(startTick, tickSize, deviceBuffer);
    }

    @Override
    public void setTick(long tick) {
        ArrayList<Stripe> stripes = deviceBuffer.getStripes();
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