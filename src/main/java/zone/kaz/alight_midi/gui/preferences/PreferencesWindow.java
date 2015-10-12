package zone.kaz.alight_midi.gui.preferences;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PreferencesWindow {
    static private PreferencesWindow preferencesWindow = new PreferencesWindow();
    static private final String resource_filename = "Preferences.fxml";
    private Stage stage = new Stage();

    private PreferencesWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(resource_filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root, 800, 600));
    }

    static public PreferencesWindow getInstance() {
        return preferencesWindow;
    }

    public void show() {
        stage.show();
    }

}
