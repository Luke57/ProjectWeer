import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionManager {
    public ConnectionManager() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(7789)) {
            while(true) {
                new ConnectionManagerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Fout met luisteren");
        }
    }
}