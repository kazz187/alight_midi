package zone.kaz.alight_midi.device.led;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfo {

    private String name;
    private int[][] mapping;
    private String hostname;
    private int port;

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

    @JsonProperty("hostname")
    public String getHostname() {
        return hostname;
    }

    @JsonProperty("hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @JsonProperty("port")
    public int getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(int port) {
        this.port = port;
    }

}
