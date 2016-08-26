package zone.kaz.alight_midi.device.midi;

import javafx.application.Platform;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.main.MainController;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.Set;

public class MidiControllerMapping {

    private HashMap<String, Processor> mappingOn = new HashMap<>();
    private HashMap<String, Processor> mappingOff = new HashMap<>();
    private ClockManager clockManager = DIContainer.get(ClockManager.class);
    private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
    private StepSequencerController controller = null;
    private HashMap<String, Processor> processors = new HashMap<>();

    public MidiControllerMapping() {
        prepareProcessors();
/*        mappingOn.put((byte) 89, v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMax);
        });
        mappingOn.put((byte) 79, v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMin);
        });
        mappingOn.put((byte) 19, v -> clockManager.stopSequencer());
        mappingOn.put((byte) 29, v -> clockManager.setNeedPlay(true));
        mappingOn.put((byte) 39, v -> clockManager.onNudgePressed(-1));
        mappingOn.put((byte) 49, v -> clockManager.onNudgePressed(1));
        mappingOff.put((byte) 39, v -> clockManager.onNudgeReleased());
        mappingOff.put((byte) 49, v -> clockManager.onNudgeReleased());
        mappingOn.put((byte) 59, v -> clockManager.tapBpm());
        int x = 0, y = 0;
        for (int i = 8; i >= 5 ; i--) {
            for (int j = 1; j <= 8; j++) {
                final int finalX = x, finalY = y;
                mappingOn.put((byte) (i * 10 + j), v -> {
                    cacheStepSequencerController().onPadPressed(finalX, finalY);
                });
                mappingOff.put((byte) (i * 10 + j), v -> {
                    cacheStepSequencerController().onPadReleased(finalX, finalY);
                });
                x++;
            }
            y++;
            x = 0;
        }*/
    }

    private void prepareProcessors() {
        processors.put("MASTER_FADER_TO_MAX_ON", v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMax);
        });
        processors.put("MASTER_FADER_TO_MIN_ON", v -> {
            MainController mainController = (MainController) controllerManager.get(MainController.class);
            Platform.runLater(mainController::setMasterFaderToMin);
        });
        processors.put("STOP_ON", v -> clockManager.stopSequencer());
        processors.put("START_ON", v -> clockManager.setNeedPlay(true));
        processors.put("NUDGE_BACKWARD_ON", v -> clockManager.onNudgePressed(-1));
        processors.put("NUDGE_FORWARD_ON", v -> clockManager.onNudgePressed(1));
        processors.put("NUDGE_BACKWARD_OFF", v -> clockManager.onNudgeReleased());
        processors.put("NUDGE_FORWARD_OFF", v -> clockManager.onNudgeReleased());
        processors.put("TAP_BPM_ON", v -> clockManager.tapBpm());
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                final int finalX = x, finalY = y;
                processors.put("PAD_" + x + "_" + y + "_ON",
                        v -> cacheStepSequencerController().onPadPressed(finalX, finalY));
                processors.put("PAD_" + x + "_" + y + "_OFF",
                        v -> cacheStepSequencerController().onPadReleased(finalX, finalY));
            }
        }
    }

    private StepSequencerController cacheStepSequencerController() {
        if (controller == null) {
            controller = (StepSequencerController) controllerManager.get(StepSequencerController.class);
        }
        return controller;
    }

    public void setMappingData(MappingData mappingData) {
        setMappingDataImpl(mappingData.getProcessorName() + "_ON", mappingData.getPressedMidiData());
        setMappingDataImpl(mappingData.getProcessorName() + "_OFF", mappingData.getReleasedMidiData());
    }

    private void setMappingDataImpl(String processorName, MidiData midiData) {
        switch (midiData.getType()) {
            case ShortMessage.NOTE_ON:
                mappingOn.put(midiData.getNote() + "_" + midiData.getVelocity(), processors.get(processorName));
                break;
            case ShortMessage.NOTE_OFF:
                mappingOff.put(midiData.getNote() + "_" + midiData.getVelocity(), processors.get(processorName));
                break;
            default: break;
        }
    }

    public Set<String> getProcessorNameSet() {
        return processors.keySet();
    }

    public void invoke(int event, byte note, byte velocity) {
        HashMap<String, Processor> mapping;
        switch (event) {
            case ShortMessage.NOTE_ON:
                mapping = velocity == 0 ? mappingOff : mappingOn;
                break;
            case ShortMessage.NOTE_OFF:
                mapping = mappingOff;
                break;
            default:
                return;
        }
        Processor processor = mapping.get(note + "_" + velocity);
        if (processor == null) {
            return;
        }
        processor.run(velocity);
    }

    private interface Processor {
        void run(byte velocity);
    }

}
