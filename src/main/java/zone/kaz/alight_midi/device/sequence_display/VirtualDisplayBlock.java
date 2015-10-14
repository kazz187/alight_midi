package zone.kaz.alight_midi.device.sequence_display;

import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class VirtualDisplayBlock extends DisplayBlock {

    private Rectangle block;

    private final Paint LIGHT_UP_COLOR = Paint.valueOf("#ff9b00");
    private final Paint LIGHT_DOWN_COLOR = Paint.valueOf("#ffdd87");

    public VirtualDisplayBlock(Rectangle block) {
        this.block = block;
    }

    @Override
    void light_up_process() {
        Platform.runLater(()->{block.setFill(LIGHT_UP_COLOR);});
    }

    @Override
    void light_down_process() {
        Platform.runLater(()->{block.setFill(LIGHT_DOWN_COLOR);});
    }

}
