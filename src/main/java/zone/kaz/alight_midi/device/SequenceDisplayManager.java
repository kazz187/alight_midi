package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.sequence_display.SequenceDisplay;

import java.util.ArrayList;

@Singleton
public class SequenceDisplayManager {

    private ArrayList<SequenceDisplay> sequenceDisplayList = new ArrayList<>();

    public void add(SequenceDisplay sequenceDisplay) {
        sequenceDisplayList.add(sequenceDisplay);
    }

    public void setNumber(int num) {
        for (SequenceDisplay sequenceDisplay : sequenceDisplayList) {
            sequenceDisplay.setSequenceNumber(num);
        }
    }

    public void reset() {
        sequenceDisplayList.forEach(SequenceDisplay::reset);
    }

    public void remove(SequenceDisplay display) {
        display.reset();
        sequenceDisplayList.remove(display);
    }
}
