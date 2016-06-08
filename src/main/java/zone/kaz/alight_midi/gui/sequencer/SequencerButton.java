package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class SequencerButton {

    public final static Paint BEAT_COLOR = Paint.valueOf("#F4EEE1");
    public final static Paint NORMAL_COLOR = Paint.valueOf("#EFEEEE");
    public final static Paint ON_COLOR = Paint.valueOf("#EBCFC4");
    public final static Paint STROKE_COLOR = Paint.valueOf("#C4BDAC");

    private Rectangle rectangle;
    private Paint offColor;
    private boolean isEnable;

    public SequencerButton(double buttonWidth, boolean isEnable, boolean isBeat) {
        offColor = isBeat ? BEAT_COLOR : NORMAL_COLOR;
        rectangle = new Rectangle();
        rectangle.setHeight(30);
        rectangle.setWidth(buttonWidth);
        rectangle.setStrokeWidth(1.0);
        rectangle.setStroke(STROKE_COLOR);
        setEnable(isEnable);
        rectangle.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    setEnable(!this.isEnable);
                    break;
                case SECONDARY:
                    setEnable(false);
                    break;
                default:
                    break;
            }
        });
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        rectangle.setFill(isEnable ? ON_COLOR : offColor);
        this.isEnable = isEnable;
    }

    public Rectangle getShape() {
        return rectangle;
    }

    public void setWidth(double width) {
        rectangle.setWidth(width);
    }

}
