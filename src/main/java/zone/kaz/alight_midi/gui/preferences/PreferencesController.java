package zone.kaz.alight_midi.gui.preferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.midi.MappingData;
import zone.kaz.alight_midi.device.midi.MidiControllerMapping;
import zone.kaz.alight_midi.device.midi.MidiData;
import zone.kaz.alight_midi.device.midi.MidiDevicePair;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.inject.DIContainer;

import javax.sound.midi.MidiDevice;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @FXML
    private TableView<MappingData> midiMapTable;
    @FXML
    private TableColumn<MappingData, String> functionColumn;
    @FXML
    private TableColumn<MappingData, MidiData> assignToPressedColumn;
    @FXML
    private TableColumn<MappingData, MidiData> assignToReleasedColumn;
    @FXML
    private CheckBox editModeCheck;
    @FXML
    ObservableList<MappingData> mappingDataList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
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

        functionColumn.setCellValueFactory(new PropertyValueFactory<>("processorName"));
        assignToPressedColumn.setCellValueFactory(new PropertyValueFactory<>("pressedMidiData"));
        assignToReleasedColumn.setCellValueFactory(new PropertyValueFactory<>("releasedMidiData"));
        //setMappingData();
        editModeCheck.setOnAction(e -> {
            if (editModeCheck.isSelected()) {
                MidiDevicePair currentDevicePair = getDevicePair();
                currentDevicePair.setMappingEditMode(true);
                currentDevicePair.setMappingStart(true);
            } else {
                MidiDevicePair currentDevicePair = getDevicePair();
                currentDevicePair.setMappingEditMode(false);
            }
        });
        mappingDataList = midiMapTable.getItems();
    }

    public void saveMappingMidiData(MidiData on, MidiData off) {
        TableView.TableViewSelectionModel<MappingData> model = midiMapTable.getSelectionModel();
        MappingData mappingData = mappingDataList.get(model.getSelectedIndex());
        mappingData.setPressedMidiData(on);
        mappingData.setReleasedMidiData(off);
        MidiControllerMapping mapping = getDevicePair().getReceiver().getMapping();
        mappingDataList.forEach(mapping::setMappingData);
        ObjectMapper objectMapper = new ObjectMapper();
        String controllerName = getDevicePair().getInputDevice().getDevice().getDeviceInfo().getName();
        String fileName = StepSequencerController.MAPPING_DIR_PATH + "/" + controllerName + ".json";
        try {
            MapSetting mapSetting = new MapSetting();
            mapSetting.setMappingList(new ArrayList<>(mappingDataList));
            objectMapper.writeValue(new File(fileName), mapSetting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MidiDevicePair getDevicePair() {
        MidiDeviceManager manager = DIContainer.get(MidiDeviceManager.class);
        return manager.getEnabledDevicePair(0);
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

    public void setMappingData() {
        MidiDevicePair devicePair = getDevicePair();
        Set<String> processorNameList = devicePair.getProcessorNameSet();
        Set<String> functionNameList = new HashSet<>();
        Pattern pattern = Pattern.compile("^(.*)_O[NF]F?$");
        for (String name : processorNameList) {
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                functionNameList.add(matcher.group(1));
            }
        }
        Set<MappingData> mappingDataSet = functionNameList.stream()
                .map(functionName -> new MappingData(functionName, new MidiData(), new MidiData()))
                .collect(Collectors.toSet());
        mappingDataList.removeAll(mappingDataList);
        mappingDataList.addAll(mappingDataSet.stream().collect(Collectors.toList()));
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
            loadDeviceMapping();
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

    private void loadDeviceMapping() {
        String controllerName = getDevicePair().getInputDevice().getDevice().getDeviceInfo().getName();
        String fileName = StepSequencerController.MAPPING_DIR_PATH + "/" + controllerName + ".json";
        if (!new File(fileName).exists()) {
            setMappingData();
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = Files.readAllLines(Paths.get(fileName)).stream().collect(Collectors.joining());
            ObservableList<MappingData> data = FXCollections.observableArrayList(objectMapper.readValue(json, MapSetting.class).getMappingList());
            mappingDataList.removeAll(mappingDataList);
            mappingDataList.addAll(data.stream().collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MidiControllerMapping mapping = getDevicePair().getReceiver().getMapping();
        mappingDataList.forEach(mapping::setMappingData);
    }

}
