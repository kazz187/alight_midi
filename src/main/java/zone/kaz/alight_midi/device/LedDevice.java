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
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(hostname, port);
        outputStream = socket.getOutputStream();
        isConnecting = true;
    }

    public void send(DeviceBuffer deviceBuffer) {
        if (!isConnecting) {
            return;
        }
        byte[] message = deviceBuffer.getData();
        char len = (char) message.length;
        byte[] header = {0, 0, (byte) (len >> 8), (byte) (len & 255)};
        try {
            outputStream.write(header);
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
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
