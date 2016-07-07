package zone.kaz.alight_midi.device.midi;

public class MappingData {

    private String processorName;
    private MidiData midiData;

    public MappingData(String processorName, MidiData midiData) {
        this.processorName = processorName;
        this.midiData = midiData;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public MidiData getMidiData() {
        return midiData;
    }

    public void setMidiData(MidiData midiData) {
        this.midiData = midiData;
    }

}
