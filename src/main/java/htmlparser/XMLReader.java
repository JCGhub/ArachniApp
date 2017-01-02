package htmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import database.ConnectDB;

/**
 * @author Juanca
 * 
 * Clase creada para la validación y lectura del fichero xml.
 *
 * 'xmlContent_array'	Array que almacena todos los parámetros para luego mostrarlos en la función showXmlFileContent().
 * 'mainEntity_array'	Array que almacena los parámetros y reglas de la entidad principal.
 * 'confFile_array' 	Array que almacena los parámetros del fichero de configuración.
 * 'nextPage_array'		Array que almacena los parámetros y reglas de la función de página siguiente.
 * 'attributes_array'	Array que almacena todos los atributos asociados a cada entidad, que a su vez almacena los parámetros de cada atributo.
 * 'xmlFile'			Variable de tipo Document generada para la gestión de los nodos del fichero xml.
 * 'xml'				Ruta en la que se sitúa el fichero xml que se va a leer.
 * 'url'				Variable que almacena la URL de la entidad principal introducida en el fichero de configuración.
 */

public class XMLReader{

	ArrayList<String> xmlContent_array = new ArrayList<String>();
	static ArrayList<String> mainEntity_array = new ArrayList<String>();
	public static ArrayList<String> confFile_array = new ArrayList<String>();
	static ArrayList<String> nextPage_array = new ArrayList<String>();
	static ArrayList<ArrayList<String>> attributes_array = new ArrayList<ArrayList<String>>();
	public String fileConfName;
	Document xmlFile;
	String xml;
	static String url;

	/**
	 * Constructor de la clase XMLReader.
	 * 
	 * @param xml, Ruta en la que se sitúa el fichero xml que se va a leer.
	 * 
	 * @throws Exception
	 */

	public XMLReader(String xml) throws Exception{
		this.xml = xml;

		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
		dBF.setValidating(false);
		DocumentBuilder dBu = dBF.newDocumentBuilder();

		xmlFile = dBu.parse(new FileInputStream(new File(xml)));
	}

	/**
	 * Función que comprueba si el fichero xml leído se ciñe al formato generado por el xml schema.
	 * 
	 * @return Devuelve verdadero si el validador del schema acepta el formato del fichero xml.
	 */

