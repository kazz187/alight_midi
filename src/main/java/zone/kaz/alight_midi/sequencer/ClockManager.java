package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.inject.DIContainer;

import java.time.Duration;
import java.time.LocalDateTime;

@Singleton
public class ClockManager extends Thread {

    private boolean isPlaying = false;
    private boolean isInit = false;
    private double bpm = 135.0;
    private LocalDateTime clock;
    private int clockInterval = 1; // ms

    public ClockManager() {}

    public void playSequencer() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        isInit = true;
        interrupt();
    }

    public void stopSequencer() {
        isPlaying = false;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public void initialize() {
        clock = LocalDateTime.now();
        isInit = false;
    }

    @Override
    public void run() {
        long clockCounter = 0;
        long playTime = 0;
        long i = 0;
        MidiDeviceManager deviceManager = DIContainer.getInjector().getInstance(MidiDeviceManager.class);
        SequenceDisplayManager displayManager = DIContainer.getInjector().getInstance(SequenceDisplayManager.class);
        while (true) {
            if (isPlaying) {
                if (isInit) {
                    initialize();
                }
                int bpmInterval = (int) (60 * 1000 / bpm);
                if (clockCounter * clockInterval > playTime) {
                    displayManager.setNumber((int) (i % 4));
                    i++;
                    playTime += bpmInterval;
                }
                clockCounter++;
                clock = clock.plusNanos(clockInterval * 1000000); // ns
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, clock);
                int realInterval = duration.getNano();
                if (duration.isNegative()) {
                    realInterval = 0;
                    System.err.println("minimized: " + now);
                }
                try {
                    Thread.sleep(realInterval / 1000000, realInterval % 1000000);
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
