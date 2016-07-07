package zone.kaz.alight_midi.gui.preferences;

import zone.kaz.alight_midi.device.midi.MappingData;

import java.util.ArrayList;

public class MapSetting {

    private ArrayList<MappingData> mappingList;

    public MapSetting() {}

    public MapSetting(ArrayList<MappingData> mappingList) {
        this.mappingList = mappingList;
    }

    public ArrayList<MappingData> getMappingList() {
        return mappingList;
    }

    public void setMappingList(ArrayList<MappingData> mappingList) {
        this.mappingList = mappingList;
    }

}
