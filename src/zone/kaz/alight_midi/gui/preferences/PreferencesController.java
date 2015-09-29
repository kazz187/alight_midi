package zone.kaz.alight_midi.gui.preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kazz on 2015/09/28.
 */
public class PreferencesController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private TabPane preferences;
    @FXML
    private Tab preferencesMidi;
    @FXML
    private SplitMenuButton preferencesMidiInput;
    @FXML
    private SplitMenuButton preferencesMidiOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (preferences == null) return;
        preferences.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        System.out.println(newValue.getId());
                        if (newValue.getId().equals("preferencesMidi")) {
                            preferencesMidiInput.getItems().addAll(new MenuItem("hoge"));
                        }
                    }
                }
        );
    }

    @FXML
    public void onSelectMidiTab(ActionEvent event) {
        System.out.println("midi selected");
    }

/*    public PreferencesController(Parent root) {
        this.root = root;
    }*/



}
