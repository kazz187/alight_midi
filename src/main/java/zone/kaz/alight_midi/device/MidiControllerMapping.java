package zone.kaz.alight_midi.device;

import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.ClockManager;

import java.util.HashMap;

public class MidiControllerMapping {

    private HashMap<Byte, Processor> mapping = new HashMap<>();
    private ClockManager clockManager = DIContainer.get(ClockManager.class);

    public MidiControllerMapping() {
        mapping.put((byte) 19, (v) ->{
            clockManager.stopSequencer();
        });
        mapping.put((byte) 29, (v) ->{
            clockManager.playSequencer();
        });
    }

    public void invoke(byte note, byte velocity) {
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
