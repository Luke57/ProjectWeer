import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
    
    public static void parse(StringBuilder data, DataIntegrityChecker integrityChecker) {
        //System.out.println(data.toString());
        //System.out.println();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            
            NodeList nodes = doc.getElementsByTagName("WEATHERDATA");
            Element nodeElements =  (Element) nodes.item(0);
            NodeList dataElements = nodeElements.getElementsByTagName("MEASUREMENT");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) dataElements.item(0);
                String STN = e.getElementsByTagName("STN").item(0).getTextContent();
                String DATE = e.getElementsByTagName("DATE").item(0).getTextContent();
                String TIME = e.getElementsByTagName("TIME").item(0).getTextContent();
                String TEMP = e.getElementsByTagName("TEMP").item(0).getTextContent();
                String DEWP = e.getElementsByTagName("DEWP").item(0).getTextContent();
                String STP = e.getElementsByTagName("STP").item(0).getTextContent();
                String SLP = e.getElementsByTagName("SLP").item(0).getTextContent();
                String VISIB = e.getElementsByTagName("VISIB").item(0).getTextContent();
                String WDSP = e.getElementsByTagName("WDSP").item(0).getTextContent();
                String PRCP = e.getElementsByTagName("PRCP").item(0).getTextContent();
                String SNDP = e.getElementsByTagName("SNDP").item(0).getTextContent();
                String FRSHTT = e.getElementsByTagName("FRSHTT").item(0).getTextContent();
                String CLDC = e.getElementsByTagName("CLDC").item(0).getTextContent();
                String WNDDIR = e.getElementsByTagName("WNDDIR").item(0).getTextContent();
                //System.out.println(STN+" "+DATE+" "+TIME+" "+TEMP+" "+DEWP+" "+STP+" "+SLP+" "+VISIB+" "+WDSP+" "+PRCP+" "+SNDP+" "+FRSHTT+" "+CLDC+" "+WNDDIR);
                
                TEMP = integrityChecker.check(STN,"TEMP", TEMP);
                DEWP = integrityChecker.check(STN,"DEWP", DEWP);
                STP = integrityChecker.check(STN,"STP", STP);
                SLP = integrityChecker.check(STN,"SLP", SLP);
                VISIB = integrityChecker.check(STN,"VISIB", VISIB);
                WDSP = integrityChecker.check(STN,"WDSP", WDSP);
                PRCP = integrityChecker.check(STN,"PRCP", PRCP);
                SNDP = integrityChecker.check(STN,"SNDP", SNDP);
                CLDC = integrityChecker.check(STN,"CLDC", CLDC);
                WNDDIR = integrityChecker.check(STN,"WNDDIR", WNDDIR);
                
                
            }
            
        } catch(SAXException | IOException | ParserConfigurationException e){
            e.printStackTrace();
        }
    }
    
}
