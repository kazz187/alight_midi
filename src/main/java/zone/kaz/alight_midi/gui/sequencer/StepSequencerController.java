package zone.kaz.alight_midi.gui.sequencer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

import java.net.URL;
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

    private double colWidth = 0;

    private StepSequencerManager stepSequencerManager = DIContainer.get(StepSequencerManager.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
        for (int i = 0; i < 3; i++) {
            stepSequencerManager.add(new StepSequencer(
                    sequencerGrid,
                    i,
                    stepSequencerManager.getCalcClock(),
                    stepSequencerManager.getBeats(),
                    colWidth
            ));
        }
        clockFader.valueProperty().addListener(event -> {
            stepSequencerManager.setClock((int) clockFader.getValue());
            int clock = stepSequencerManager.getCalcClock();
            int beats = stepSequencerManager.getBeats();
            clockLabel.setText(String.valueOf(clock));
        });
        rateFader.valueProperty().addListener(event -> {
            stepSequencerManager.setRate((int) rateFader.getValue());
            rateLabel.setText(String.valueOf(stepSequencerManager.getRate()));
        });
        addSequence.setOnAction(event -> {
            stepSequencerManager.add(new StepSequencer(
                    sequencerGrid,
                    stepSequencerManager.getSize(),
                    stepSequencerManager.getCalcClock(),
                    stepSequencerManager.getBeats(),
                    colWidth));
        });
        sequencerGrid.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            double labelWidth = sequencerGrid.getColumnConstraints().get(0).getPrefWidth();
            int clock = stepSequencerManager.getCalcClock();
            colWidth = (newValue.doubleValue() - labelWidth) / clock + 1;
            stepSequencerManager.setButtonWidth(colWidth);
        });
        stepSequencerManager.setClock(0);
    }

    public GridPane getSequencerGrid() {
        return sequencerGrid;
    }

}
