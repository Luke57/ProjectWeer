import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class ConnectionManager {
    public ConnectionManager() throws IOException {
        int port = 7789;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while(true) {
                new ConnectionManagerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Fout met luisteren");
            System.exit(-1);
        }
    }
}