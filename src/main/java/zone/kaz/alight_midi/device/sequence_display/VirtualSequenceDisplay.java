package zone.kaz.alight_midi.device.sequence_display;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VirtualSequenceDisplay extends SequenceDisplay {

    public VirtualSequenceDisplay(ArrayList<Rectangle> rectangleList) {
        displayBlockList.addAll(rectangleList.stream().map(VirtualDisplayBlock::new).collect(Collectors.toList()));
    }

}
