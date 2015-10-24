package zone.kaz.alight_midi.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import zone.kaz.alight_midi.device.led.DeviceBuffer;
import zone.kaz.alight_midi.device.led.DeviceInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class DeviceBufferManager {

    private HashMap<String, DeviceInfo> deviceInfoList = new HashMap<>();

    public DeviceInfo registerDeviceInfo(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = Files.readAllLines(Paths.get(fileName)).stream().collect(Collectors.joining());
        DeviceInfo deviceInfo = objectMapper.readValue(json, DeviceInfo.class);
        deviceInfoList.put(deviceInfo.getName(), deviceInfo);
        return deviceInfo;
    }

    public DeviceBuffer createDeviceBuffer(String name) {
        if (deviceInfoList.containsKey(name)) {
            return new DeviceBuffer(deviceInfoList.get(name));
        }
        return null;
    }

    public Set<String> getDeviceNames() {
        return deviceInfoList.keySet();
    }

}
