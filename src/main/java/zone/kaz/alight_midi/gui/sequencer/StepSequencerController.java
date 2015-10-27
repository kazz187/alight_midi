package zone.kaz.alight_midi.gui.sequencer;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.midi.MidiDevicePair;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

import javax.sound.midi.MidiDevice;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StepSequencerController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private GridPane sequencerGrid;
    @FXML
    private Button addSequence;
    @FXML
    private Button removeSequence;
    @FXML
    private Label rateLabel;
    @FXML
    private Slider rateFader;
    @FXML
    private Label clockLabel;
    @FXML
    private Slider clockFader;

    private StepSequencerManager stepSequencerManager = DIContainer.get(StepSequencerManager.class);
    private ArrayList<StepSequencer> stepSequencerList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 3; i++) {
            stepSequencerList.add(new StepSequencer(
                    sequencerGrid,
                    i,
                    stepSequencerManager.getClock(),
                    stepSequencerManager.getBeats()
            ));
        }
        clockFader.valueProperty().addListener(event -> {
            stepSequencerManager.setClock((int) clockFader.getValue());
            int clock = stepSequencerManager.getClock();
            int beats = stepSequencerManager.getBeats();
            clockLabel.setText(String.valueOf(clock));
            for (StepSequencer stepSequencer : stepSequencerList) {
                stepSequencer.setClock(clock, beats);
            }
        });
        rateFader.valueProperty().addListener(event -> {
            stepSequencerManager.setRate((int) rateFader.getValue());
            rateLabel.setText(String.valueOf(stepSequencerManager.getRate()));
        });
        addSequence.setOnAction(event -> {
            stepSequencerList.add(new StepSequencer(
                    sequencerGrid,
                    stepSequencerList.size(),
                    stepSequencerManager.getClock(),
                    stepSequencerManager.getBeats()
            ));
        });
    }



}
