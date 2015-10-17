package zone.kaz.alight_midi.device.led;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfo {

    private String name;
    private int[][] mapping;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("mapping")
    public int[][] getMapping() {
        return mapping;
    }

    @JsonProperty("mapping")
    public void setMapping(int[][] mapping) {
        this.mapping = mapping;
    }

}
