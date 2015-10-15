package zone.kaz.alight_midi.device;

import javax.sound.midi.MidiDevice;
import java.util.HashMap;

public class MidiDeviceFactory {

    private HashMap<MidiDevice.Info, EnabledMidiDevice> devices = new HashMap<>();

    public EnabledMidiDevice getDevice(MidiDevice.Info deviceInfo) {
        if (devices.containsKey(deviceInfo)) {
            return devices.get(deviceInfo);
        }
        return new EnabledMidiDevice(deviceInfo);
    }

    public void close() {
        devices.values().forEach(EnabledMidiDevice::close);
    }

}