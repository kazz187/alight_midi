package zone.kaz.alight_midi.gui.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import zone.kaz.alight_midi.gui.MenuManager;
import zone.kaz.alight_midi.gui.preferences.PreferencesWindow;
import zone.kaz.alight_midi.inject.DIContainer;

import java.io.IOException;

public class MainWindow {

    static private final String resourceFilename = "Main.fxml";
    private Stage stage;
    private PreferencesWindow preferencesWindow = DIContainer.get(PreferencesWindow.class);

    public MainWindow(Stage stage) {
        this.stage = stage;
        stage.setTitle("Alight MIDI");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(resourceFilename));
            BorderPane rootPane = (BorderPane) root.lookup("#root");
            MenuManager menuManager = MenuManager.getInstance();
            rootPane.setTop(menuManager.getMenuBar());
            menuManager.getItem("Preferences").setOnAction(event -> preferencesWindow.show());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(new Scene(root, 800, 600));
    }

    public void show() {
        stage.show();
    }

}
