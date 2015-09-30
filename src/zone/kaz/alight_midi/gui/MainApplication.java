package zone.kaz.alight_midi.gui;
/**
 * Created by kazz on 2015/09/28.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import zone.kaz.alight_midi.device.MidiDeviceManager;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        MenuManager menuManager = MenuManager.getInstance();
        pane.setTop(menuManager.getMenuBar());
        primaryStage.setTitle("Alight MIDI");
        menuManager.getItem("Preferences").setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PreferencesWindow.getInstance().show();
            }
        });


        Label label = new Label("This is JavaFX!");
        pane.setCenter(label);



        Scene scene = new Scene(pane, 320, 240);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        MidiDeviceManager.getInstance().finish();
        System.out.println("Device finished");
        super.stop();
    }

}
