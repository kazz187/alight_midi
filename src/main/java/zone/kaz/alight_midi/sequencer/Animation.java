package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;

public abstract class Animation {

    protected final long startTick;
    protected DeviceBuffer deviceBuffer;
    protected int tickSize;

    public Animation(long startTick, int tickSize, DeviceBuffer deviceBuffer) {
        this.startTick = startTick;
        this.tickSize = tickSize;
        this.deviceBuffer = deviceBuffer;
    }

    public DeviceBuffer getDeviceBuffer() {
        return deviceBuffer;
    }

    public abstract void setTick(long tick);

    public boolean isEnd(long tick) {
        return tick >= startTick + tickSize;
    }

    public void reset() {
        for (Stripe stripe : deviceBuffer.getStripes()) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = 0;
            }
        }
    }

}
