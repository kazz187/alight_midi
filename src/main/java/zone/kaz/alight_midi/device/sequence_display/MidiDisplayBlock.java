package zone.kaz.alight_midi.device.sequence_display;

import zone.kaz.alight_midi.device.midi.MidiDevicePair;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class MidiDisplayBlock extends DisplayBlock {

    private MidiDevicePair device;
    private int note;

    private final int LIGHT_UP_COLOR = 20;
    private final int LIGHT_DOWN_COLOR = 127;

    public MidiDisplayBlock(MidiDevicePair device, int note) {
        this.device = device;
        this.note = note;
    }

    @Override
    void light_up_process() {
        try {
            device.getReceiver().send(new ShortMessage(
                    ShortMessage.NOTE_ON, 0, note, LIGHT_UP_COLOR
            ), 0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    @Override
    void light_down_process() {
        try {
            device.getReceiver().send(new ShortMessage(
                    ShortMessage.NOTE_OFF, 0, note, LIGHT_DOWN_COLOR
            ), 0);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
