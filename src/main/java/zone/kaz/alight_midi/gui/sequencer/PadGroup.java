package zone.kaz.alight_midi.gui.sequencer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Tab;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class PadGroup {
    private final StepSequencerController controller;
    private final StepSequencerManager manager;

    public static final int PAD_NUM_WIDTH = 8;
    public static final int PAD_NUM_HEIGHT = 4;

    private PadButton[][] padButtons = new PadButton[PAD_NUM_WIDTH][PAD_NUM_HEIGHT];

    private String groupName = "";

    public PadGroup(StepSequencerController stepSequencerController, StepSequencerManager stepSequencerManager) {
        this.controller = stepSequencerController;
        this.manager = stepSequencerManager;
    }

    void createPadButtons(Tab tab) {
        groupName = tab.getText();
        AnchorPane pane = (AnchorPane) tab.getContent();
        String filePath = StepSequencerController.PAD_DIR_PATH + "/" + groupName + ".json";
        String[][] padInfo = loadPadInfo(filePath);
        pane.getChildren().forEach(child -> {
            GridPane p = (GridPane) child;
            for (int i = 0; i < PAD_NUM_HEIGHT; i++) {
                for (int j = 0; j < PAD_NUM_WIDTH; j++) {
                    PadButton padButton = new PadButton(controller, manager);
                    padButton.getShape().setOnDragOver(event -> {
                        Dragboard db = event.getDragboard();
                        if (db.hasString()) {
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    });
                    padButton.getShape().setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasString()) {
                            String data = db.getString();
                            String[] sequencerInfoData = data.split(":", 0);
                            if (sequencerInfoData.length == 2) {
                                padButton.updateSequencerInfo(
                                        sequencerInfoData,
                                        controller.getPatternInfo(sequencerInfoData[1])
                                );
                                savePad();
                                success = true;
                            }
                        }
                        event.setDropCompleted(success);
                        event.consume();
                    });
                    padButton.getLabel().setOnDragOver(event -> padButton.getShape().fireEvent(event));
                    padButton.getLabel().setOnDragDropped(event -> padButton.getShape().fireEvent(event));
                    p.add(padButton.getShape(), j, i);
                    p.add(padButton.getLabel(), j, i);
                    if (padInfo[j][i] != null) {
                        PatternInfo patternInfo = controller.getPatternInfo(padInfo[j][i]);
                        if (patternInfo != null) {
                            padButton.loadPattern(patternInfo);
                        }
                    }
                    padButtons[j][i] = padButton;
                }
            }
        });
    }

    private String[][] loadPadInfo(String filePath) {
        String[][] padInfo = new String[PAD_NUM_WIDTH][PAD_NUM_HEIGHT];
        try {
            String json = Files.readAllLines(Paths.get(filePath)).stream().collect(Collectors.joining());
            ObjectMapper objectMapper = new ObjectMapper();
            List<List<String>> padInfoTmp = objectMapper.readValue(json, new TypeReference<List<List<String>>>() {});
            int j = 0;
            for (List<String> padList : padInfoTmp) {
                int i = 0;
                for (String animationName : padList) {
                    padInfo[j][i] = animationName;
                    i++;
                }
                j++;
            }
        } catch (NoSuchFileException e) {
            // skip
        } catch (IOException e) {
            e.printStackTrace();
        }
        return padInfo;
    }

    private void savePad() {
        String[][] loadedPatternList = createLoadedPatternList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(StepSequencerController.PAD_DIR_PATH + "/" + groupName + ".json"), loadedPatternList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[][] createLoadedPatternList() {
        String[][] loadedPatternList = new String[PAD_NUM_WIDTH][PAD_NUM_HEIGHT];
        for (int i = 0; i < PAD_NUM_HEIGHT; i++) {
            for (int j = 0; j < PAD_NUM_WIDTH; j++) {
                loadedPatternList[j][i] = padButtons[j][i].getLabel().getText();
            }
        }
        return loadedPatternList;
    }

    public PadButton getPadButton(int x, int y) {
        return padButtons[x][y];
    }

}