import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DataIntegrityChecker {
    private double value;
    private double tempAvg;
    private double dewpAvg;
    private double stpAvg;
    private double slpAvg;
    private double visibAvg;
    private double wdspAvg;
    private double prcpAvg;
    private double sndpAvg;
    private double cldcAvg;
    private double wnddirAvg;
    
    private String stn;
    private String tag;
    private HashMap stations;
    private HashMap values;
    private Writer writer;
    
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    
    private String path = "C:\\Users\\xd\\Documents\\NetBeansProjects\\ProjectWeer\\resources\\data_serialized.sec";
    
    public DataIntegrityChecker(){
        if(writer == null){
            createFile();
        }
        
    }
    public String check(String stn,String tag, String value){
        this.tag = tag;
        this.stn = stn;
        try{
            this.value = Double.valueOf(value);
        } catch(NumberFormatException e){
            this.value = 0.0;                //remove null value TODO
        }
        switch(tag){
            case "TEMP":
                this.value = checkAvg("tempAvg", this.value);
                break;
            case "DEWP":
                this.value = checkAvg("dewpAvg", this.value);
                break;
            case "STP":
                this.value = checkAvg("stpAvg", this.value);
                break;
            case "SLP":
                this.value = checkAvg("slpAvg", this.value);
                break;
            case "VISIB":
                this.value = checkAvg("visibAvg", this.value);
                break;
            case "WDSP":
                this.value = checkAvg("wdspAvg", this.value);
                break;
            case "PRCP":
                this.value = checkAvg("prcpAvg", this.value);
                break;
            case "SNDP":
                this.value = checkAvg("sndpAvg", this.value);
                break;
            case "CLDC":
                this.value = checkAvg("cldcAvg", this.value);
                break;
            case "WNDDIR":
                this.value = checkAvg("wnddirAvg", this.value);
                break;
            default:
                System.out.println("Invalid value");
                break;
        }
        value = String.valueOf(this.value);
        //System.out.println(tag+" "+value);
        return value;
    } 
    private Double checkAvg(String avg, Double value){
        /*
        try{
                FileInputStream fis = new FileInputStream(path);
                ObjectInputStream ois = new ObjectInputStream(fis);             
                try{
                    stations = (HashMap) ois.readObject();                      //read file f
                } catch(ClassNotFoundException e){                              //if the file does not contain any HashMaps, create a new HashMap Structure
                    stations = new HashMap<>();
                    values = new HashMap<>();
                    ArrayList a = new ArrayList();
                    a.add(value);
                    values.put(tag, a);
                    stations.put(stn, values);                                  //put the value into values HashMap and values(HashMap) into the stations HashMap
                }
                values = (HashMap) stations.get(stn);
                ArrayList array = (ArrayList) values.get(tag);
                Double sum = 0.0;
                int i = 0;
                Iterator it = array.iterator();
                while(it.hasNext()){                                            //iterate trhough the values of the ArrayList and calculate the average
                    sum+=(Double)array.get(i);
                    i++;
                    it.next();
                }
                
                if((Math.min(avgF, value)/Math.max(avgF, value)) > 0.80){
                        
                } else{
                    value = avgF;
                }
                
            } catch(IOException e){
                e.printStackTrace();
            }
        */
        store(tag,value);
        return value;
    }
    private void store(String tag, Double value){
        try{
            ArrayList valueArray;
            //fis = new FileInputStream(path);
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
            
            //stations = new HashMap<String, HashMap>();
            //values = (HashMap) stations.get(stn);
            //ArrayList arr = (ArrayList)values.get(tag);
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
    
    
}
