package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;

import java.util.HashMap;

@Singleton
public class LedDeviceManager {

    private HashMap<String, LedDevice> ledDevices = new HashMap<>();

    public LedDevice openDevice(String deviceKey, String hostname, int port) {
        LedDevice device = new LedDevice(hostname, port);
        ledDevices.put(deviceKey, device);
        return device;
    }

    public LedDevice getDevice(String key) {
        if (ledDevices.containsKey(key)) {
            return ledDevices.get(key);
        }
        return null;
    }

    public void closeDevice(String key) {
        if (ledDevices.containsKey(key)) {
            ledDevices.get(key).close();
            ledDevices.remove(key);
        }
    }

    public void closeAllDevices() {
        ledDevices.values().forEach(LedDevice::close);
    }

}
