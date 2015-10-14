package zone.kaz.alight_midi.device.sequence_display;

import java.util.ArrayList;

public abstract class SequenceDisplay {

    protected ArrayList<DisplayBlock> displayBlockList = new ArrayList<>();

    public void setSequenceNumber(int num) {
        for (int i = 0; i < displayBlockList.size(); i++) {
            DisplayBlock block = displayBlockList.get(i);
            if (block == null) {
                continue;
            }
            if (i == num) {
                block.light_up();
            } else {
                block.light_down();
            }
        }
    }

    public void reset() {
        displayBlockList.forEach(DisplayBlock::light_down);
    }

}
