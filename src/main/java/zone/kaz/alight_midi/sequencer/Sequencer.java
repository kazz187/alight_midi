package zone.kaz.alight_midi.sequencer;

import zone.kaz.alight_midi.device.DeviceBufferManager;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.inject.DIContainer;

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
            animationManager.register(new AllFlashAnimation(tick, 240, deviceBufferManager.createDeviceBuffer("stripe_test")));
            sequenceDisplayManager.setNumber(getBeats());
            beats++;
            nextTick += baseTick * baseParts / 4;
        }
        animationManager.setTick(tick);
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