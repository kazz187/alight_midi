package zone.kaz.alight_midi.device.midi;

import javafx.application.Platform;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.main.MainController;
import zone.kaz.alight_midi.gui.preferences.PreferencesController;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class MidiControllerMapping {

    private HashMap<Byte, Processor> mapping_on = new HashMap<>();
    private HashMap<Byte, Processor> mapping_off = new HashMap<>();
    private ClockManager clockManager = DIContainer.get(ClockManager.class);
    private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
    private StepSequencerController controller = null;
    private HashMap<String, Processor> processors = new HashMap<>();

    public MidiControllerMapping() {
        prepareProcessors();
/*        mapping_on.put((byte) 89, v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMax);
        });
        mapping_on.put((byte) 79, v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMin);
        });
        mapping_on.put((byte) 19, v -> clockManager.stopSequencer());
        mapping_on.put((byte) 29, v -> clockManager.setNeedPlay(true));
        mapping_on.put((byte) 39, v -> clockManager.onNudgePressed(-1));
        mapping_on.put((byte) 49, v -> clockManager.onNudgePressed(1));
        mapping_off.put((byte) 39, v -> clockManager.onNudgeReleased());
        mapping_off.put((byte) 49, v -> clockManager.onNudgeReleased());
        mapping_on.put((byte) 59, v -> clockManager.tapBpm());
        int x = 0, y = 0;
        for (int i = 8; i >= 5 ; i--) {
            for (int j = 1; j <= 8; j++) {
                final int finalX = x, finalY = y;
                mapping_on.put((byte) (i * 10 + j), v -> {
                    cacheStepSequencerController().onPadPressed(finalX, finalY);
                });
                mapping_off.put((byte) (i * 10 + j), v -> {
                    cacheStepSequencerController().onPadReleased(finalX, finalY);
                });
                x++;
            }
            y++;
            x = 0;
        }*/
    }

    private void prepareProcessors() {
        processors.put("MASTER_FADER_TO_MAX", v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMax);
        });
        processors.put("MASTER_FADER_TO_MIN", v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMin);
        });
        processors.put("STOP", v -> clockManager.stopSequencer());
        processors.put("START", v -> clockManager.setNeedPlay(true));
        processors.put("NUDGE_BACKWARD_ON", v -> clockManager.onNudgePressed(-1));
        processors.put("NUDGE_FORWARD_ON", v -> clockManager.onNudgePressed(1));
        processors.put("NUDGE_BACKWARD_OFF", v -> clockManager.onNudgeReleased());
        processors.put("NUDGE_FORWARD_OFF", v -> clockManager.onNudgeReleased());
        processors.put("TAP_BPM", v -> clockManager.tapBpm());
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                final int finalX = x, finalY = y;
                processors.put("PAD_" + x + "_" + y + "_ON",
                        v -> cacheStepSequencerController().onPadPressed(finalX, finalY));
                processors.put("PAD_" + x + "_" + y + "_OFF",
                        v -> cacheStepSequencerController().onPadReleased(finalX, finalY));
            }
        }
        // TODO: Implement dynamic mapping
/*        Set<MappingData> mappingDataSet = processors.keySet()
                .stream()
                .map(mappingName -> new MappingData(mappingName, new MidiData()))
                .collect(Collectors.toSet());
        PreferencesController controller = (PreferencesController) controllerManager.get(PreferencesController.class);
        if (controller != null) {
            controller.setMappingData(mappingDataSet);
        }
        */
    }

    private StepSequencerController cacheStepSequencerController() {
        if (controller == null) {
            controller = (StepSequencerController) controllerManager.get(StepSequencerController.class);
        }
        return controller;
    }

    public Set<String> getProcessorNameSet() {
        return processors.keySet();
    }

    public void invoke(int event, byte note, byte velocity) {
        HashMap<Byte, Processor> mapping;
        switch (event) {
            case ShortMessage.NOTE_ON:
                mapping = mapping_on;
                break;
            case ShortMessage.NOTE_OFF:
                mapping = mapping_off;
                break;
            default:
                return;
        }
        Processor processor = mapping.get(note);
        if (processor == null) {
            return;
        }
        processor.run(velocity);
    }

    private interface Processor {
        void run(byte velocity);
    }

}
