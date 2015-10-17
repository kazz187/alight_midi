package zone.kaz.alight_midi.sequencer;

public abstract class Animation {

    private int tickSize;
    private byte[] buffer;

    public Animation(int tickSize, int bufSize) {
        this.tickSize = tickSize;
        buffer = new byte[bufSize];
    }

    public abstract void setTick(int tick);

}
