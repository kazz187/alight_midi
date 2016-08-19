package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.led.DeviceInfo;

import java.util.HashMap;

@Singleton
public class LedDeviceManager {

    private HashMap<String, LedDevice> ledDevices = new HashMap<>();

    public LedDevice openDevice(DeviceInfo deviceInfo) {
        LedDevice device = new LedDevice(deviceInfo.getHostname(), deviceInfo.getPort());
        ledDevices.put(deviceInfo.getName(), device);
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
    public void openAllDevices() {
        ledDevices.values().forEach(LedDevice::connect);
    }

    public void closeAllDevices() {
        ledDevices.values().forEach(LedDevice::close);
    }

}
