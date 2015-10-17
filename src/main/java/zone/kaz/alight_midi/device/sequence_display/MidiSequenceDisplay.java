package zone.kaz.alight_midi.device.sequence_display;

import zone.kaz.alight_midi.device.midi.MidiDevicePair;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MidiSequenceDisplay extends SequenceDisplay {

    public MidiSequenceDisplay(MidiDevicePair device, ArrayList<Integer> noteList) {
        displayBlockList.addAll(noteList.stream().map(note -> new MidiDisplayBlock(device, note)).collect(Collectors.toList()));
    }

}
