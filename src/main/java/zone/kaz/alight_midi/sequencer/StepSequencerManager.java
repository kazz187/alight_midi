package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;

@Singleton
public class StepSequencerManager {

    private int rate = 1;
    private int clock = 0;
    private int beats = 4;

    public StepSequencerManager() {
    }

    public double getRate() {
        return Math.pow(2, rate);
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getClock() {
        return (int) Math.pow(2, clock) * 4;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getBeats() {
        return beats;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

}
