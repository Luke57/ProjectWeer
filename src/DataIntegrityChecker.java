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
    
    private final String path = "C:\\Users\\xd\\Documents\\NetBeansProjects\\ProjectWeer\\resources\\data_serialized.sec";
    
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
            System.out.println("Value "+this.tag+" is missing");
            this.value = extrapolate(this.value);
        }
        
        if(tag.equals("TEMP")){
            Double extrapolated = extrapolate(this.value);

            if(this.value > 0 && extrapolated > 0){
                if(min(extrapolated,this.value)/max(extrapolated,this.value )<0.8){
                    this.value = extrapolated;  
                }
            } else{
                if(max(extrapolated,this.value)/min(extrapolated,this.value )<0.8){
                    this.value = extrapolated;   
                }
            }
        }

        store();
        value = String.valueOf(this.value);
        return value;
    } 
    private Double extrapolate(Double valueExtra){
        
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
                    
                    valueExtra = count/valueArray.size();
                    valueExtra = round(valueExtra, 2);
                    
                    
                }
            } 
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        
        
        return valueExtra;
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
