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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playButton.setOnAction(event -> {
            ClockManager clockManager = DIContainer.getInjector().getInstance(ClockManager.class);
            clockManager.playSequencer();
        });
        stopButton.setOnAction(event -> {
            ClockManager clockManager = DIContainer.getInjector().getInstance(ClockManager.class);
            clockManager.stopSequencer();
        });
        tapButton.setOnAction(event -> {
            System.out.println("test");

        });
        nudgeUpButton.setOnAction(event -> {
            System.out.println("test");

        });
        nudgeDownButton.setOnAction(event -> {
            System.out.println("test");

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
    }

    private void setupSequenceDisplay() {
        ArrayList<Rectangle> sequenceDisplayList = new ArrayList<>();
        sequenceDisplayList.add(sequenceDisplay0);
        sequenceDisplayList.add(sequenceDisplay1);
        sequenceDisplayList.add(sequenceDisplay2);
        sequenceDisplayList.add(sequenceDisplay3);
        SequenceDisplayManager manager = DIContainer.getInjector().getInstance(SequenceDisplayManager.class);
        manager.add(new VirtualSequenceDisplay(sequenceDisplayList));
    }

    private void applyBpm() {
        ClockManager clockManager = DIContainer.getInjector().getInstance(ClockManager.class);
        clockManager.setBpm(Double.valueOf(bpmField.getText()));
    }

}