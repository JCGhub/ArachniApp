package htmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader{
	ArrayList<String> arrayMainEnt = new ArrayList<String>();
	ArrayList<String> arrayConfAttributes = new ArrayList<String>();
	ArrayList<ArrayList<String>> arrayPredEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arraySecEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arrayAtt = new ArrayList<ArrayList<String>>();
	Document xmlFile;
	String xml, url;
	
	public XMLReader(String xml) throws Exception{
		this.xml = xml;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
    	DocumentBuilder db = dbf.newDocumentBuilder();
    	
    	xmlFile = db.parse(new FileInputStream(new File(xml)));
	}
	
	public boolean validateFile(){
		try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("confGen2.xsd"));
            
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            
            return false;
        }
		
        return true;
	}
	
	public void readFile() throws Exception{
		//Extraemos los atributos de la etiqueta conf
		
		String confAtt1 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("name").getNodeValue();
		arrayConfAttributes.add(confAtt1);
    	String confAtt2 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("cat").getNodeValue();
    	arrayConfAttributes.add(confAtt2);
    	
    	System.out.println("Configuration file \""+confAtt1+"\", Category: "+confAtt2);
		System.out.println("----------------------------------------\n");
    	
    	//Extraemos la url
    	
    	url = xmlFile.getElementsByTagName("url").item(0).getTextContent();
    	System.out.println("Url: " + url);
    	
    	//Extraemos las entidades
    	
    	NodeList entitiesList = xmlFile.getElementsByTagName("entities");
    	
    	for(int i = 0; i < entitiesList.getLength(); i++){
    		System.out.println("\n\nEntities");
    		System.out.println("----------------------------------------\n");
    		
    		Node entityNode = entitiesList.item(i);
			Element entity = (Element)entityNode;
			
			//Extraemos la entidad principal

			Node mainEntNode = entity.getElementsByTagName("main_entity").item(0);
			
			String mainEntSize = xmlFile.getElementsByTagName("main_entity").item(0).getAttributes().getNamedItem("size").getNodeValue();
			arrayMainEnt.add(mainEntSize);
			System.out.println("Size: "+mainEntSize);
			
			arrayMainEnt.add(mainEntNode.getFirstChild().getNodeValue());
			System.out.println("Main entity: "+arrayMainEnt.get(1));
			
			//Extraemos las entidades predeterminadas
			
			NodeList predEntList = entity.getElementsByTagName("pred_entity");
			
			for(int j = 0; j < predEntList.getLength(); j++){
				ArrayList<String> arrayCurrPredEnt = new ArrayList<String>();
				
				System.out.println("\nPredeterminate entity: ");
				
	    		Node predEntNode = predEntList.item(j);
				Element predEnt = (Element)predEntNode;
				
				String predType = xmlFile.getElementsByTagName("pred_entity").item(0).getAttributes().getNamedItem("type").getNodeValue();
				arrayCurrPredEnt.add(predType);
				System.out.println("Type: "+predType);
				
				Node nameNode = predEnt.getElementsByTagName("name").item(0);
				String name = nameNode.getFirstChild().getNodeValue();
				arrayCurrPredEnt.add(name);
		    	System.out.println("Name: "+name);
		    	
		    	Node ruleNode = predEnt.getElementsByTagName("rule").item(0);
				String rule = ruleNode.getFirstChild().getNodeValue();
				arrayCurrPredEnt.add(rule);
		    	System.out.println("Rule: "+rule);
		    	
		    	arrayPredEnt.add(arrayCurrPredEnt);
	    	}
			
			//Extraemos las entidades secundarias
			
			NodeList secEntList = entity.getElementsByTagName("sec_entity");
			
			for(int j = 0; j < secEntList.getLength(); j++){
				ArrayList<String> arrayCurrSecEnt = new ArrayList<String>();
				
				System.out.println("\nSecondary entity: ");
				
	    		Node secEntNode = secEntList.item(j);
				Element secEnt = (Element)secEntNode;
				
				Node nameNode = secEnt.getElementsByTagName("name").item(0);
				String name = nameNode.getFirstChild().getNodeValue();
				arrayCurrSecEnt.add(name);
		    	System.out.println("Name: "+name);
		    	
		    	Node ruleNode = secEnt.getElementsByTagName("rule").item(0);
				String rule = ruleNode.getFirstChild().getNodeValue();
				arrayCurrSecEnt.add(rule);
		    	System.out.println("Rule: "+rule);
		    	
		    	arraySecEnt.add(arrayCurrSecEnt);
	    	}
    	}
    	
    	//Extraemos las atributos
    	
    	NodeList attList = xmlFile.getElementsByTagName("attributes");
    	
    	for(int i = 0; i < attList.getLength(); i++){
    		System.out.println("\n\nAttributes");
    		System.out.println("----------------------------------------");
    		
    		Node attNode = attList.item(i);
			Element attribute = (Element)attNode;
			
			NodeList fieldList = attribute.getElementsByTagName("field");
			
			for(int j = 0; j < fieldList.getLength(); j++){
				ArrayList<String> arrayCurrAtt = new ArrayList<String>();
				
	    		System.out.println("\nField: ");
	    		
	    		Node fieldNode = fieldList.item(j);
				Element field = (Element)fieldNode;
				
				if(fieldNode.hasAttributes()){
					String fieldAtt = fieldNode.getAttributes().getNamedItem("type").getNodeValue();
					arrayCurrAtt.add(fieldAtt);
					System.out.println("Field att: "+fieldAtt);
				}
				
				Node nameNode = field.getElementsByTagName("name").item(0);
				String name = nameNode.getFirstChild().getNodeValue();
				arrayCurrAtt.add(name);
		    	System.out.println("Name: "+name);
		    	
		    	Node ruleNode = field.getElementsByTagName("rule").item(0);
				String rule = ruleNode.getFirstChild().getNodeValue();
				arrayCurrAtt.add(rule);
		    	System.out.println("Rule: "+rule);
		    	
		    	arrayAtt.add(arrayCurrAtt);
	    	}
    	}
	}
	
	public String getURL(){
		return url;
	}
	
	public ArrayList<String> getMainEnt(){
		return arrayMainEnt;
	}
	
	public ArrayList<ArrayList<String>> getSecEnt(){
		return arraySecEnt;
	}
	
	public ArrayList<String> getConfAtt(){
		return arrayConfAttributes;
	}
	
	public ArrayList<ArrayList<String>> getAtt(){
		return arrayAtt;
	}
}