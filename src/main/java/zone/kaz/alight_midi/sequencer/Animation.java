package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.led.DeviceBuffer;

public abstract class Animation {

    private DeviceBuffer deviceBuffer;
    private int tickSize;

    public Animation(int tickSize, DeviceBuffer deviceBuffer) {
        this.tickSize = tickSize;
        this.deviceBuffer = deviceBuffer;
    }

    public abstract void setTick(int tick);

}
