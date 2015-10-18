package zone.kaz.alight_midi.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.DeviceInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

@Singleton
public class DeviceBufferManager {

    HashMap<String, DeviceInfo> deviceInfoList = new HashMap<>();

    public void registerDeviceInfo(String key, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = Files.readAllLines(Paths.get(fileName)).stream().collect(Collectors.joining());
            DeviceInfo deviceInfo = objectMapper.readValue(json, DeviceInfo.class);
            deviceInfoList.put(key, deviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DeviceBuffer createDeviceBuffer(String key) {
        if (deviceInfoList.containsKey(key)) {
            return new DeviceBuffer(key, deviceInfoList.get(key));
        }
        return null;
    }

}
