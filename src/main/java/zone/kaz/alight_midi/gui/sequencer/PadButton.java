package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

public class PadButton {

    public final static Paint UNLOADED_COLOR = Paint.valueOf("#EFEEEE");
    public final static Paint LOADED_COLOR = Paint.valueOf("#F4EEE1");
    public final static Paint ON_COLOR = Paint.valueOf("#EBCFC4");
    public final static Paint STROKE_COLOR = Paint.valueOf("#C4BDAC");

    private Rectangle rectangle;
    private Label label;
    private boolean isReady = false;
    private boolean isEnabled = false;
    private PatternInfo patternInfo = null;
    private StepSequencerController controller;
    private StepSequencerManager manager;

    public PadButton(StepSequencerController controller, StepSequencerManager manager) {
        this.controller = controller;
        this.manager = manager;
        rectangle = new Rectangle();
        rectangle.setWidth(90);
        rectangle.setHeight(40);
        rectangle.setStrokeWidth(1.0);
        rectangle.setStroke(STROKE_COLOR);
        rectangle.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    setEnabled(true);
                    break;
                default:
                    break;
            }
        });
        rectangle.setOnMouseReleased(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    setEnabled(false);
                    break;
                default:
                    break;
            }
        });
        rectangle.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case SECONDARY:
                    unloadPattern();
                    break;
                default:
                    break;
            }
        });
        label = new Label();
        label.setOnMousePressed(event -> rectangle.fireEvent(event));
        label.setOnMouseReleased(event -> rectangle.fireEvent(event));
        label.setOnMouseClicked(event -> rectangle.fireEvent(event));
        unloadPattern();
    }

    public void updateSequencerInfo(String[] sequencerInfoData, PatternInfo sequencerInfo) {
        if (sequencerInfoData == null) {
            return;
        }
        switch (sequencerInfoData[0]) {
            case "PATTERN":
                loadPattern(sequencerInfo);
                break;
            default:
                break;
        }
    }

    public void loadPattern(PatternInfo patternInfo) {
        setReady(true);
        label.setText(patternInfo.toString());
        this.patternInfo = patternInfo;
    }

    public void unloadPattern() {
        setReady(false);
        label.setText("");
    }

    private void setReady(boolean isReady) {
        rectangle.setFill(isReady ? LOADED_COLOR : UNLOADED_COLOR);
        this.isReady = isReady;
    }

    public void setEnabled(boolean isEnabled) {
        rectangle.setFill(isEnabled ? ON_COLOR : (isReady ? LOADED_COLOR : UNLOADED_COLOR));
        if (isEnabled && patternInfo != null) {
            patternInfo.loadPattern(controller, manager);
        }
        this.isEnabled = isEnabled;
    }

    public Rectangle getShape() {
        return rectangle;
    }

    public Label getLabel() {
        return label;
    }

}
