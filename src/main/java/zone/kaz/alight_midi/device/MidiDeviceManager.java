package zone.kaz.alight_midi.device;

import javax.sound.midi.*;
import java.util.HashMap;

/**
 * Created by kazz on 2015/09/30.
 */
public class MidiDeviceManager {

    private static MidiDeviceManager midiDeviceManager = new MidiDeviceManager();

    private HashMap<MidiDevice.Info, MidiDevice> inputDevices = new HashMap<>();
    private HashMap<MidiDevice.Info, MidiDevice> outputDevices = new HashMap<>();

    private MidiDeviceManager() {}

    public static MidiDeviceManager getInstance() {
        return midiDeviceManager;
    }

    public MidiDevice getInputMidiDevice(int index) {
        return getMidiDevice(index, inputDevices);
    }

    public MidiDevice getOutputMidiDevice(int index) {
        return getMidiDevice(index, outputDevices);
    }

    private MidiDevice getMidiDevice(int index, HashMap<MidiDevice.Info, MidiDevice> devices) {
        if (devices.values().size() > index) {
            int i = 0;
            for (MidiDevice inputDevice : devices.values()) {
                if (i++ == index) {
                    return inputDevice;
                }
            }
        }
        return null;
    }

    public void registerInputDevice(MidiDevice.Info deviceInfo) {
        final MidiDevice device = registerDevice(deviceInfo, inputDevices);
        if (device == null) return;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Transmitter transmitter = device.getTransmitter();
                    transmitter.setReceiver(new Receiver() {
                        MidiDevice sendDevice = null;
                        @Override
                        public void send(MidiMessage message, long timeStamp) {
                            if (sendDevice == null) {
                                sendDevice = getOutputMidiDevice(0);
                            }
                            try {
                                Receiver receiver = sendDevice.getReceiver();
                                receiver.send(message, timeStamp);
                            } catch (MidiUnavailableException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void close() {

                        }
                    });
                } catch (MidiUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void registerOutputDevice(MidiDevice.Info deviceInfo) {
        final MidiDevice device = registerDevice(deviceInfo, outputDevices);
    }

    private MidiDevice registerDevice(MidiDevice.Info deviceInfo, HashMap<MidiDevice.Info, MidiDevice> devices) {
        MidiDevice device;
        if (devices.containsKey(deviceInfo)) {
            device = devices.get(deviceInfo);
        } else {
            try {
                device = MidiSystem.getMidiDevice(deviceInfo);
                devices.put(deviceInfo, device);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
                return null;
            }
        }
        if (!device.isOpen()) {
            try {
                device.open();
                System.out.println("Connect to " + deviceInfo);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        return device;
    }

    public void finish() {
        for (MidiDevice device : inputDevices.values()) {
            System.out.println("input close");
            device.close();
        }
        for (MidiDevice device : outputDevices.values()) {
            System.out.println("output close");
            device.close();
        }
    }

}
