import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    public ConnectionManager() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(7789)) {
            while(true) {
                //new ConnectionManagerThread(serverSocket.accept()).start();
                executor.execute(new ConnectionManagerThread(serverSocket.accept()));
                
            }
        } catch (IOException e) {
            System.err.println("Fout met luisteren");
        }
    }
}