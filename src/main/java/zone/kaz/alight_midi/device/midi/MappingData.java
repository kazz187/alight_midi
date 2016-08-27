package zone.kaz.alight_midi.device.midi;

import javafx.beans.property.*;

public class MappingData {

    private StringProperty processorName;
    private ObjectProperty<Note> note;

    public MappingData() {
        this.processorName = new SimpleStringProperty("");
        this.note = new SimpleObjectProperty(0);
    }

    public MappingData(String processorName, int note) {
        this.processorName = new SimpleStringProperty(processorName);
        this.note = new SimpleObjectProperty<Note>(new Note(note));
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

    public ObjectProperty<Note> noteProperty() {
        return note;
    }

    public int getNote() {
        return note.get().getNote();
    }

    public void setNote(int note) {
        this.note.set(new Note(note));
    }

    public class Note {

        int note;

        public Note(Integer note) {
            this.note = note;
        }

        public int getNote() {
            return note;
        }

        public String toString() {
            if (note == 9999) {
                return "-";
            }
            return Integer.toString(note);
        }

    }

}
