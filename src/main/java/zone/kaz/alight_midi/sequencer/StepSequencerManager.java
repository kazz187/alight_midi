package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;

@Singleton
public class StepSequencerManager {

    private StepSequencerPattern pattern = null;

    public StepSequencerManager() {
        initPattern();
    }

    private void initPattern() {
        pattern = new StepSequencerPattern();
    }

    public StepSequencerPattern getPattern() {
        return pattern;
    }

}
