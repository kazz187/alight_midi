package zone.kaz.alight_midi.device.midi;

public class MappingData {

    private String processorName;
    private MidiData pressedMidiData;
    private MidiData releasedMidiData;

    public MappingData(String processorName, MidiData pressedMidiData, MidiData releasedMidiData) {
        this.processorName = processorName;
        this.pressedMidiData = pressedMidiData;
        this.releasedMidiData = releasedMidiData;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public MidiData getPressedMidiData() {
        return pressedMidiData;
    }

    public void setPressedMidiData(MidiData pressedMidiData) {
        this.pressedMidiData = pressedMidiData;
    }

    public MidiData getReleasedMidiData() {
        return releasedMidiData;
    }

    public void setReleasedMidiData(MidiData releasedMidiData) {
        this.releasedMidiData = releasedMidiData;
    }

}
