package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class StepSequencer {

    public final static int COLUMN_INDEX_LABEL = 0;
    public final static int COLUMN_INDEX_BOX = 1;
    private GridPane gridPane;
    private ArrayList<Rectangle> buttons = new ArrayList<>();
    private int clock;
    private Label label = new Label();
    private int rowIndex;

    public StepSequencer(GridPane gridPane, int rowIndex, int clock, int beats) {
        this.rowIndex = rowIndex;
        this.gridPane = gridPane;
        label.backgroundProperty().set(new Background(new BackgroundFill(Paint.valueOf("#EFEEEE"), null, null)));
        label.setText("Hoge");
        gridPane.add(label, COLUMN_INDEX_LABEL, rowIndex);
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
        setClock(clock, beats);
    }

    public void setClock(int clock, int beats) {
        if (this.clock >= clock) {
            for (int i = this.clock - 1; i >= clock; i--) {
                Rectangle button = buttons.remove(i);
                GridPane.clearConstraints(button);
                gridPane.getChildren().remove(button);
            }
            this.clock = clock;
            return;
        }
        for (int i = this.clock; i < clock; i++) {
            boolean isBeats = i % beats == 0;
            Rectangle button = createButton(isBeats);
            buttons.add(button);
            gridPane.add(button, COLUMN_INDEX_BOX + i, rowIndex);
        }
        this.clock = clock;
    }

    public Rectangle createButton(boolean isBeat) {
        Paint color = Paint.valueOf(isBeat ? "#F4EEE1" : "#EFEEEE");
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(30);
        rectangle.setWidth(20);

        rectangle.setFill(color);
        rectangle.setStroke(Paint.valueOf("#C4BDAC"));
        rectangle.setStrokeWidth(1.0);
        rectangle.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    rectangle.setFill(Paint.valueOf("#EBCFC4"));
                    break;
                case SECONDARY:
                    rectangle.setFill(color);
                    break;
                default:
                    break;
            }
        });
        return rectangle;
    }

    public void finish() {
        gridPane.getChildren().remove(label);
        gridPane.getChildren().removeAll(buttons);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

}
