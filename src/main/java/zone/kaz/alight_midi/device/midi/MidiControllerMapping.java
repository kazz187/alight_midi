package zone.kaz.alight_midi.device.midi;

import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.sequencer.StepSequencerController;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

import javax.sound.midi.ShortMessage;
import java.util.HashMap;

public class MidiControllerMapping {

    private HashMap<Byte, Processor> mapping_on = new HashMap<>();
    private HashMap<Byte, Processor> mapping_off = new HashMap<>();
    private ClockManager clockManager = DIContainer.get(ClockManager.class);
    private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);
    private StepSequencerController controller = null;

    public MidiControllerMapping() {
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
        }
    }

    private StepSequencerController cacheStepSequencerController() {
        if (controller == null) {
            controller = (StepSequencerController) controllerManager.get(StepSequencerController.class);
        }
        return controller;
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
