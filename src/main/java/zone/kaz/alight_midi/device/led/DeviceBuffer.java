package zone.kaz.alight_midi.device.led;

import java.util.ArrayList;

public class DeviceBuffer {

    DeviceInfo deviceInfo;
    ArrayList<Stripe> stripes = new ArrayList<>();
    int dataLength = 0;

    public DeviceBuffer(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        int[][] mapping = deviceInfo.getMapping();
        for (int i = 0; i < mapping.length; i++) {
            stripes.add(i, new Stripe(mapping[i].length));
            dataLength += mapping[i].length * 3;
        }
    }

    public ArrayList<Stripe> getStripes() {
        return stripes;
    }

    public int getStripesLength() {
        return stripes.size();
    }

    public Stripe getStripe(int i) {
        return stripes.get(i);
    }

    public byte[] getData() {
        byte[] data = new byte[dataLength];
        int[][] mapping = deviceInfo.getMapping();
        for (int i = 0; i < mapping.length; i++) {
            for (int j = 0; j < mapping[i].length; j++) {
                Stripe stripe = stripes.get(i);
                for (int k = 0; k < 3; k++) {
                    data[mapping[i][j*3+k]] = stripe.getBuffer()[j*3+k];
                }
            }
        }
        return data;
    }

}
