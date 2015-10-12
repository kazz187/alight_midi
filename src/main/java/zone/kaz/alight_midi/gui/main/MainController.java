package zone.kaz.alight_midi.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.*;

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
            System.out.println("test");
        });
        stopButton.setOnAction(event -> {
            System.out.println("test");

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
        System.out.println(bpmField.getText());
    }

    private void prepareDeviceList() {

    }

}
