package htmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import database.ConnectDB;

public class XMLReader{
	ArrayList<String> arrayMainEnt = new ArrayList<String>();
	public ArrayList<String> arrayConfAtt = new ArrayList<String>();
	ArrayList<ArrayList<String>> arrayPredEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arrayAtt = new ArrayList<ArrayList<String>>();
	Document xmlFile;
	String xml, url;
	public boolean urlExc = false;
	ConnectDB db;
	
	public XMLReader(String xml, ConnectDB db) throws Exception{
		this.xml = xml;
		this.db = db;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
    	DocumentBuilder dbu = dbf.newDocumentBuilder();
    	
    	xmlFile = dbu.parse(new FileInputStream(new File(xml)));
	}
	
	public boolean validateFile(){
		try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("confGen2.xsd"));
            
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (IOException | SAXException e) {
            //System.out.println("Exception: "+e.getMessage());
        	JOptionPane.showMessageDialog(null, "Exception: "+e.getMessage());
            
            return false;
        }
		
        return true;
	}
	
	public void readFile() throws Exception{
		//Extraemos los atributos de la etiqueta conf
		
		String confAtt1 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("name").getNodeValue();
		arrayConfAtt.add(confAtt1);
    	String confAtt2 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("cat").getNodeValue();
    	arrayConfAtt.add(confAtt2);
    	
    	//Extraemos la url
    	
    	url = xmlFile.getElementsByTagName("url").item(0).getTextContent();
    	
    	//Extraemos las entidades
    	
    	NodeList entitiesList = xmlFile.getElementsByTagName("entities");
    	
    	for(int i = 0; i < entitiesList.getLength(); i++){    		
    		Node entityNode = entitiesList.item(i);
			Element entity = (Element)entityNode;
			
			//Extraemos la entidad principal

			Node mainEntNode = entity.getElementsByTagName("main_entity").item(0);
			Element mainEnt = (Element)mainEntNode;
			
			String mainEntSize = xmlFile.getElementsByTagName("main_entity").item(0).getAttributes().getNamedItem("size").getNodeValue();
			arrayMainEnt.add(mainEntSize);
			String mainEntUrlType = xmlFile.getElementsByTagName("main_entity").item(0).getAttributes().getNamedItem("url").getNodeValue();
			arrayMainEnt.add(mainEntUrlType);
	    	
	    	Node ruleMENode = mainEnt.getElementsByTagName("rule").item(0);
			if(mainEnt.getElementsByTagName("rule").getLength() > 0){
				String ruleME = ruleMENode.getFirstChild().getNodeValue();
				arrayMainEnt.add(ruleME);
			}	    	
			
			if(mainEnt.getElementsByTagName("url_root").getLength() > 0){
				Node urlRootNode = mainEnt.getElementsByTagName("url_root").item(0);
				String urlRoot = urlRootNode.getFirstChild().getNodeValue();
				arrayMainEnt.add(urlRoot);
			}
			else{
				if(mainEnt.getElementsByTagName("url_root").getLength() == 0 && mainEntUrlType.contains("incomplete")){
					urlExc = true;					
					return;
				}
			}
			
			//Extraemos las entidades predeterminadas
			
			NodeList predEntList = entity.getElementsByTagName("pred_entity");
			
			for(int j = 0; j < predEntList.getLength(); j++){
				ArrayList<String> arrayCurrPredEnt = new ArrayList<String>();
				
	    		Node predEntNode = predEntList.item(j);
				Element predEnt = (Element)predEntNode;
				
				String predType = xmlFile.getElementsByTagName("pred_entity").item(0).getAttributes().getNamedItem("type").getNodeValue();
				arrayCurrPredEnt.add(predType);
				
				Node nameNode = predEnt.getElementsByTagName("name").item(0);
				String name = nameNode.getFirstChild().getNodeValue();
				arrayCurrPredEnt.add(name);
				
				NodeList ruleList = predEnt.getElementsByTagName("rules");
				
				for(int k = 0; k < ruleList.getLength(); k++){		    		
		    		Node ruleNode = ruleList.item(k);
					Element rule = (Element)ruleNode;
					
					Node rulePENode = rule.getElementsByTagName("rule").item(0);
					String rulePE = rulePENode.getFirstChild().getNodeValue();
					arrayCurrPredEnt.add(rulePE);
		    	}		    	
		    	
		    	arrayPredEnt.add(arrayCurrPredEnt);
	    	}
    	}
    	
    	//Extraemos las atributos
    	
    	NodeList attList = xmlFile.getElementsByTagName("attributes");
    	
    	for(int i = 0; i < attList.getLength(); i++){
    		Node attNode = attList.item(i);
			Element attribute = (Element)attNode;
			
			NodeList fieldList = attribute.getElementsByTagName("field");
			
			for(int j = 0; j < fieldList.getLength(); j++){
				ArrayList<String> arrayCurrAtt = new ArrayList<String>();
	    		
	    		Node fieldNode = fieldList.item(j);
				Element field = (Element)fieldNode;
				
				Node nameNode = field.getElementsByTagName("name").item(0);
				String name = nameNode.getFirstChild().getNodeValue();
				arrayCurrAtt.add(name);
		    	
		    	Node ruleNode = field.getElementsByTagName("rule").item(0);
				String rule = ruleNode.getFirstChild().getNodeValue();
				arrayCurrAtt.add(rule);
				
				NamedNodeMap getAtt = field.getAttributes();
				int numAtt = getAtt.getLength();
				
				String attSize = fieldNode.getAttributes().getNamedItem("size").getNodeValue();
				arrayCurrAtt.add(attSize);
				
				if(numAtt > 1){					
					String fieldAtt = fieldNode.getAttributes().getNamedItem("type").getNodeValue();
					arrayCurrAtt.add(fieldAtt);
				}
		    	
		    	arrayAtt.add(arrayCurrAtt);
	    	}
    	}
	}
	
	public InfoOrganizator infoReady(){
    	InfoOrganizator iO = new InfoOrganizator(url, arrayConfAtt, arrayMainEnt, arrayPredEnt, arrayAtt, db);
	
    	return iO;
	}
	
	public int insertConfParameters(){
		InfoDownloader iD = new InfoDownloader(arrayConfAtt.get(0), arrayConfAtt.get(1));
		int i = 0;
		
		try {
			i = iD.insertConfFileParamsDB(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return i;
	}
	
	public void showArrayConfAtt(){
		System.out.println("Configuration file \""+arrayConfAtt.get(0)+"\", Category: "+arrayConfAtt.get(1));
		System.out.println("--------------------------------------------------------------------------------\n");
		System.out.println("*****  MOSTRANDO ARRAY QUE DEVUELVE LOS ATRIBUTOS DE conf  *****");
		
		for(int i = 0; i < arrayConfAtt.size(); i++){
			System.out.println("- "+arrayConfAtt.get(i));
		}
		
		System.out.println("****************************************************************\n");
	}
	
	public void showArrayMainEnt(){
		System.out.println("*****  MOSTRANDO ARRAY QUE DEVUELVE EL CONTENIDO DE mainEnt  *****");
		
		for(int i = 0; i < arrayMainEnt.size(); i++){
			System.out.println("- "+arrayMainEnt.get(i));
		}
		
		System.out.println("******************************************************************\n");
	}
	
	public void showArrayPredEnt(){
		System.out.println("*****  MOSTRANDO ARRAY QUE DEVUELVE EL CONTENIDO DE predEnt  *****");
		
		for(int i = 0; i < arrayPredEnt.size(); i++){
			ArrayList<String> currArrayPredEnt = new ArrayList<String>();
			
			currArrayPredEnt = arrayPredEnt.get(i);
			
			for(int j = 0; j < currArrayPredEnt.size(); j++){
				System.out.println("Ent. Pred. "+i+": "+currArrayPredEnt.get(j));
			}
		}
		
		System.out.println("******************************************************************\n");
	}
	
	public void showArrayAtt(){
		System.out.println("*****  MOSTRANDO ARRAY QUE DEVUELVE EL CONTENIDO DE attributes  *****");
		
		for(int i = 0; i < arrayAtt.size(); i++){
			ArrayList<String> currArrayAtt = new ArrayList<String>();
			
			currArrayAtt = arrayAtt.get(i);
			
			for(int j = 0; j < currArrayAtt.size(); j++){
				System.out.println("Atributo "+i+": "+currArrayAtt.get(j));
			}
		}
		
		System.out.println("*********************************************************************\n");
	}
	
	public String getUrl(){
		return url;
	}
}