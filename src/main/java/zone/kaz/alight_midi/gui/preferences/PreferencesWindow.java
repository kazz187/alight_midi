package zone.kaz.alight_midi.gui.preferences;

import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

@Singleton
public class PreferencesWindow {

    static private final String resourceFilename = "Preferences.fxml";
    private Stage stage = new Stage();

    public PreferencesWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(resourceFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root, 800, 600));
    }

    public void show() {
        stage.show();
    }

}
