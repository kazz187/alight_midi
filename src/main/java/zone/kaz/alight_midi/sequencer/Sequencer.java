package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.DeviceBufferManager;
import zone.kaz.alight_midi.device.MixerManager;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.inject.DIContainer;
import zone.kaz.alight_midi.sequencer.animation.AllFlash;
import zone.kaz.alight_midi.sequencer.animation.Wave;

public class Sequencer {

    private int baseTick;
    private int baseBeats;
    private int baseParts;
    private int beats = 0;
    private long tick = 0;
    private long nextTick = 0;
    private SequenceDisplayManager sequenceDisplayManager = DIContainer.get(SequenceDisplayManager.class);
    private AnimationManager animationManager = DIContainer.get(AnimationManager.class);
    private DeviceBufferManager deviceBufferManager = DIContainer.get(DeviceBufferManager.class);
    private MixerManager mixerManager = DIContainer.get(MixerManager.class);

    public Sequencer(int baseTick, int baseBeats, int baseParts) {
        this.baseTick = baseTick;
        this.baseBeats = baseBeats;
        this.baseParts = baseParts;
        reset();
    }

    public void reset() {
        tick = 0;
        nextTick = 0;
        beats = 0;
        sequenceDisplayManager.setNumber(0);
        animationManager.removeAll();
    }

    public void setTick(long tick) {
        while (tick > nextTick) {
            animationManager.register(
//                    new AllFlash(
                    new Wave(
                            tick, 120,
                            deviceBufferManager.createDeviceBuffer("stripe_test")
                    ), MixerChannel.CHANNEL1);
            sequenceDisplayManager.setNumber(getBeats());
            beats++;
            nextTick += baseTick * baseParts / 4;
        }
        animationManager.setTick(tick);
        mixerManager.run();
    }

    public long getTick() {
        return tick;
    }

    public int getBeats() {
        return beats % baseBeats;
    }

    public int getParts() {
        return beats / baseBeats;
    }

}
