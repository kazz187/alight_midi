package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;
//import com.sun.media.sound.MidiInDeviceProvider;
//import com.sun.media.sound.MidiOutDeviceProvider;
import zone.kaz.alight_midi.device.midi.EnabledMidiDevice;
import zone.kaz.alight_midi.device.midi.MidiDeviceFactory;
import zone.kaz.alight_midi.device.midi.MidiDevicePair;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;

@Singleton
public class MidiDeviceManager {

//    private static final Class MIDI_IN = MidiInDeviceProvider.class;
//    private static final Class MIDI_OUT = MidiOutDeviceProvider.class;

    private ArrayList<MidiDevice.Info> inputDevices = new ArrayList<>();
    private ArrayList<MidiDevice.Info> outputDevices = new ArrayList<>();
    private HashMap<Integer, MidiDevicePair> enabledDevicePairs = new HashMap<>();
    private MidiDeviceFactory factory = new MidiDeviceFactory();

    public MidiDeviceManager() {}

    public void reloadDevices() {
        ArrayList<MidiDevice.Info> inputDevices = new ArrayList<>();
        ArrayList<MidiDevice.Info> outputDevices = new ArrayList<>();
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
/*        for (MidiDevice.Info device : devices) {
            if (device.getClass().getEnclosingClass().equals(MIDI_IN)) {
                if (!inputDevices.contains(device)) {
                    inputDevices.add(device);
                }
            } else if (device.getClass().getEnclosingClass().equals(MIDI_OUT)) {
                if (!outputDevices.contains(device)) {
                    outputDevices.add(device);
                }
            }
        }*/
        this.inputDevices = inputDevices;
        this.outputDevices = outputDevices;
    }

    public ArrayList<MidiDevice.Info> getInputDevices() {
        return inputDevices;
    }

    public ArrayList<MidiDevice.Info> getOutputDevices() {
        return outputDevices;
    }

    public MidiDevicePair getEnabledDevicePair(int index) {
        return enabledDevicePairs.get(index);
    }

    public void registerDevice(int index, MidiDevice.Info inputDeviceInfo, MidiDevice.Info outputDeviceInfo) {
        MidiDevicePair devicePair;
        if (enabledDevicePairs.containsKey(index)) {
            devicePair = enabledDevicePairs.get(index);
        } else {
            devicePair = new MidiDevicePair();
        }
        try {
            EnabledMidiDevice inputDevice = null;
            if (inputDeviceInfo != null) {
                inputDevice = factory.getDevice(inputDeviceInfo);
            }
            EnabledMidiDevice outputDevice = null;
            if (outputDeviceInfo != null) {
                outputDevice = factory.getDevice(outputDeviceInfo);
            }
            devicePair.registerInputDevice(inputDevice);
            devicePair.registerOutputDevice(outputDevice);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        enabledDevicePairs.put(index, devicePair);
    }

    public void finish() {
        enabledDevicePairs.values().forEach(MidiDevicePair::finish);
    }

}
