import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    private File f = new File("tempdata_serialized");
    public String check(String stn,String tag, String value){
        this.tag = tag;
        this.stn = stn;
        this.value = Double.valueOf(value);
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
        System.out.println(tag+" "+value);
        return value;
    } 
    private Double checkAvg(String avg, Double value){
        try{
                FileInputStream fis = new FileInputStream(f);
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
                /*
                if((Math.min(avgF, value)/Math.max(avgF, value)) > 0.80){
                        
                } else{
                    value = avgF;
                }
                */
            } catch(IOException e){
                e.printStackTrace();
            }
        
        return value;
    }
    private void store(String tag, Double value){
        try{
            //BufferedReader reader = new BufferedReader(new FileReader(f));
            
            stations = new HashMap<String, HashMap>();
            values = new HashMap<String, ArrayList<Double>>();
            ArrayList arr = values.get(tag);
            arr.add(value);
            values.remove(tag);
            values.put(tag, arr);
            
            FileOutputStream fos = new FileOutputStream(f);   
            ObjectOutputStream oos = new ObjectOutputStream(fos);           
            oos.writeObject(stations); 
            oos.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
}
