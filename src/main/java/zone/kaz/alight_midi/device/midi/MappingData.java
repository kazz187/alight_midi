package zone.kaz.alight_midi.device.midi;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MappingData {

    private StringProperty processorName;
    private ObjectProperty<MidiData> pressedMidiData;

    private ObjectProperty<MidiData> releasedMidiData;

    public MappingData(String processorName, MidiData pressedMidiData, MidiData releasedMidiData) {
        this.processorName = new SimpleStringProperty(processorName);
        this.pressedMidiData = new SimpleObjectProperty<>(pressedMidiData);
        this.releasedMidiData = new SimpleObjectProperty<>(releasedMidiData);
    }

    public StringProperty processorNameProperty() {
        return processorName;
    }

    public String getProcessorName() {
        return processorName.get();
    }

    public void setProcessorName(String processorName) {
        this.processorName.set(processorName);
    }

    public ObjectProperty<MidiData> pressedMidiDataProperty() {
        return pressedMidiData;
    }

    public MidiData getPressedMidiData() {
        return pressedMidiData.get();
    }

    public void setPressedMidiData(MidiData pressedMidiData) {
        this.pressedMidiData.set(pressedMidiData);
    }

    public ObjectProperty<MidiData> releasedMidiDataProperty() {
        return releasedMidiData;
    }

    public MidiData getReleasedMidiData() {
        return releasedMidiData.get();
    }

    public void setReleasedMidiData(MidiData releasedMidiData) {
        this.releasedMidiData.set(releasedMidiData);
    }

}
