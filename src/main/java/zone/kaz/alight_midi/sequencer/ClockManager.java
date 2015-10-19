package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.LedDeviceManager;
import zone.kaz.alight_midi.inject.DIContainer;

import java.time.Duration;
import java.time.LocalDateTime;

@Singleton
public class ClockManager extends Thread {

    LedDeviceManager ledDevicemanager = DIContainer.get(LedDeviceManager.class);

    private boolean isPlaying = false;
    private boolean isInit = false;
    private double bpm = 135.0;
    private double realBpm = 135.0;
    private double nudgeDir;
    private LocalDateTime clock;
    private int clockInterval = 3; // ms
    private long clockCounter = 0;
    private double playTime = 0;
    private double beforePlayTime = 0;
    private long tickCounter = 0;
    private int baseTick = 480;
    Sequencer sequencer = new Sequencer(baseTick, 4, 4);

    public ClockManager() {}

    public void playSequencer() {
        if (isPlaying) {
            resetSequencer();
            return;
        }
        isPlaying = true;
        isInit = true;
        interrupt();
    }

    public void stopSequencer() {
        if (!isPlaying) {
            resetSequencer();
        }
        isPlaying = false;
    }

    public void resetSequencer() {
        clockCounter = 0;
        playTime = beforePlayTime = 0;
        tickCounter = 0;
        nudgeDir = 0;
        sequencer.reset();
    }

    public void onNudgePressed(int nudgeDir) {
        if (nudgeDir != -1 && nudgeDir != 1) {
            return;
        }
        this.nudgeDir = nudgeDir;
    }

    public void onNudgeReleased() {
        nudgeDir = 0;
        realBpm = bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = realBpm = bpm;
    }

    public void initialize() {
        clock = LocalDateTime.now();
        isInit = false;
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void run() {
        resetSequencer();
        while (true) {
            if (isPlaying) {
                if (isInit) {
                    initialize();
                }

                if (nudgeDir != 0) {
                    double d = nudgeDir * 0.001;
                    realBpm += d;
                    if (realBpm <= 0 ) {
                        realBpm = 1;
                    }
                    double tickInterval = 60.0 * 1000 / realBpm / baseTick;
                    playTime = beforePlayTime + tickInterval;
                }

                while (clockCounter * clockInterval > playTime) {
                    sequencer.setTick(tickCounter);
                    tickCounter++;
                    beforePlayTime = playTime;
                    double tickInterval = 60.0 * 1000 / realBpm / baseTick;
                    playTime += tickInterval;
                }





                clock = clock.plus(Duration.ofMillis(clockInterval));
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, clock);
                clockCounter++;
                while (duration.isNegative()) {
                    duration = duration.plus(Duration.ofMillis(clockInterval));
                    clockCounter++;
                }
                long durationSecs = duration.getSeconds();
                long durationNs = duration.getNano();
                try {
                    Thread.sleep(durationSecs * 1000 + durationNs / 1000000,
                            (int) (durationNs % 1000000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // DO NOTHING
                }
            }
        }
    }

}
