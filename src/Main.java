import java.io.IOException;


public class Main {
    public static void main(String[] args){
        try{
            ConnectionManager conn = new ConnectionManager();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
