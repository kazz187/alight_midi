package zone.kaz.alight_midi.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import zone.kaz.alight_midi.device.DeviceBufferManager;
import zone.kaz.alight_midi.device.LedDeviceManager;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.led.DeviceInfo;
import zone.kaz.alight_midi.gui.main.MainWindow;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerWindow;
import zone.kaz.alight_midi.inject.AlightModule;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

import javax.sound.midi.MidiDevice;
import java.io.IOException;

public class MainApplication extends Application {

    public static void main(String[] args) {
        DIContainer.setModule(new AlightModule());
        MidiDeviceManager deviceManager = DIContainer.get(MidiDeviceManager.class);
        deviceManager.reloadDevices();
        MidiDevice.Info inputDevice = null, outputDevice = null;
        if (deviceManager.getInputDevices().size() > 0) {
            inputDevice = deviceManager.getInputDevices().get(0);
        }
        if (deviceManager.getOutputDevices().size() > 0) {
            outputDevice = deviceManager.getOutputDevices().get(0);
        }
        deviceManager.registerDevice(0, inputDevice, outputDevice);

        DeviceBufferManager bufferManager = DIContainer.get(DeviceBufferManager.class);

        // TODO: move to preference
        try {
            DeviceInfo deviceInfo = bufferManager.registerDeviceInfo(StepSequencerController.DEVICE_DIR_PATH + "/led8.json");
            LedDeviceManager ledDeviceManager = DIContainer.get(LedDeviceManager.class);
            ledDeviceManager.openDevice(deviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClockManager clockManager = DIContainer.get(ClockManager.class);
        clockManager.setPriority(Thread.MAX_PRIORITY);
        clockManager.start();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainWindow mainWindow = new MainWindow(stage);
        mainWindow.show();
        StepSequencerWindow stepSequencerWindow = DIContainer.get(StepSequencerWindow.class);
        stepSequencerWindow.show();
    }

    @Override
    public void stop() throws Exception {
        DIContainer.get(MidiDeviceManager.class).finish();
        DIContainer.get(LedDeviceManager.class).closeAllDevices();
        System.out.println("Device finished");
        super.stop();
        System.exit(0);
    }

}
