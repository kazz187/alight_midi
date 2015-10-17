package zone.kaz.alight_midi.device.midi;

import javax.sound.midi.*;

public class EnabledMidiDevice {

    private MidiDevice device;
    private Receiver receiver;
    private Transmitter transmitter;

    public EnabledMidiDevice(MidiDevice.Info deviceInfo) {
        try {
            device = MidiSystem.getMidiDevice(deviceInfo);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public MidiDevice getDevice() {
        return device;
    }

    public Receiver getReceiver() {
        if (receiver == null) {
            try {
                receiver = device.getReceiver();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        return receiver;
    }

    public Transmitter getTransmitter() {
        if (transmitter == null) {
            try {
                transmitter = device.getTransmitter();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        return transmitter;
    }

    public void close() {
        if (receiver != null) {
            receiver.close();
        }
        if (transmitter != null) {
            transmitter.close();
        }
        device.close();
    }

}
