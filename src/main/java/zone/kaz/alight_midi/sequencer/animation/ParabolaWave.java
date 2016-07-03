package zone.kaz.alight_midi.sequencer.animation;

public class ParabolaWave extends Wave {

    public ParabolaWave() {
        super();
    }

    @Override
    protected double calcPosRate(long tick) {
        long pos = tick - startTick;
        double linearPosRate = 1 - (double) pos / tickSize;
        double startX = waveParams.getStartX();
        double maxY = startX * startX;
        double currentX = startX * linearPosRate;
        double currentY = currentX * currentX;
        double posRate = currentY / maxY;
        if (!waveParams.getReverse()) {
            posRate = 1 - posRate;
        }
        return posRate;
    }

}
