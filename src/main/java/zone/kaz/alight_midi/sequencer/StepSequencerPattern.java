package zone.kaz.alight_midi.sequencer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import zone.kaz.alight_midi.gui.sequencer.StepSequencer;

import java.util.ArrayList;

public class StepSequencerPattern {

    private int rate = 0;
    private int clock = 0;
    private int beats = 4;
    private ArrayList<StepSequencer> stepSequencerList = new ArrayList<>();

    public void add(StepSequencer stepSequencer) {
        stepSequencerList.add(stepSequencer);
    }

    public void remove() {
        int index = stepSequencerList.size() - 1;
        if (index < 0) {
            return;
        }
        StepSequencer sequencer = stepSequencerList.get(index);
        sequencer.finish();
        stepSequencerList.remove(sequencer);
    }

    @JsonIgnore
    public int getSize() {
        return stepSequencerList.size();
    }

    @JsonIgnore
    public double getCalcRate() {
        return Math.pow(2, rate);
    }

    @JsonProperty("rate")
    public int getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(int rate) {
        this.rate = rate;
    }

    @JsonIgnore
    public int getCalcClock() {
        return (int) Math.pow(2, clock) * beats;
    }

    @JsonProperty("clock")
    public void setClock(int clock) {
        this.clock = clock;
        int calcClock = getCalcClock();
        int beats = getBeats();
        for (StepSequencer stepSequencer : stepSequencerList) {
            stepSequencer.setClock(calcClock, beats);
        }
    }

    @JsonProperty("clock")
    public int getClock() {
        return clock;
    }

    @JsonProperty("beats")
    public int getBeats() {
        return beats;
    }

    @JsonProperty("beats")
    public void setBeats(int beats) {
        this.beats = beats;
    }

    public void setButtonWidth(double buttonWidth) {
        for (StepSequencer stepSequencer : stepSequencerList) {
            stepSequencer.setButtonWidth(buttonWidth);
        }
    }

    @JsonProperty("stepSequencerList")
    public ArrayList<StepSequencer> getStepSequencerList() {
        return stepSequencerList;
    }

    @JsonProperty("stepSequencerList")
    public void setStepSequencerList(ArrayList<StepSequencer> stepSequencerList) {
        this.stepSequencerList = stepSequencerList;
    }

}
