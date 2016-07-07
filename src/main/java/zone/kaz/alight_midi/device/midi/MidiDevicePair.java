package zone.kaz.alight_midi.device.midi;

import javafx.application.Platform;
import zone.kaz.alight_midi.device.SequenceDisplayManager;
import zone.kaz.alight_midi.device.sequence_display.MidiSequenceDisplay;
import zone.kaz.alight_midi.gui.ControllerManager;
import zone.kaz.alight_midi.gui.preferences.PreferencesController;
import zone.kaz.alight_midi.inject.DIContainer;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static javax.sound.midi.ShortMessage.*;

public class MidiDevicePair {

    private enum Type {
        INPUT, OUTPUT;
    }
    private HashMap<Type, EnabledMidiDevice> devices = new HashMap<>();
    private MidiSequenceDisplay display = null;
    private ControllerReceiver controllerReceiver = null;
    private boolean mappingEditMode = false;
    private boolean mappingStart;
    private MidiData first, latest;

    public EnabledMidiDevice getInputDevice() {
        if (devices.containsKey(Type.INPUT)) {
            return devices.get(Type.INPUT);
        }
        return null;
    }

    public EnabledMidiDevice getOutputDevice() {
        if (devices.containsKey(Type.OUTPUT)) {
            return devices.get(Type.OUTPUT);
        }
        return null;
    }

    public Receiver getReceiver() {
        return controllerReceiver;
    }

    public void registerInputDevice(EnabledMidiDevice device) throws MidiUnavailableException {
        if (device == null) {
            return;
        }
        if (registerDevice(Type.INPUT, device)) {
            createReceiver();
        }
    }

    public void registerOutputDevice(EnabledMidiDevice device) throws MidiUnavailableException {
        if (device == null) {
            return;
        }
        if (registerDevice(Type.OUTPUT, device)) {
            createReceiver();
            SequenceDisplayManager manager = DIContainer.get(SequenceDisplayManager.class);
            ArrayList<Integer> noteList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                noteList.add(11 + i);
            }
            if (display != null) {
                manager.remove(display);
            }
            display = new MidiSequenceDisplay(this, noteList);
            manager.add(display);
        }
    }

    public void finish() {
        finish(Type.INPUT);
        finish(Type.OUTPUT);
        if (controllerReceiver != null) {
            controllerReceiver.close();
        }
    }

    private void finish(Type type) {
        EnabledMidiDevice device = devices.get(type);
        if (device != null) {
            device.close();
            devices.remove(type);
        }
    }

    private boolean registerDevice(Type type, EnabledMidiDevice device) throws MidiUnavailableException {
        if (device == null) {
            return false;
        }
        boolean ret = false;
        if (devices.containsKey(type)) {
            EnabledMidiDevice currentDevice = devices.get(type);
            if (!currentDevice.equals(device)) {
                currentDevice.close();
                ret = true;
            }
        } else {
            ret = true;
        }
        if (!device.getDevice().isOpen()) {
            device.getDevice().open();
            System.err.println("Connect to " + device.getDevice());
        }
        devices.put(type, device);
        return ret;
    }

    private void createReceiver() {
        EnabledMidiDevice inputDevice = getInputDevice();
        EnabledMidiDevice outputDevice = getOutputDevice();
        if (outputDevice != null) {
            Receiver receiver = outputDevice.getReceiver();
            if (controllerReceiver == null) {
                controllerReceiver = new ControllerReceiver(receiver);
            } else {
                controllerReceiver.setReceiver(receiver);
            }
        } else {
            if (controllerReceiver == null) {
                controllerReceiver = new ControllerReceiver(null);
            }
        }
        if (inputDevice != null) {
            Transmitter transmitter = inputDevice.getTransmitter();
            transmitter.setReceiver(controllerReceiver);
        }
    }

    public Set<String> getProcessorNameSet() {
        if (controllerReceiver != null) {
            return controllerReceiver.getProcessorNameSet();
        }
        return null;
    }

    public void setMappingEditMode(boolean mappinEeditMode) {
        this.mappingEditMode = mappinEeditMode;
    }

    public void setMappingStart(boolean mappingStart) {
        this.mappingStart = mappingStart;
    }

    private class ControllerReceiver implements Receiver {

        private Receiver receiver;
        private MidiControllerMapping mapping = new MidiControllerMapping();
        private ControllerManager controllerManager = DIContainer.get(ControllerManager.class);

        public ControllerReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            byte[] buf = message.getMessage();
            if (mappingEditMode) {
                MidiData midiData = null;
                switch (buf[0] & 0xF0) {
                    case NOTE_ON:
                        midiData = new MidiData(NOTE_ON, buf[1], buf[2]);
                        break;
                    case NOTE_OFF:
                        midiData = new MidiData(NOTE_OFF, buf[1], buf[2]);
                        break;
                    default:
                        break;
                }
                if (mappingStart) {
                    first = midiData;
                    mappingStart = false;
                } else {
                    latest = midiData;
                    Platform.runLater(()->{
                        PreferencesController preferencesController = (PreferencesController) controllerManager.get(PreferencesController.class);
                        preferencesController.saveMappingMidiData(first, latest);
                    });
                    mappingStart = true;
                }
            } else {
                switch (buf[0] & 0xF0) {
                    case NOTE_ON:
                        mapping.invoke(NOTE_ON, buf[1], buf[2]);
                        break;
                    case NOTE_OFF:
                        mapping.invoke(NOTE_OFF, buf[1], buf[2]);
                        break;
                    default:
                        break;
                }
                if (receiver == null) {
                    return;
                }
                receiver.send(message, timeStamp);
            }
        }

        public MidiData getFirstMidiData() {
            return first;
        }

        public MidiData getLatestMidiData() {
            return latest;
        }

        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        public Set<String> getProcessorNameSet() {
            return mapping.getProcessorNameSet();
        }

        @Override
        public void close() {
            if (receiver != null) {
                receiver.close();
            }
        }

    }

}
