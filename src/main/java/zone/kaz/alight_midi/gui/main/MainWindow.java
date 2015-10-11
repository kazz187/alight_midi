package zone.kaz.alight_midi.gui.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import zone.kaz.alight_midi.gui.MenuManager;
import zone.kaz.alight_midi.gui.preferences.PreferencesWindow;

import java.io.IOException;

/**
 * Created by kazz on 2015/10/11.
 */
public class MainWindow {
    static private final String resource_filename = "Main.fxml";
    private Stage stage;

    public MainWindow(Stage stage) {
        this.stage = stage;
        stage.setTitle("Alight MIDI");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(resource_filename));
            BorderPane rootPane = (BorderPane) root.lookup("#root");
            MenuManager menuManager = MenuManager.getInstance();
            rootPane.setTop(menuManager.getMenuBar());
            menuManager.getItem("Preferences").setOnAction(event -> PreferencesWindow.getInstance().show());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(new Scene(root, 800, 600));
    }

    public void show() {
        stage.show();
    }

}
