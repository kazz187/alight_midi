package zone.kaz.alight_midi.gui.sequencer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class StepSequencer {

    public enum Type {
        ANIMATION, COLOR;
    }

    public final static int COLUMN_INDEX_LABEL = 0;
    public final static int COLUMN_INDEX_BOX = 1;
    @JsonIgnore
    private StepSequencerController controller = null;
    @JsonIgnore
    private CopyOnWriteArrayList<SequencerButton> buttons = new CopyOnWriteArrayList<>();
    @JsonIgnore
    private Type type = null;
    @JsonIgnore
    private SequencerInfo sequencerInfo = null;
    @JsonIgnore
    private Label label = new Label();
    @JsonIgnore
    private int rowIndex;
    @JsonIgnore
    private double buttonWidth = 0;
    @JsonIgnore
    private int clock;
    @JsonProperty
    private String[] sequencerInfoData = null;
    @JsonProperty
    private Boolean[] boolArray = null;
    @JsonProperty
    private String params = "";

    public StepSequencer() {
        initLabel();
    }

    public StepSequencer(StepSequencerController controller, int rowIndex, int clock, int beats, double buttonWidth) {
        this.controller = controller;
        this.rowIndex = rowIndex;
        this.buttonWidth = buttonWidth;
        initLabel();
        initGridPane();
        setClock(clock, beats);
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String[] getSequencerInfoData() {
        return sequencerInfoData;
    }

    public void setSequencerInfoData(String[] sequencerInfoData) {
        this.sequencerInfoData = sequencerInfoData;
    }

    public void initGridPane() {
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
    }

    private void initLabel() {
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
                sequencerInfoData = data.split(":", 0);
                updateSequencerInfo();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        label.setOnMouseClicked(event -> {
            controller.setCurrentStepSequencer(this);
        });
    }

    public void setController(StepSequencerController controller) {
        this.controller = controller;
    }

    public void updateSequencerInfo() {
        if (sequencerInfoData == null) {
            return;
        }
        switch (sequencerInfoData[0]) {
            case "ANIMATION":
                type = Type.ANIMATION;
                sequencerInfo = controller.getAnimationInfo(sequencerInfoData[1]);;
                label.setText(sequencerInfo.toString());
                break;
            default:
                break;
        }
    }

    public Boolean[] getBoolArray() {
        Boolean[] result = new Boolean[buttons.size()];
        return buttons.stream().map(SequencerButton::isEnable)
                .collect(Collectors.toList()).toArray(result);
    }

    public void setBoolArray(Boolean[] boolArray) {
        this.boolArray = boolArray;
    }

    public void updateButtons() {
        int i = 0;
        for (SequencerButton button : buttons) {
            if (boolArray.length <= i) {
                button.setEnable(false);
            } else {
                button.setEnable(boolArray[i++]);
            }
        }
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
