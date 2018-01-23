import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManagerThread extends Thread{
    private Socket socket = null;
    private BufferedReader dataIn;

    public ConnectionManagerThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            while (true) {
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String xml = null;
                StringBuilder weatherdata = new StringBuilder();
                while((xml = dataIn.readLine().replaceAll("<\\?xml(.+?)\\?>", "").trim())!= null){
                    weatherdata.append(xml);

                    if(xml.contains("</WEATHERDATA")){
                        XMLParser.parse(weatherdata);
                        weatherdata = new StringBuilder();
                    }
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(socket != null) {
                try {
                    dataIn.close();
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
