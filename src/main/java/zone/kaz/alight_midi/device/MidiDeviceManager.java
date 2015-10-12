package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;
import com.sun.media.sound.MidiInDeviceProvider;
import com.sun.media.sound.MidiOutDeviceProvider;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;

@Singleton
public class MidiDeviceManager {

    private static final Class MIDI_IN = MidiInDeviceProvider.class;
    private static final Class MIDI_OUT = MidiOutDeviceProvider.class;

    private ArrayList<MidiDevice.Info> inputDevices = new ArrayList<>();
    private ArrayList<MidiDevice.Info> outputDevices = new ArrayList<>();
    private HashMap<Integer, MidiDevicePair> enabledDevices = new HashMap<>();

    public MidiDeviceManager() {}

    public void reloadDevices() {
        ArrayList<MidiDevice.Info> inputDevices = new ArrayList<>();
        ArrayList<MidiDevice.Info> outputDevices = new ArrayList<>();
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info device : devices) {
            if (device.getClass().getEnclosingClass().equals(MIDI_IN)) {
                if (!inputDevices.contains(device)) {
                    inputDevices.add(device);
                }
            } else if (device.getClass().getEnclosingClass().equals(MIDI_OUT)) {
                if (!outputDevices.contains(device)) {
                    outputDevices.add(device);
                }
            }
        }
        this.inputDevices = inputDevices;
        this.outputDevices = outputDevices;
    }

    public ArrayList<MidiDevice.Info> getInputDevices() {
        return inputDevices;
    }

    public ArrayList<MidiDevice.Info> getOutputDevices() {
        return outputDevices;
    }

    public MidiDevicePair getEnabledDevice(int index) {
        return enabledDevices.get(index);
    }

    public void registerDevice(int index, MidiDevice.Info inputDeviceInfo, MidiDevice.Info outputDeviceInfo) {
        MidiDevicePair devicePair;
        if (enabledDevices.containsKey(index)) {
            devicePair = enabledDevices.get(index);
        } else {
            devicePair = new MidiDevicePair();
        }
        try {
            devicePair.registerInputDevice(inputDeviceInfo);
            devicePair.registerOutputDevice(outputDeviceInfo);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        enabledDevices.put(index, devicePair);
    }

    public void finish() {
        enabledDevices.values().forEach(MidiDevicePair::finish);
    }

}
