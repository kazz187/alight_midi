package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.DeviceBufferManager;
import zone.kaz.alight_midi.device.MixerManager;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.gui.sequencer.AnimationInfo;
import zone.kaz.alight_midi.gui.sequencer.SequencerInfo;
import zone.kaz.alight_midi.gui.sequencer.StepSequencer;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.animation.Wave;

import java.util.ArrayList;

public class Sequencer {

    private int baseTick;
    private int baseBeats;
    private int baseParts;
    private int beats = 0;
    private long nextBeatTick = 0;
    private long prevTick = 0;
    private SequenceDisplayManager sequenceDisplayManager = DIContainer.get(SequenceDisplayManager.class);
    private AnimationManager animationManager = DIContainer.get(AnimationManager.class);
    private DeviceBufferManager deviceBufferManager = DIContainer.get(DeviceBufferManager.class);
    private MixerManager mixerManager = DIContainer.get(MixerManager.class);
    private StepSequencerManager stepSequencerManager = DIContainer.get(StepSequencerManager.class);

    public Sequencer(int baseTick, int baseBeats, int baseParts) {
        this.baseTick = baseTick;
        this.baseBeats = baseBeats;
        this.baseParts = baseParts;
        reset();
    }

    public void reset() {
        nextBeatTick = 0;
        prevTick = 0;
        beats = 0;
        sequenceDisplayManager.setNumber(0);
        animationManager.removeAll();
    }

    public void setTick(long tick) {
        double rate = stepSequencerManager.getRate();
        ArrayList<StepSequencer> stepSequencerList = stepSequencerManager.getStepSequencerList();
        for (StepSequencer stepSequencer : stepSequencerList) {
            Boolean[] boolArray = stepSequencer.getBoolArray();
            int size = boolArray.length;
            int ratedTick = (int) (baseTick / 4 * rate);
            int loopTick = ratedTick * size;
            int currentSeqTick = (int) (tick % loopTick);
            int prevSeqTick = (int) (prevTick % loopTick);
            if (prevSeqTick > currentSeqTick) {
                prevSeqTick -= loopTick;
            }
            int checkStepNum = currentSeqTick / ratedTick;
            if ((tick == 0 || prevSeqTick < checkStepNum * ratedTick)
                    && checkStepNum <= currentSeqTick
                    && boolArray[checkStepNum]) {
                StepSequencer.Type type = stepSequencer.getType();
                switch (type) {
                    case ANIMATION:
                        AnimationInfo info = (AnimationInfo) stepSequencer.getSequencerInfo();
                        Animation animation = info.createAnimation(
                                tick, ratedTick,
                                deviceBufferManager.createDeviceBuffer("stripe_test")
                        );
                        animationManager.register(animation, MixerChannel.CHANNEL1);
                        break;
                    default:
                        break;
                }

            }
        }

        while (tick > nextBeatTick) {
            sequenceDisplayManager.setNumber(getBeats());
            beats++;
            nextBeatTick += baseTick * baseParts / 4;
        }
        animationManager.setTick(tick);
        mixerManager.run();
        prevTick = tick;
    }

    public int getBeats() {
        return beats % baseBeats;
    }

    public int getParts() {
        return beats / baseBeats;
    }

}
