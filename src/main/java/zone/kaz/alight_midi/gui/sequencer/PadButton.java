package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class PadButton {

    public final static Paint UNLOADED_COLOR = Paint.valueOf("#EFEEEE");
    public final static Paint LOADED_COLOR = Paint.valueOf("#F4EEE1");
    public final static Paint ON_COLOR = Paint.valueOf("#EBCFC4");
    public final static Paint STROKE_COLOR = Paint.valueOf("#C4BDAC");

    private Rectangle rectangle;
    private Label label;
    private boolean isReady = false;
    private boolean isEnable = false;

    public PadButton() {
        rectangle = new Rectangle();
        rectangle.setWidth(70);
        rectangle.setHeight(40);
        rectangle.setStrokeWidth(1.0);
        rectangle.setStroke(STROKE_COLOR);
        rectangle.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    setEnable(true);
                    break;
                default:
                    break;
            }
        });
        rectangle.setOnMouseReleased(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    setEnable(false);
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

    public void loadPattern(String patternName) {
        setReady(true);
        label.setText(patternName);

    }

    public void unloadPattern() {
        setReady(false);
        label.setText("");
    }

    private void setReady(boolean isReady) {
        rectangle.setFill(isReady ? LOADED_COLOR : UNLOADED_COLOR);
        this.isReady = isReady;
    }

    private void setEnable(boolean isEnable) {
        rectangle.setFill(isEnable ? ON_COLOR : isReady ? LOADED_COLOR : UNLOADED_COLOR);
        this.isEnable = isEnable;
    }

    public Rectangle getShape() {
        return rectangle;
    }

    public Label getLabel() {
        return label;
    }

}
