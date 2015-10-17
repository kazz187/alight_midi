package zone.kaz.alight_midi.device;

import com.google.inject.Singleton;

import java.util.ArrayList;

@Singleton
public class LedDeviceManager {

    private ArrayList<LedDevice> ledDevices = new ArrayList<>();

    public LedDevice openDevice(String hostname, int port) {
        LedDevice device = new LedDevice(hostname, port);
        ledDevices.add(device);
        return device;
    }

    public LedDevice getDevice(int index) {
        if (ledDevices.size() > index) {
            return ledDevices.get(index);
        }
        return null;
    }

    public void closeDevice(int index) {
        if (ledDevices.size() > index) {
            ledDevices.get(index).close();
            ledDevices.remove(index);
        }
    }

    public void closeAllDevices() {
        ledDevices.forEach(LedDevice::close);
    }

    public ArrayList<LedDevice> getDevices() {
        return ledDevices;
    }

}
