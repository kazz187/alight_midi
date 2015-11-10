package zone.kaz.alight_midi.gui.sequencer;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StepSequencer {

    public enum Type {
        ANIMATION, COLOR;
    }

    public final static int COLUMN_INDEX_LABEL = 0;
    public final static int COLUMN_INDEX_BOX = 1;
    private final StepSequencerController controller;
    private ArrayList<SequencerButton> buttons = new ArrayList<>();
    private int clock;
    private Type type;
    private SequencerInfo sequencerInfo = null;
    private Label label = new Label();
    private int rowIndex;
    private double buttonWidth = 0;

    public StepSequencer(StepSequencerController controller, int rowIndex, int clock, int beats, double buttonWidth) {
        this.controller = controller;
        this.rowIndex = rowIndex;
        this.buttonWidth = buttonWidth;
        label.backgroundProperty().set(new Background(new BackgroundFill(Paint.valueOf("#EFEEEE"), null, null)));
        label.setPrefWidth(90);
        label.setPrefHeight(30);
        label.setPadding(new Insets(0, 0, 0, 5));
        label.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });
        label.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String data = db.getString();
                String[] parsedData = data.split(":", 0);
                switch (parsedData[0]) {
                    case "ANIMATION":
                        ListView<SequencerInfo> animationList = controller.getAnimationList();
                        int index = new Integer(parsedData[1]);
                        SequencerInfo item = animationList.itemsProperty().getValue().get(index);
                        sequencerInfo = item;
                        type = Type.ANIMATION;
                        label.setText(item.toString());
                        break;
                    default:
                        break;
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        GridPane gridPane = controller.getSequencerGrid();
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

    public Boolean[] getBoolArray() {
        Boolean[] result = new Boolean[buttons.size()];
        return buttons.stream().map(SequencerButton::isEnable)
                .collect(Collectors.toList()).toArray(result);
    }

    public SequencerInfo getSequencerInfo() {
        return sequencerInfo;
    }

    public Type getType() {
        return type;
    }

    public void setClock(int clock, int beats) {
        GridPane gridPane = controller.getSequencerGrid();
        if (this.clock >= clock) {
            for (int i = this.clock - 1; i >= clock; i--) {
                SequencerButton button = buttons.remove(i);
                GridPane.clearConstraints(button.getShape());
                gridPane.getChildren().remove(button.getShape());
            }
            this.clock = clock;
            return;
        }
        for (int i = this.clock; i < clock; i++) {
            boolean isBeats = i % beats == 0;
            SequencerButton button = new SequencerButton(buttonWidth, false, isBeats);
            buttons.add(button);
            gridPane.add(button.getShape(), COLUMN_INDEX_BOX + i, rowIndex);
        }
        this.clock = clock;
    }

    public void finish() {
        GridPane gridPane = controller.getSequencerGrid();
        gridPane.getChildren().remove(label);
        List<Rectangle> shapes = buttons.stream().map(SequencerButton::getShape).collect(Collectors.toList());
        gridPane.getChildren().removeAll(shapes);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setButtonWidth(double buttonWidth) {
        this.buttonWidth = buttonWidth;
        buttons.forEach(btn -> btn.setWidth(buttonWidth));
    }

}
