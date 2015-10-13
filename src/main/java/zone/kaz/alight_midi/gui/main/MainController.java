package zone.kaz.alight_midi.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import java.net.URL;
import java.time.Clock;
import java.util.ResourceBundle;
import javafx.scene.control.*;
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
    }

    private void applyBpm() {
        ClockManager clockManager = DIContainer.getInjector().getInstance(ClockManager.class);
        clockManager.setBpm(Double.valueOf(bpmField.getText()));
    }

    private void prepareDeviceList() {

    }

}
