import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ConnectionManagerThread  implements Runnable{
    private final Socket inputSocket;
    private Socket outputSocket;
    private BufferedReader dataIn;
    private PrintWriter dataOut;
    private StringBuilder weatherdataParsed;
    private final DataIntegrityChecker integrityChecker = new DataIntegrityChecker();
    private final XMLParser parser = new XMLParser();

    public ConnectionManagerThread(Socket socket){
        inputSocket = socket;
    }

    @Override
    public void run(){
        System.out.println("Thread: "+Thread.currentThread().getId());
        try {
            outputSocket = new Socket("localhost",7790);
            while (true) {
                dataIn = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
                dataOut = new PrintWriter(outputSocket.getOutputStream());
               
                String xml;
                StringBuilder weatherdata = new StringBuilder();
                while((xml = dataIn.readLine().replaceAll("<\\?xml(.+?)\\?>", "").trim())!= null){
                    weatherdata.append(xml);
                    if(xml.contains("</WEATHERDATA")){
                        weatherdataParsed = parser.parse(weatherdata, integrityChecker); //return the parsed weatherdata and send it on it's way
                        weatherdata = new StringBuilder();
                        dataOut.write(weatherdataParsed.toString());
                        dataOut.flush();
                        dataOut.close();
                    } 
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputSocket != null) {
                try {
                    dataIn.close();
                    dataOut.close();
                    inputSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
