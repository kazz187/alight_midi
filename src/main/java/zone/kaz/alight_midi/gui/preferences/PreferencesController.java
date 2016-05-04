package zone.kaz.alight_midi.gui.preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.midi.MidiDevicePair;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;

import javax.sound.midi.MidiDevice;
import java.net.URL;
import java.util.ResourceBundle;

public class PreferencesController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private TabPane preferences;
    @FXML
    private Tab preferencesMidi;
    @FXML
    private ComboBox<MidiDevice.Info> preferencesMidiInput;
    @FXML
    private ComboBox<MidiDevice.Info> preferencesMidiOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializePreferencesMidi();
        preferences.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.getId() != null) {
                        switch (newValue.getId()) {
                            case "preferencesMidi":
                                prepareDeviceList();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
    }

    private void initializePreferencesMidi() {
        StringConverter<MidiDevice.Info> converter = new StringConverter<MidiDevice.Info>() {
            @Override
            public String toString(MidiDevice.Info object) {
                return object.getName() + " - " + object.getDescription();
            }

            @Override
            public MidiDevice.Info fromString(String string) {
                return null;
            }
        };
        preferencesMidiInput.setConverter(converter);
        preferencesMidiOutput.setConverter(converter);
    }

    private void prepareDeviceList() {
        MidiDeviceManager manager = DIContainer.get(MidiDeviceManager.class);
        manager.reloadDevices();

        ObservableList<MidiDevice.Info> inputList = FXCollections.observableList(manager.getInputDevices());
        preferencesMidiInput.setItems(inputList);
        ObservableList<MidiDevice.Info> outputList = FXCollections.observableList(manager.getOutputDevices());
        preferencesMidiOutput.setItems(outputList);

        preferencesMidiInput.setOnAction(event -> {
            MidiDevice.Info inputDeviceInfo = preferencesMidiInput.getValue();
            MidiDevice.Info outputDeviceInfo = preferencesMidiOutput.getValue();
            manager.registerDevice(0, inputDeviceInfo, outputDeviceInfo);
        });
        preferencesMidiOutput.setOnAction(event -> {
            MidiDevice.Info inputDeviceInfo = preferencesMidiInput.getValue();
            MidiDevice.Info outputDeviceInfo = preferencesMidiOutput.getValue();
            manager.registerDevice(0, inputDeviceInfo, outputDeviceInfo);
        });

        MidiDevicePair pair = manager.getEnabledDevicePair(0);
        if (pair.getInputDevice() != null) {
            MidiDevice inputDevice = pair.getInputDevice().getDevice();
            if (inputDevice != null) {
                preferencesMidiInput.setValue(inputDevice.getDeviceInfo());
            }
        }
        if (pair.getOutputDevice() != null) {
            MidiDevice outputDevice = pair.getOutputDevice().getDevice();
            if (outputDevice != null) {
                preferencesMidiOutput.setValue(outputDevice.getDeviceInfo());
            }
        }
    }

}
