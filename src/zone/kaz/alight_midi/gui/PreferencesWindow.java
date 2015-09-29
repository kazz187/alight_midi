package zone.kaz.alight_midi.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by kazz on 2015/09/28.
 */
public class PreferencesWindow {
    static private PreferencesWindow preferencesWindow = new PreferencesWindow();
    private Stage stage = new Stage();

    private PreferencesWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("zone/kaz/alight_midi/gui/preferences/Preferences.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root, 800, 600));
    }

    static PreferencesWindow getInstance() {
        return preferencesWindow;
    }

    public void show() {
        stage.show();
    }

}