	public boolean validateFile(){
		try{
			SchemaFactory sF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sF.newSchema(getClass().getClassLoader().getResource("confGen.xsd"));

			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xml)));
		}catch(IOException | SAXException e){
			JOptionPane.showMessageDialog(null, "Exception: "+e.getMessage());            
			return false;
		}

		return true;
	}

	/**
	 * Función que lee y almacena en los arrays creados globalmente los diferentes valores que almacenan 
	 * los nodos del fichero xml para su posterior uso.
	 * 
	 * @throws Exception
	 */

	public void readFile() throws Exception{
		xmlContent_array.add("Configuration File Parameters");
		xmlContent_array.add("-----------------------------");

		String confFileAtt1 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("name").getNodeValue();
		confFile_array.add(confFileAtt1);
		xmlContent_array.add("Name: "+confFileAtt1);
		String confFileAtt2 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("web_portal").getNodeValue();
		confFile_array.add(confFileAtt2);
		xmlContent_array.add("Web Portal: "+confFileAtt2);
		String confFileAtt3 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("category").getNodeValue();
		confFile_array.add(confFileAtt3);
		xmlContent_array.add("Category: "+confFileAtt3);
		String confFileAtt4 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("rerun").getNodeValue();
		confFile_array.add(confFileAtt4);
		xmlContent_array.add("Rerun: "+confFileAtt4);
		String confFileAtt5 = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("time").getNodeValue();
		confFile_array.add(confFileAtt5);
		xmlContent_array.add("Time (minutes): "+confFileAtt5);

		xmlContent_array.add("\nMain Entity parameters");
		xmlContent_array.add("----------------------");

		url = xmlFile.getElementsByTagName("url").item(0).getTextContent();
		xmlContent_array.add("Url: "+url);

		NodeList entities_list = xmlFile.getElementsByTagName("entities");

		for(int i = 0; i < entities_list.getLength(); i++){    		
			Node entity_node = entities_list.item(i);
			Element entity = (Element)entity_node;

			Node mainEntity_node = entity.getElementsByTagName("main_entity").item(0);
			Element mainEntity = (Element)mainEntity_node;

			String mainEntitySize = xmlFile.getElementsByTagName("main_entity").item(0).getAttributes().getNamedItem("size").getNodeValue();
			mainEntity_array.add(mainEntitySize);
			xmlContent_array.add("Size: "+mainEntitySize);
			String mainEntityUrlType = xmlFile.getElementsByTagName("main_entity").item(0).getAttributes().getNamedItem("url").getNodeValue();
			mainEntity_array.add(mainEntityUrlType);
			xmlContent_array.add("Url type: "+mainEntityUrlType);

			Node mainEntityRule_node = mainEntity.getElementsByTagName("rule").item(0);
			String mainEntityRule = mainEntityRule_node.getFirstChild().getNodeValue();
			mainEntity_array.add(mainEntityRule);
			xmlContent_array.add("Rule: "+mainEntityRule);

			Node urlRoot_node = mainEntity.getElementsByTagName("url_root").item(0);
			String urlRoot = urlRoot_node.getFirstChild().getNodeValue();
			mainEntity_array.add(urlRoot);
			xmlContent_array.add("Url root: "+urlRoot);

			if(entity.getElementsByTagName("next_page").getLength() > 0){
				xmlContent_array.add("\nNext Page parameters");
				xmlContent_array.add("--------------------");

				Node nextPage_node = entity.getElementsByTagName("next_page").item(0);
				Element nextPage = (Element)nextPage_node;

				String nextPageType = xmlFile.getElementsByTagName("next_page").item(0).getAttributes().getNamedItem("type").getNodeValue();
				nextPage_array.add(nextPageType);
				xmlContent_array.add("Type: "+nextPageType);
				String nextPageSize = xmlFile.getElementsByTagName("next_page").item(0).getAttributes().getNamedItem("size").getNodeValue();
				nextPage_array.add(nextPageSize);
				xmlContent_array.add("Size: "+nextPageSize);

				Node nextPageRule_node = nextPage.getElementsByTagName("rule").item(0);
				String nextPageRule = nextPageRule_node.getFirstChild().getNodeValue();
				nextPage_array.add(nextPageRule);
				xmlContent_array.add("Rule: "+nextPageRule);

				if(xmlFile.getElementsByTagName("next_page").item(0).getAttributes().getLength() > 2){
					String nextPageInitValue = xmlFile.getElementsByTagName("next_page").item(0).getAttributes().getNamedItem("initValue").getNodeValue();
					nextPage_array.add(nextPageInitValue);
					xmlContent_array.add("Initial Value: "+nextPageInitValue);
					String nextPageIncrement = xmlFile.getElementsByTagName("next_page").item(0).getAttributes().getNamedItem("increment").getNodeValue();
					nextPage_array.add(nextPageIncrement);
					xmlContent_array.add("Increment Value: "+nextPageIncrement);
				}		    	
			}
		}

		NodeList attributes_list = xmlFile.getElementsByTagName("attributes");

		for(int i = 0; i < attributes_list.getLength(); i++){
			Node attributes_node = attributes_list.item(i);
			Element attribute = (Element)attributes_node;

			NodeList field_list = attribute.getElementsByTagName("field");

			for(int j = 0; j < field_list.getLength(); j++){
				xmlContent_array.add("\nAttribute "+j+" parameters");
				xmlContent_array.add("-------------------------");

				ArrayList<String> attributes_currArray = new ArrayList<String>();

				Node field_node = field_list.item(j);
				Element field = (Element)field_node;

				Node attributeName_node = field.getElementsByTagName("name").item(0);
				String attributeName = attributeName_node.getFirstChild().getNodeValue();
				attributes_currArray.add(attributeName);
				xmlContent_array.add("Name: "+attributeName);

				Node attributeRule_node = field.getElementsByTagName("rule").item(0);
				String attributeRule = attributeRule_node.getFirstChild().getNodeValue();
				attributes_currArray.add(attributeRule);
				xmlContent_array.add("Rule: "+attributeRule);

				attributes_array.add(attributes_currArray);
			}
		}
	}

	/**
	 * Función creada para la inserción en la base de datos de los parámetros que corresponden al fichero de configuración.
	 * Es llamada desde MainWindow.
	 * 
	 * @return Devuelve b, que será true si el nombre del fichero ya existe ó false en caso contrario y por tanto se dispone a introducirlo en la base de datos.
	 */

	public boolean prepareConfFileParameters(){
		fileConfName = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("name").getNodeValue();
		String webPortal = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("web_portal").getNodeValue();
		String category = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("category").getNodeValue();
		boolean b = false;

		try{
			ResultSet rS = ConnectDB.db.getNameConfFile();
			int webPortalId = 0, categoryId = 0;

			ResultSet webPortalRS = ConnectDB.db.getWebPortalId(webPortal);
			ResultSet categoryRS = ConnectDB.db.getCategoryId(category);

			while(webPortalRS.next()){
				webPortalId = webPortalRS.getInt(1);
			}

			while(categoryRS.next()){
				categoryId = categoryRS.getInt(1);
			}

			while(rS.next()){			
				if(rS.getString("name").equals(fileConfName)){
					return true;
				}
			}

			ConnectDB.db.insertConfFileParameters(fileConfName, webPortalId, categoryId, dateMakerOnlyDay());
		}catch(SQLException e){
			e.printStackTrace();
		}

		return b;
	}

	/**
	 * Función creada para la inserción, en caso de que aun no exista, del parámetro web_portal del fichero de configuración en la base de datos.
	 * 
	 */

	public void prepareWebPortalParameters(){
		String webPortal = xmlFile.getElementsByTagName("conf").item(0).getAttributes().getNamedItem("web_portal").getNodeValue();
		boolean b = false;

		try{
			ResultSet rS = ConnectDB.db.getWebPortal();			

			while(rS.next()){			
				if(rS.getString("name").equals(webPortal)){
					b = true;
				}
			}

			if(!b){
				ConnectDB.db.insertWebPortalParameters(webPortal);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Función que genera la fecha actual del sistema sin especificar la hora para asignarsela a la entrada del 
	 * fichero de configuración.
	 * 
	 * @return Devuelve la fecha del sistema sin la hora.
	 */

	public String dateMakerOnlyDay(){
		String date;
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DATE));
		String month = Integer.toString((cal.get(Calendar.MONTH)+1));
		String year = Integer.toString(cal.get(Calendar.YEAR));

		date = year+"-"+month+"-"+day;

		return date;
	}

	/**
	 * Función que crea una instancia de la clase InfoOrganizator llamada desde ParserWindow para preparar la ejecución
	 * de las funciones de dicha clase.
	 * 
	 */

	public void infoReady(){
		InfoOrganizator.getInstance();
	}

	/**
	 * Función para visualizar el contenido del fichero xml de configuración.
	 * 
	 */

	public void showXmlFileContent(){
		for(int i = 0; i < xmlContent_array.size(); i++){
			System.out.println(xmlContent_array.get(i));
		}
	}

	/**
	 * Función que devuelve el array que contiene todos los parámetros introducidos en el fichero xml de configuración.
	 * 
	 * @return El array del contenido del fichero xml.
	 */

	public ArrayList<String> getXmlFileContent(){
		return xmlContent_array;
	}

	/**
	 * Función que devuelve el valor de la variable global url.
	 * 
	 * @return La url de la entidad principal.
	 */

	public String getUrl(){
		return url;
	}
	
	public String getFileConfName(){
		return fileConfName;
	}
}