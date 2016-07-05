package zone.kaz.alight_midi.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.device.sequence_display.VirtualSequenceDisplay;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

public class MainController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button tapButton;
    @FXML
    private Button nudgeUpButton;
    @FXML
    private Button nudgeDownButton;
    @FXML
    private TextField bpmField;
    @FXML
    private Rectangle sequenceDisplay0;
    @FXML
    private Rectangle sequenceDisplay1;
    @FXML
    private Rectangle sequenceDisplay2;
    @FXML
    private Rectangle sequenceDisplay3;
    @FXML
    private Slider channelFader1;
    @FXML
    private Slider channelFader2;
    @FXML
    private Slider crossFader;
    @FXML
    private Slider masterFader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClockManager clockManager = DIContainer.get(ClockManager.class);
        playButton.setOnAction(event -> {
            clockManager.playSequencer();
        });
        stopButton.setOnAction(event -> {
            clockManager.stopSequencer();
        });
        tapButton.setOnAction(event -> {
            clockManager.tapBpm();
        });
        nudgeUpButton.setOnMousePressed(event -> {
            clockManager.onNudgePressed(1);
        });
        nudgeUpButton.setOnMouseReleased(event -> {
            clockManager.onNudgeReleased();
        });
        nudgeDownButton.setOnMousePressed(event -> {
            clockManager.onNudgePressed(-1);
        });
        nudgeDownButton.setOnMouseReleased(event -> {
            clockManager.onNudgeReleased();
        });
        bpmField.setOnKeyTyped(event -> {
            if (event.getCharacter().equals("\r")) {
                applyBpm();
            }
        });
        bpmField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                applyBpm();
            }
        });
        setupSequenceDisplay();
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
    }

    private void setupSequenceDisplay() {
        ArrayList<Rectangle> sequenceDisplayList = new ArrayList<>();
        sequenceDisplayList.add(sequenceDisplay0);
        sequenceDisplayList.add(sequenceDisplay1);
        sequenceDisplayList.add(sequenceDisplay2);
        sequenceDisplayList.add(sequenceDisplay3);
        SequenceDisplayManager manager = DIContainer.get(SequenceDisplayManager.class);
        manager.add(new VirtualSequenceDisplay(sequenceDisplayList));
    }

    private void applyBpm() {
        ClockManager clockManager = DIContainer.get(ClockManager.class);
        clockManager.setBpm(Double.valueOf(bpmField.getText()));
    }

    public void setBpm(double bpm) {
        bpmField.setText(String.valueOf(bpm));
    }

    public double getFader1() {
        return channelFader1.getValue();
    }

    public double getFader2() {
        return channelFader2.getValue();
    }

    public double getMasterFader() {
        return masterFader.getValue();
    }

    public void setMasterFaderToMax() {
        masterFader.setValue(masterFader.getMax());
    }

    public void setMasterFaderToMin() {
        masterFader.setValue(masterFader.getMin());
    }

    public double getCrossFader() {
        return crossFader.getValue();
    }

}
