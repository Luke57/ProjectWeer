import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try {
            ConnectionManager conn = new ConnectionManager();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
