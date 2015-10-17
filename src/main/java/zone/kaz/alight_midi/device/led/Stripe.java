package zone.kaz.alight_midi.device.led;

public class Stripe {

    byte[] buffer;

    public Stripe(int length) {
        buffer = new byte[length * 3];
    }

    public byte[] getBuffer() {
        return buffer;
    }

}
