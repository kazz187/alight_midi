package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class StepSequencer {

    private final static int COLUMN_INDEX_LABEL = 0;
    private final static int COLUMN_INDEX_BOX = 1;
    private GridPane gridPane;
    private ArrayList<Rectangle> buttons = new ArrayList<>();
    private int clock;
    private Label label = new Label();
    private HBox sequenceBox = new HBox();

    public StepSequencer(GridPane gridPane, int rowIndex, int clock, int beats) {
        this.gridPane = gridPane;
        gridPane.add(label, COLUMN_INDEX_LABEL, rowIndex);
        gridPane.add(sequenceBox, COLUMN_INDEX_BOX, rowIndex);
        double minHeight = 10.0, prefHeight = 30.0, maxHeight = -1.0;
        RowConstraints rowConstraints = new RowConstraints(
                minHeight, prefHeight, maxHeight
        );
        rowConstraints.setPercentHeight(-1);
        if (gridPane.getRowConstraints().size() > rowIndex) {
            gridPane.getRowConstraints().set(rowIndex, rowConstraints);
        } else {
            gridPane.getRowConstraints().add(rowIndex, rowConstraints);
        }
        sequenceBox.setSpacing(-1.0);
        setClock(clock, beats);
    }

    public void setClock(int clock, int beats) {
        if (this.clock >= clock) {
            for (int i = this.clock - 1; i >= clock; i--) {
                Rectangle button = buttons.remove(i);
                sequenceBox.getChildren().remove(button);
            }
            this.clock = clock;
            return;
        }
        ArrayList<Rectangle> additionalButtons = new ArrayList<>();
        for (int i = this.clock; i < clock; i++) {
            boolean isBeats = i % beats == 0;
            Rectangle button = createButton(isBeats);
            buttons.add(button);
            additionalButtons.add(button);
        }
        sequenceBox.getChildren().addAll(additionalButtons);
        this.clock = clock;
    }

    public Rectangle createButton(boolean isBeat) {
        Paint color = Paint.valueOf(isBeat ? "#F4EEE1" : "#EFEEEE");
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(30);
        rectangle.setHeight(30);
        rectangle.setFill(color);
        rectangle.setStroke(Paint.valueOf("#C4BDAC"));
        rectangle.setStrokeWidth(1.0);
        return rectangle;
    }

    public void finish() {
        gridPane.getChildren().remove(label);
        gridPane.getChildren().remove(sequenceBox);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

}
