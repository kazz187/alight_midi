package zone.kaz.alight_midi.device;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class LedDevice {

    private final String hostname;
    private final int port;
    private Socket socket;
    private OutputStream outputStream;

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
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
