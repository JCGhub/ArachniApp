package htmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader{
	public XMLReader(){}
	
	public ArrayList<String> readFile(String xml) throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
    	DocumentBuilder db = dbf.newDocumentBuilder();
    	Document doc = db.parse(new FileInputStream(new File(xml)));
    	ArrayList<String> arrayParam = new ArrayList<String>();
    	
    	NodeList paramList = doc.getElementsByTagName("PARAMETERS");
    	
    	int paramCount = paramList.getLength();
    	
    	for(int i = 0; i < paramCount; i++){    		
    		System.out.println("Parameters: ");
			
    		Node paramNode = paramList.item(i);
			Element param = (Element) paramNode;
			
			Node nameNode = param.getElementsByTagName("NAME").item(0);
			String name = nameNode.getFirstChild().getNodeValue();
			arrayParam.add(name);
	    	System.out.println("Name: " + name);			
			
			Node urlNode = param.getElementsByTagName("URL").item(0);
			String url = urlNode.getFirstChild().getNodeValue();
			arrayParam.add(url);
	    	System.out.println("Url: " + url);		
			
			Node xpath1Node = param.getElementsByTagName("XPATH1").item(0);
			String xpath1 = xpath1Node.getFirstChild().getNodeValue();
			arrayParam.add(xpath1);
	    	System.out.println("xPath1: " + xpath1);
			
	    	Node xpath2Node = param.getElementsByTagName("XPATH2").item(0);
			String xpath2 = xpath2Node.getFirstChild().getNodeValue();
			arrayParam.add(xpath2);
	    	System.out.println("xPath1: " + xpath2);
		}
    	
    	return arrayParam;
	}
}