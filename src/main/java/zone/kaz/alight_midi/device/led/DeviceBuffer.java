package zone.kaz.alight_midi.device.led;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
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
                    data[mapping[i][j]*3+k] = stripe.getBuffer()[j*3+k];
                }
            }
        }
        return data;
    }

    public void add(DeviceBuffer deviceBuffer) {
        if (!Objects.equals(deviceBuffer.getDeviceInfo().getName(), deviceInfo.getName())) {
            return;
        }

        ArrayList<Stripe> stripes = deviceBuffer.getStripes();
        for (int i = 0; i < stripes.size(); i++) {
            byte[] buffer = stripes.get(i).getBuffer();
            byte[] bufferOrigin = this.stripes.get(i).getBuffer();
            for (int j = 0; j < bufferOrigin.length; j++) {
                int data = (bufferOrigin[j] & 0xff) + (buffer[j] & 0xff);
                bufferOrigin[j] = (byte) (data > 0xff ? 0xff : data);
            }
        }
    }

    public void multiply(double filter) {
        for (Stripe stripe : stripes) {
            byte[] buffer = stripe.getBuffer();
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) ((buffer[i] & 0xff) * filter);
            }
        }
    }

}
