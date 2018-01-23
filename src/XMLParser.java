package src;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
    
    public static void parse(StringBuilder data) {
        //System.out.println(input.toString());
        //System.out.println();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            
            NodeList nodes = doc.getElementsByTagName("MEASUREMENT");
            
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("" + nodes.item(i).getTextContent());
            }
            
        } catch(SAXException | IOException | ParserConfigurationException e){
            e.printStackTrace();
        }
    }
}
