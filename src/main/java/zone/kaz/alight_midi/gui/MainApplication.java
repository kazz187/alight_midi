package zone.kaz.alight_midi.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.gui.main.MainWindow;
import zone.kaz.alight_midi.inject.AlightModule;
import zone.kaz.alight_midi.inject.DIContainer;

import javax.sound.midi.MidiDevice;

public class MainApplication extends Application {

    public static void main(String[] args) {
        DIContainer.setModule(new AlightModule());
        MidiDeviceManager deviceManager = DIContainer.getInjector().getInstance(MidiDeviceManager.class);
        deviceManager.reloadDevices();
        MidiDevice.Info inputDevice = null, outputDevice = null;
        if (deviceManager.getInputDevices().size() > 0) {
            inputDevice = deviceManager.getInputDevices().get(0);
        }
        if (deviceManager.getOutputDevices().size() > 0) {
            outputDevice = deviceManager.getOutputDevices().get(0);
        }
        deviceManager.registerDevice(0, inputDevice, outputDevice);
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.show();
    }

    @Override
    public void stop() throws Exception {
        DIContainer.getInjector().getInstance(MidiDeviceManager.class).finish();
        System.out.println("Device finished");
        super.stop();
        System.exit(0);
    }

}
