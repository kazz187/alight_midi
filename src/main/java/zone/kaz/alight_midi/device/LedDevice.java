package zone.kaz.alight_midi.device;

import zone.kaz.alight_midi.device.led.DeviceBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class LedDevice {

    private final String hostname;
    private final int port;
    private Socket socket;
    private OutputStream outputStream;
    private boolean isConnecting = false;

    public LedDevice(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        connect();
    }

    public void connect() {
        try {
            socket = new Socket(hostname, port);
            outputStream = socket.getOutputStream();
            isConnecting = true;
        } catch (IOException e) {
            System.err.println("Failed to connect to LED device server. (" + hostname + ":" + port + ")");
        }
    }

    public void send(DeviceBuffer deviceBuffer) {
        if (!isConnecting) {
            return;
        }
        int channel = 0; // TODO: Support multi fadecandy.
        byte[] message = deviceBuffer.getData();
        char len = (char) message.length;
        byte[] header = {(byte) channel, 0, (byte) (len >> 8), (byte) (len & 255)};
        try {
            outputStream.write(header);
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
            isConnecting = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
