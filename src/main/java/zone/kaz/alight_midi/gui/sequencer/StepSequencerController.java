package zone.kaz.alight_midi.gui.sequencer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.ClassPath;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;
import zone.kaz.alight_midi.sequencer.StepSequencerPattern;

import static zone.kaz.alight_midi.gui.sequencer.StepSequencer.COLUMN_INDEX_BOX;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StepSequencerController implements Initializable {

    @FXML
    private Parent root;
    @FXML
    private ListView<SequencerInfo> animationList;
    @FXML
    private ListView<SequencerInfo> colorList;
    @FXML
    private ListView<SequencerInfo> patternList;
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
    @FXML
    private TabPane padTabPane;
    @FXML
    private TextField paramsField;

    private ArrayList<Tab> tabs = new ArrayList<>();

    private double colWidth = 0;

    private StepSequencerManager stepSequencerManager = DIContainer.get(StepSequencerManager.class);
    private ClockManager clockManager = DIContainer.get(ClockManager.class);

    private HashMap<String, AnimationInfo> animationInfoMap = new HashMap<>();
    private HashMap<String, PatternInfo> patternInfoMap = new HashMap<>();
    private HashMap<String, PadGroup> padGroupMap = new HashMap<>();

    public static final String CONF_DIR_PATH = System.getProperty("user.home") + "/.alight_midi";
    public static final String PATTERN_DIR_PATH = CONF_DIR_PATH + "/pattern";
    private StepSequencer currentStepSequencer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: Move to Preferences.
        prepareConfDir();
        loadAnimationList();
        loadPatternList();
        loadPadTabPane();
        ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
        controllerManager.register(this);
        StepSequencerPattern defaultPattern = stepSequencerManager.getPattern();
        for (int i = 0; i < 3; i++) {
            defaultPattern.add(new StepSequencer(
                    this,
                    i,
                    defaultPattern.getCalcClock(),
                    defaultPattern.getBeats(),
                    colWidth
            ));
        }
        currentStepSequencer = defaultPattern.getStepSequencerList().get(0);
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
        patternNameField.setOnKeyTyped(event -> {
            if (Objects.equals(event.getCharacter(), "\r")) {
                patternSave.fire();
            }
        });
        patternSave.setOnAction(event -> {
            StepSequencerPattern pattern = stepSequencerManager.getPattern();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(new File(PATTERN_DIR_PATH + "/" + patternNameField.getText() + ".json"), pattern);
                loadPatternList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stepSequencerManager.getPattern().setClock(0);
        updateStepSequencer(stepSequencerManager.getPattern());
        paramsField.setOnKeyTyped(event -> {
            if (Objects.equals(event.getCharacter(), "\r")) {
                currentStepSequencer.setParams(paramsField.getText());
            }
        });
    }

    private void loadPadTabPane() {
        ObservableList<Tab> tabs = padTabPane.getTabs();
        tabs.forEach(tab -> this.tabs.add(tab));
        for (Tab tab : this.tabs) {
            PadGroup padGroup = new PadGroup(this, stepSequencerManager);
            padGroup.createPadButtons(tab);
            padGroupMap.put(tab.getText(), padGroup);
        }
    }

    private void prepareConfDir() {
        new File(CONF_DIR_PATH).mkdir();
        new File(PATTERN_DIR_PATH).mkdir();
    }

    public double getColWidth() {
        return colWidth;
    }

    public void setRate(int rate) {
        rateFader.valueProperty().set(rate);
    }

    public void setClock(int clock) {
        clockFader.valueProperty().set(clock);
    }

    private void loadAnimationList() {
        animationList.setOnDragDetected(event -> setDraggable(event, animationList, "ANIMATION"));
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Set<ClassPath.ClassInfo> classInfoSet = ClassPath.from(loader).getTopLevelClasses("zone.kaz.alight_midi.sequencer.animation");
            for (ClassPath.ClassInfo classInfo : classInfoSet) {
                AnimationInfo item = new AnimationInfo(classInfo);
                animationList.itemsProperty().getValue().add(item);
                animationInfoMap.put(item.toString(), item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPatternList() {
        patternList.setOnMouseClicked(event -> {
            MultipleSelectionModel<SequencerInfo> items = patternList.getSelectionModel();
            PatternInfo patternInfo = (PatternInfo) items.getSelectedItem();
            patternInfo.loadPattern(this, stepSequencerManager);
            event.consume();
        });
        patternList.setOnDragDetected(event -> setDraggable(event, patternList, "PATTERN"));
        patternList.itemsProperty().getValue().removeAll(
                patternList.itemsProperty().getValue()
        );
        File[] patternFileList = new File(PATTERN_DIR_PATH).listFiles();
        for (File patternFile : patternFileList) {
            PatternInfo patternInfo = new PatternInfo(patternFile.getAbsolutePath());
            patternList.itemsProperty().getValue().add(patternInfo);
            patternInfoMap.put(patternInfo.toString(), patternInfo);
        }
    }

    private void setDraggable(MouseEvent event, ListView<SequencerInfo> list, String tag) {
        MultipleSelectionModel<SequencerInfo> items = list.getSelectionModel();
        SequencerInfo item = items.getSelectedItem();
        Dragboard db = list.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(tag + ":" + item);
        db.setContent(content);
        event.consume();
    }

    public GridPane getSequencerGrid() {
        return sequencerGrid;
    }

    public void setPatternName(String patternName) {
        patternNameField.setText(patternName);
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

    public AnimationInfo getAnimationInfo(String key) {
        return animationInfoMap.get(key);
    }

    public PatternInfo getPatternInfo(String key) {
        return patternInfoMap.get(key);
    }

    private Tab getActivePadTab() {
        SingleSelectionModel<Tab> selectionModel = padTabPane.getSelectionModel();
        return selectionModel.getSelectedItem();
    }

    public void onPadPressed(int x, int y) {
        setPadButtonEnable(x, y, true);
    }

    public void onPadReleased(int x, int y) {
        setPadButtonEnable(x, y, false);
    }

    private void setPadButtonEnable(int x, int y, boolean isEnable) {
        if (isEnable) {
            clockManager.playSequencer();
        }
        PadGroup padGroup = padGroupMap.get(getActivePadTab().getText());
        if (padGroup == null) {
            System.err.println("padGroup is null.");
            return;
        }
        PadButton padButton = padGroup.getPadButton(x, y);
        Platform.runLater(() -> padButton.setEnabled(isEnable));
    }

    public String getPatternName() {
        return patternNameField.getText();
    }

    public void setCurrentStepSequencer(StepSequencer currentStepSequencer) {
        this.currentStepSequencer = currentStepSequencer;
        String params = null;
        if (currentStepSequencer != null) {
            params = currentStepSequencer.getParams();
        }
        paramsField.setText(params);
    }

}
