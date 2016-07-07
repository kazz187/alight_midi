package zone.kaz.alight_midi.device.midi;

public class MidiData {

    private byte type;
    private byte note;
    private byte velocity;

    public MidiData() {

    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getNote() {
        return note;
    }

    public void setNote(byte note) {
        this.note = note;
    }

    public byte getVelocity() {
        return velocity;
    }

    public void setVelocity(byte velocity) {
        this.velocity = velocity;
    }

}
