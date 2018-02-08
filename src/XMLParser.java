import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OptionalDataException;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
    StringBuilder outputString;
    private ReentrantLock lock = new ReentrantLock();
    public StringBuilder parse(StringBuilder data, DataIntegrityChecker integrityChecker) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.println(data.toString());
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            
            NodeList nodes = doc.getElementsByTagName("WEATHERDATA");
            Element nodeElements =  (Element) nodes.item(0);
            NodeList dataElements = nodeElements.getElementsByTagName("MEASUREMENT");
            for (int i = 0; i < dataElements.getLength(); i++) {
                Element e = (Element) dataElements.item(i);
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
                
                e.getElementsByTagName("TEMP").item(0).setTextContent(TEMP);
                e.getElementsByTagName("DEWP").item(0).setTextContent(DEWP);
                e.getElementsByTagName("STP").item(0).setTextContent(STP);
                e.getElementsByTagName("SLP").item(0).setTextContent(SLP);
                e.getElementsByTagName("VISIB").item(0).setTextContent(VISIB);
                e.getElementsByTagName("WDSP").item(0).setTextContent(WDSP);
                e.getElementsByTagName("PRCP").item(0).setTextContent(PRCP);
                e.getElementsByTagName("SNDP").item(0).setTextContent(SNDP);
                e.getElementsByTagName("CLDC").item(0).setTextContent(CLDC);
                e.getElementsByTagName("WNDDIR").item(0).setTextContent(WNDDIR);
                
                
                
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
  
            outputString = new StringBuilder();
            outputString.append(output.toString("UTF-8"));
            
            
        } catch(SAXException | IOException | ParserConfigurationException | TransformerException e){
            //e.printStackTrace();
        }
        return outputString;
    }
    
}
