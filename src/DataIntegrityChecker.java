import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.Double.max;
import static java.lang.Double.min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;


public class DataIntegrityChecker {
    private String stn;
    private double value;
    private String tag;
    private HashMap stations;
    private HashMap values;
    private Writer writer;
    private ArrayList valueArray;
    
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    
    private String path = "C:\\Users\\Eloy\\Documents\\NetBeansProjects\\ProjectWeer\\resources\\data_serialized.sec";
    
    public DataIntegrityChecker(){
        if(writer == null){
            createFile();
        }
        
    }
    public String check(String stn, String tag, String value){
        this.tag = tag;
        this.stn = stn;
        try{
            this.value = Double.valueOf(value);
        } catch(NumberFormatException e){
            this.value = extrapolate();
        }
        
        if(tag == "TEMP"){
            System.out.println("Dit is een temperatuur waarde "+value);
            Double extrapolated = extrapolate();
            System.out.println(extrapolated);
            if(min(extrapolated,this.value)/max(extrapolated,this.value )<0.8){
                this.value = extrapolated;
                System.out.println("Deze temperatuurwaarde "+this.value+" verschild meer dan 20% van "+extrapolated);
            }
        }

        store();
        value = String.valueOf(this.value);
        //System.out.println(tag+" "+value);
        return value;
    } 
    private Double extrapolate(){
        
        try{
            FileInputStream fis = new FileInputStream(path);
            try{
                ObjectInputStream ois = new ObjectInputStream(fis);
                stations = (HashMap) ois.readObject();
            } catch(EOFException e){
                stations = new HashMap();
            }
            fis.close();
            
            if(stations.get(stn)!=null){
                values = (HashMap) stations.get(stn);
                if(values.get(tag)!=null){
                    
                    valueArray = (ArrayList) values.get(tag);
                    Double count = 0.0;
                    for(int i=0;i<valueArray.size();i++){
                        count+=(Double)valueArray.get(i);
                    }
                    System.out.println(count);
                    System.out.println(valueArray.size());
                    System.out.println(count/valueArray.size());
                    value = count/valueArray.size();
                    value = round(value, 2);
                    
                    
                }
            } else{
                value = 0.0;                        //Screws with temperature value!!!
                System.out.println("Waarde naar 0.0 gezet");
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
        
        return value;
    }
    
    private void store(){
        try{
            FileInputStream fis = new FileInputStream(path);
            try{
                ObjectInputStream ois = new ObjectInputStream(fis);
                stations = (HashMap) ois.readObject();
                
            } catch(EOFException e){
                stations = new HashMap();
            }
            fis.close();
          
            if(stations.get(stn)==null) values = new HashMap();
            else values=(HashMap)stations.get(stn);

            if(values.get(tag)==null) valueArray = new ArrayList();
            else valueArray = (ArrayList) values.get(tag);
            
            if(valueArray.size()>=30){
                valueArray.remove(0);
            }
            valueArray.add(value);
            values.remove(tag);
            values.put(tag, valueArray);
            stations.put(stn, values);
            
            System.out.println(stations.toString());
               
            fos = new FileOutputStream(path); 
            oos = new ObjectOutputStream(fos);  
            oos.writeObject(stations);
            oos.close();
            fos.close();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private void createFile(){
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
    }
    
    
}
