package zone.kaz.alight_midi.gui.sequencer;

import com.google.common.reflect.ClassPath;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;
import zone.kaz.alight_midi.sequencer.StepSequencerPattern;

import static zone.kaz.alight_midi.gui.sequencer.StepSequencer.COLUMN_INDEX_BOX;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class StepSequencerController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private ListView<SequencerInfo> animationList;
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
    @FXML
    private TextField patternNameField;
    @FXML
    private Button patternSave;

    private double colWidth = 0;

    private StepSequencerManager stepSequencerManager = DIContainer.get(StepSequencerManager.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAnimationList();
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
        for (int i = 0; i < 3; i++) {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            pattern.add(new StepSequencer(
                    this,
                    i,
                    pattern.getCalcClock(),
                    pattern.getBeats(),
                    colWidth
            ));
        }
        clockFader.valueProperty().addListener(event -> {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            pattern.setClock((int) clockFader.getValue());
            updateStepSequencer(pattern);
            int clock = pattern.getCalcClock();
            int beats = pattern.getBeats();
            clockLabel.setText(String.valueOf(clock));
        });
        rateFader.valueProperty().addListener(event -> {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            pattern.setRate((int) rateFader.getValue());
            rateLabel.setText(String.valueOf(pattern.getCalcRate()));
        });
        addSequence.setOnAction(event -> {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            pattern.add(new StepSequencer(
                    this,
                    pattern.getSize(),
                    pattern.getCalcClock(),
                    pattern.getBeats(),
                    colWidth));
        });
        removeSequence.setOnAction(event -> {
            stepSequencerManager.getPattern().remove();
        });
        sequencerGrid.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            double labelWidth = sequencerGrid.getColumnConstraints().get(0).getPrefWidth();
            int clock = pattern.getCalcClock();
            colWidth = (newValue.doubleValue() - labelWidth) / clock + 1;
            pattern.setButtonWidth(colWidth);
        });
        patternSave.setOnAction(event -> {
            System.out.println(patternNameField.getText());
        });
        stepSequencerManager.getPattern().setClock(0);
        updateStepSequencer(stepSequencerManager.getPattern());
    }

    private void loadAnimationList() {
        animationList.setOnDragDetected(event -> {
            MultipleSelectionModel<SequencerInfo> items = animationList.getSelectionModel();
            SequencerInfo item = items.getSelectedItem();
            int index = animationList.itemsProperty().getValue().indexOf(item);
            Dragboard db = animationList.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("ANIMATION:" + index);
            db.setContent(content);
            event.consume();
        });
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Set<ClassPath.ClassInfo> classInfoSet = ClassPath.from(loader).getTopLevelClasses("zone.kaz.alight_midi.sequencer.animation");
            for (ClassPath.ClassInfo classInfo : classInfoSet) {
                AnimationInfo item = new AnimationInfo(classInfo);
                this.animationList.itemsProperty().getValue().add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GridPane getSequencerGrid() {
        return sequencerGrid;
    }

    public ListView<SequencerInfo> getAnimationList() {
        return animationList;
    }

    public void updateStepSequencer(StepSequencerPattern pattern) {
        int currentClock = pattern.getCalcClock();
        ObservableList<ColumnConstraints> constraintsList = sequencerGrid.getColumnConstraints();
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
            if (sequencerGrid.getColumnConstraints().size() > i + COLUMN_INDEX_BOX) {
                sequencerGrid.getColumnConstraints().set(i + COLUMN_INDEX_BOX, columnConstraints);
            } else {
                sequencerGrid.getColumnConstraints().add(i + COLUMN_INDEX_BOX, columnConstraints);
            }
        }
    }

}
