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
                
                integrityChecker.check("TEMP", TEMP);
                integrityChecker.check("DEWP", DEWP);
                integrityChecker.check("STP", STP);
                integrityChecker.check("SLP", SLP);
                integrityChecker.check("VISIB", VISIB);
                integrityChecker.check("WDSP", WDSP);
                integrityChecker.check("PRCP", PRCP);
                integrityChecker.check("SNDP", SNDP);
                integrityChecker.check("CLDC", CLDC);
                integrityChecker.check("WNDDIR", WNDDIR);
                /*
                NodeList subnodes = nodes.item(0).getChildNodes();
                
                String stn = subnodes.getAttributes();
                    for(int j = 0; j<(subnodes.getLength());j++){
                        try{
                            String tag = subnodes.item(j).getNodeName();
                            String value = subnodes.item(j).getTextContent();
                            System.out.println(tag+" "+value);
                        } catch(NullPointerException e){
                            System.out.println("MISSING VALUE");
                        }
                    }
                System.out.println("--------------");
                */
                
            }
            
        } catch(SAXException | IOException | ParserConfigurationException e){
            e.printStackTrace();
        }
    }
    /*
    public static void doSomething(Element element) {
    // do something with the current node instead of System.out
    System.out.println(node.getNodeName()+" "+element);

    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node currentNode = nodeList.item(i);
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            //calls this method for all the children which is Element
            doSomething(currentNode);
        }
    }
    }
    */
}
