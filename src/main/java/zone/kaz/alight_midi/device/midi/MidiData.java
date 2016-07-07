package zone.kaz.alight_midi.device.midi;

import javax.sound.midi.ShortMessage;

public class MidiData {

    private int type;
    private byte note;
    private byte velocity;

    public MidiData() {}
    public MidiData(int type, byte note, byte velocity) {
        this.type = type;
        this.note = note;
        this.velocity = velocity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public String toString() {
        String typeStr = "";
        switch (type) {
            case ShortMessage.NOTE_ON:
                typeStr = "ON";
                break;
            case ShortMessage.NOTE_OFF:
                typeStr = "OFF";
                break;
            default:
                break;
        }
        return typeStr + " - " + note + " - " + velocity;
    }

}
