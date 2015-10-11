package zone.kaz.alight_midi.gui.preferences;

import com.sun.media.sound.MidiInDeviceProvider;
import com.sun.media.sound.MidiOutDeviceProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import zone.kaz.alight_midi.device.MidiDeviceManager;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kazz on 2015/09/28.
 */
public class PreferencesController implements Initializable {

    private static final Class MIDI_IN = MidiInDeviceProvider.class;
    private static final Class MIDI_OUT = MidiOutDeviceProvider.class;

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
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
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
                }
        );
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
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        ObservableList<MidiDevice.Info> inputItems = preferencesMidiInput.getItems();
        ObservableList<MidiDevice.Info> outputItems = preferencesMidiOutput.getItems();
        for (MidiDevice.Info device : devices) {
            if (device.getClass().getEnclosingClass().equals(MIDI_IN)) {
                if (!inputItems.contains(device)) {
                    inputItems.add(device);
                }
            } else if (device.getClass().getEnclosingClass().equals(MIDI_OUT)) {
                if (!outputItems.contains(device)) {
                    outputItems.add(device);
                }
            }
        }

        preferencesMidiInput.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MidiDevice.Info deviceInfo = preferencesMidiInput.getValue();
                MidiDeviceManager.getInstance().registerInputDevice(deviceInfo);
            }
        });
        preferencesMidiOutput.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                MidiDevice.Info deviceInfo = preferencesMidiOutput.getValue();
                MidiDeviceManager.getInstance().registerOutputDevice(deviceInfo);
            }
        });

        if (preferencesMidiInput.getValue() == null && inputItems.size() > 0) {
            preferencesMidiInput.setValue(inputItems.get(0));
        }
        if (preferencesMidiOutput.getValue() == null && outputItems.size() > 0) {
            preferencesMidiOutput.setValue(outputItems.get(0));
        }
    }

}
