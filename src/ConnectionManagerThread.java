import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManagerThread extends Thread{
    private Socket socket;
    private BufferedReader dataIn;
    private DataIntegrityChecker integrityChecker = new DataIntegrityChecker();

    public ConnectionManagerThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            while (true) {
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String xml = null;
                StringBuilder weatherdata = new StringBuilder();
                ArrayList arraydata = new ArrayList<>();
                while((xml = dataIn.readLine().replaceAll("<\\?xml(.+?)\\?>", "").trim())!= null){
                    weatherdata.append(xml);
                    arraydata.add(xml);

                    if(xml.contains("</WEATHERDATA")){
                        //weatherdata.append("</WEATHERDATA>");
                        XMLParser.parse(weatherdata, integrityChecker); //return the parsed weatherdata and send it on it's way
                        //System.out.println(weatherdata);
                        //System.out.println(arraydata.toString());
                        //integrityChecker.check(arraydata);
                        arraydata.clear();
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
                    e.printStackTrace();
                }
            }
        }
    }
}
