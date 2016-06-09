package zone.kaz.alight_midi.gui.sequencer;

import javafx.scene.control.Tab;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import zone.kaz.alight_midi.sequencer.StepSequencerManager;

public class PadGroup {
    private final StepSequencerController controller;
    private final StepSequencerManager manager;

    public static final int PAD_NUM_WIDTH = 8;
    public static final int PAD_NUM_HEIGHT = 4;

    private PadButton[][] padButtons = new PadButton[PAD_NUM_WIDTH][PAD_NUM_HEIGHT];

    public PadGroup(StepSequencerController stepSequencerController, StepSequencerManager stepSequencerManager) {
        this.controller = stepSequencerController;
        this.manager = stepSequencerManager;
    }

    void createPadButtons(Tab tab) {
        AnchorPane pane = (AnchorPane) tab.getContent();
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
                    padButtons[j][i] = padButton;
                }
            }
        });
    }

    public PadButton getPadButton(int x, int y) {
        return padButtons[x][y];
    }

}