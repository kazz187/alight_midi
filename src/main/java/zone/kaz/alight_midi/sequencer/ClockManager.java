package zone.kaz.alight_midi.sequencer;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.MidiDeviceManager;
import zone.kaz.alight_midi.device.MidiDevicePair;
import zone.kaz.alight_midi.inject.DIContainer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import static javax.sound.midi.ShortMessage.*;

@Singleton
public class ClockManager extends Thread {

    private boolean isPlaying = false;
    private double bpm = 135.0;

    public ClockManager() {}

    public void playSequencer() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        interrupt();
    }

    public void stopSequencer() {
        isPlaying = false;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    @Override
    public void run() {
        long i = 0;
        MidiDeviceManager deviceManager = DIContainer.getInjector().getInstance(MidiDeviceManager.class);
        while (true) {
            if (isPlaying) {
                MidiDevicePair devicePair = deviceManager.getEnabledDevice(0);
                try {
                    devicePair.getReceiver().send(new ShortMessage(NOTE_ON, 0, (int) (11 + i % 4), (int) (i % 128)), 0);
                    devicePair.getReceiver().send(new ShortMessage(NOTE_OFF, 0, (int) (11 + (i-1) % 4), (int) (i % 128)), 0);
                    System.out.println(i%128);
                } catch (InvalidMidiDataException e) {
                    e.printStackTrace();
                }
                i++;
                try {
                    int delay = (int) (60 * 1000 / bpm);
                    Thread.sleep(delay);
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
