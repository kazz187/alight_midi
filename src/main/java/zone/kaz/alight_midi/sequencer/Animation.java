package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.Stripe;

public abstract class Animation {

    protected long startTick;
    protected DeviceBuffer deviceBuffer;
    protected int tickSize;

    public Animation() {}

    public Animation(long startTick, int tickSize, DeviceBuffer deviceBuffer) {
        this.startTick = startTick;
        this.tickSize = tickSize;
        this.deviceBuffer = deviceBuffer;
    }

    public DeviceBuffer getDeviceBuffer() {
        return deviceBuffer;
    }


    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    public void setStartTick(long startTick) {
        this.startTick = startTick;
    }

    public void setDeviceBuffer(DeviceBuffer deviceBuffer) {
        this.deviceBuffer = deviceBuffer;
    }

    public abstract void setTick(long tick);

    public boolean isEnd(long tick) {
        return tick > startTick + tickSize;
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
