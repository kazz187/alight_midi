package zone.kaz.alight_midi.gui;
/**
 * Created by kazz on 2015/09/28.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.gui.main.MainWindow;
import zone.kaz.alight_midi.gui.preferences.PreferencesWindow;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.show();
    }

    @Override
    public void stop() throws Exception {
        MidiDeviceManager.getInstance().finish();
        System.out.println("Device finished");
        super.stop();
        System.exit(0);
/*        Set<Thread> set = Thread.getAllStackTraces().keySet();
        for (Thread thread : set) {
            // if (thread.getName().equals("Java Sound MidiInDevice Thread")) {
            try {
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // }
        }*/
    }

}
