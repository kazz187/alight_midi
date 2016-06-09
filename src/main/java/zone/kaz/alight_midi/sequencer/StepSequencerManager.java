package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;

import java.util.concurrent.Semaphore;

@Singleton
public class StepSequencerManager {

    private StepSequencerPattern pattern = null;
    private Semaphore stepSequencerSemaphore = new Semaphore(1);

    public StepSequencerManager() {
        initPattern();
    }

    private void initPattern() {
        pattern = new StepSequencerPattern();
    }

    public StepSequencerPattern getPattern() {
        return pattern;
    }

    public void setPattern(StepSequencerPattern pattern) {
        this.pattern = pattern;
    }

    public Semaphore getStepSequencerSemaphore() {
        return stepSequencerSemaphore;
    }

}
