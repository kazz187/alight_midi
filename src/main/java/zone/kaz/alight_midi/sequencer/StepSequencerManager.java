package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.sequencer.StepSequencer;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.inject.DIContainer;

import java.util.ArrayList;

import static zone.kaz.alight_midi.gui.sequencer.StepSequencer.COLUMN_INDEX_BOX;

@Singleton
public class StepSequencerManager {

    private int rate = 1;
    private int clock = 0;
    private int beats = 4;
    private ArrayList<StepSequencer> stepSequencerList = new ArrayList<>();
    private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);

    public StepSequencerManager() {
    }

    public void add(StepSequencer stepSequencer) {
        stepSequencerList.add(stepSequencer);
    }

    public int getSize() {
        return stepSequencerList.size();
    }

    public double getRate() {
        return Math.pow(2, rate);
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getCalcClock() {
        return (int) Math.pow(2, clock) * beats;
    }

    public void setClock(int clock) {
        this.clock = clock;
        int calcClock = getCalcClock();
        int beats = getBeats();
        for (StepSequencer stepSequencer : stepSequencerList) {
            stepSequencer.setClock(calcClock, beats);
        }

        StepSequencerController stepSequencerController = (StepSequencerController) controllerManager.get(StepSequencerController.class);
        GridPane gridPane = stepSequencerController.getSequencerGrid();
        int currentClock = getCalcClock();
        ObservableList<ColumnConstraints> constraintsList = gridPane.getColumnConstraints();
        if (constraintsList.size() - COLUMN_INDEX_BOX >= currentClock) {
            constraintsList.remove(currentClock, constraintsList.size() - COLUMN_INDEX_BOX);
            return;
        }
        double minWidth = 10;
        double prefWidth = 10;
        double maxWidth = Control.USE_COMPUTED_SIZE;
        for (int i = constraintsList.size() - COLUMN_INDEX_BOX; i < currentClock; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(
                    minWidth, prefWidth, maxWidth
            );
            columnConstraints.setFillWidth(true);
            columnConstraints.setPercentWidth(-1);
            columnConstraints.setHalignment(HPos.LEFT);
            columnConstraints.setHgrow(Priority.SOMETIMES);
            if (gridPane.getColumnConstraints().size() > i + COLUMN_INDEX_BOX) {
                gridPane.getColumnConstraints().set(i + COLUMN_INDEX_BOX, columnConstraints);
            } else {
                gridPane.getColumnConstraints().add(i + COLUMN_INDEX_BOX, columnConstraints);
            }
        }
    }

    public int getBeats() {
        return beats;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

    public void setButtonWidth(double buttonWidth) {
        for (StepSequencer stepSequencer : stepSequencerList) {
            stepSequencer.setButtonWidth(buttonWidth);
        }
    }
}
