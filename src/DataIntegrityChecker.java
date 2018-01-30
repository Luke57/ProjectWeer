import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;

public class DataIntegrityChecker {
    private float value;
    private float tempAvg;
    private float dewpAvg;
    private float stpAvg;
    private float slpAvg;
    private float visibAvg;
    private float wdspAvg;
    private float prcpAvg;
    private float sndpAvg;
    private float cldcAvg;
    private float wnddirAvg;
    public String check(String tag, String value){
        this.value = Float.valueOf(value);
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
    private Float checkAvg(String avg, Float value){
        return value;
    }
    private static void store(){
        //TODO
    }
    
    
}
