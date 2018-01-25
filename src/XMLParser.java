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
    
    public static void parse(StringBuilder data) {
        //System.out.println(data.toString());
        //System.out.println();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            
            NodeList nodes = doc.getElementsByTagName("MEASUREMENT");
            
            for (int i = 0; i < nodes.getLength(); i++) {
                NodeList subnodes = nodes.item(0).getChildNodes();
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
