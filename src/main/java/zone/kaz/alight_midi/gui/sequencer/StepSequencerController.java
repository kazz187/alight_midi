package zone.kaz.alight_midi.gui.sequencer;

import com.google.common.reflect.ClassPath;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

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
            stepSequencerManager.getPattern().add(new StepSequencer(
                    this,
                    i,
                    stepSequencerManager.getPattern().getCalcClock(),
                    stepSequencerManager.getPattern().getBeats(),
                    colWidth
            ));
        }
        clockFader.valueProperty().addListener(event -> {
            stepSequencerManager.getPattern().setClock((int) clockFader.getValue());
            int clock = stepSequencerManager.getPattern().getCalcClock();
            int beats = stepSequencerManager.getPattern().getBeats();
            clockLabel.setText(String.valueOf(clock));
        });
        rateFader.valueProperty().addListener(event -> {
            stepSequencerManager.getPattern().setRate((int) rateFader.getValue());
            rateLabel.setText(String.valueOf(stepSequencerManager.getPattern().getCalcRate()));
        });
        addSequence.setOnAction(event -> {
            stepSequencerManager.getPattern().add(new StepSequencer(
                    this,
                    stepSequencerManager.getPattern().getSize(),
                    stepSequencerManager.getPattern().getCalcClock(),
                    stepSequencerManager.getPattern().getBeats(),
                    colWidth));
        });
        removeSequence.setOnAction(event -> {
            stepSequencerManager.getPattern().remove();
        });
        sequencerGrid.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            double labelWidth = sequencerGrid.getColumnConstraints().get(0).getPrefWidth();
            int clock = stepSequencerManager.getPattern().getCalcClock();
            colWidth = (newValue.doubleValue() - labelWidth) / clock + 1;
            stepSequencerManager.getPattern().setButtonWidth(colWidth);
        });
        stepSequencerManager.getPattern().setClock(0);
        patternSave.setOnAction(event -> {
            System.out.println(patternNameField.getText());
        });
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

}
