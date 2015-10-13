package zone.kaz.alight_midi.device;

import javax.sound.midi.*;
import java.util.HashMap;

public class MidiDevicePair {

    private enum Type {
        INPUT, OUTPUT;
    }
    private HashMap<Type, MidiDevice> devices = new HashMap<>();
    private Receiver receiver;

    public MidiDevice getInputDevice() {
        if (devices.containsKey(Type.INPUT)) {
            return devices.get(Type.INPUT);
        }
        return null;
    }

    public MidiDevice getOutputDevice() {
        if (devices.containsKey(Type.OUTPUT)) {
            return devices.get(Type.OUTPUT);
        }
        return null;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void registerInputDevice(MidiDevice.Info deviceInfo) throws MidiUnavailableException {
        if (deviceInfo == null) {
            return;
        }
        if (registerDevice(Type.INPUT, deviceInfo)) {
            createReceiver();
        }
    }

    public void registerOutputDevice(MidiDevice.Info deviceInfo) throws MidiUnavailableException {
        if (deviceInfo == null) {
            return;
        }
        if (registerDevice(Type.OUTPUT, deviceInfo)) {
            createReceiver();
        }
    }

    public void finish() {
        if (receiver != null) {
            receiver.close();
        }
        finish(Type.INPUT);
        finish(Type.OUTPUT);
    }

    private void finish(Type type) {
        MidiDevice device = devices.get(type);
        if (device != null) {
            device.close();
            devices.remove(type);
        }
    }

    private boolean registerDevice(Type type, MidiDevice.Info deviceInfo) throws MidiUnavailableException {
        if (deviceInfo == null) {
            return false;
        }
        MidiDevice device;
        boolean ret = false;
        if (devices.containsKey(type)) {
            MidiDevice currentDevice = devices.get(type);
            if (currentDevice.getDeviceInfo().equals(deviceInfo)) {
                device = currentDevice;
            } else {
                currentDevice.close();
                receiver.close();
                device = MidiSystem.getMidiDevice(deviceInfo);
                ret = true;
            }
        } else {
            device = MidiSystem.getMidiDevice(deviceInfo);
            ret = true;
        }
        if (!device.isOpen()) {
            device.open();
            System.out.println("Connect to " + deviceInfo);
        }
        devices.put(type, device);
        return ret;
    }

    private void createReceiver() {
        MidiDevice inputDevice = getInputDevice();
        MidiDevice outputDevice = getOutputDevice();
        ControllerReceiver controllerReceiver;
        try {
            if (outputDevice != null) {
                Receiver receiver = outputDevice.getReceiver();
                controllerReceiver = new ControllerReceiver(receiver);
            } else {
                controllerReceiver = new ControllerReceiver(null);
            }
            if (inputDevice != null) {
                Transmitter transmitter = inputDevice.getTransmitter();
                transmitter.setReceiver(controllerReceiver);
            }
            this.receiver = controllerReceiver;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    private class ControllerReceiver implements Receiver {

        private Receiver receiver;

        public ControllerReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (receiver == null) {
                return;
            }
            receiver.send(message, timeStamp);
        }

        @Override
        public void close() {
            if (receiver != null) {
                receiver.close();
            }
        }

    }

}
