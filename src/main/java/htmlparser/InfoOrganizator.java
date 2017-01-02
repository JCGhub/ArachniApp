package htmlparser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import database.ConnectDB;

/**
 * @author Juanca
 *
 * Clase creada para la organización de la ejecución del fichero de configuración.
 *
 * 'multMainEntity_array'	Array que almacena cada una de las URLs de cada entidad perteneciente a la entidad principal múltiple.
 * 'attributeValues_array'	Array que almacena los valores de un atributo múltiple.
 * 'iD'						Instancia de InfoDownloader para el uso de las funciones de dicha clase.
 */

public class InfoOrganizator{

	ArrayList<String> multMainEntity_array = new ArrayList<String>();
	ArrayList<String> attributeValues_array = new ArrayList<String>();
	InfoDownloader iD = new InfoDownloader();
	public static InfoOrganizator iO;
	int c;

	/**
	 * Constructor de la clase InfoOrganizator.
	 *
	 */

	private InfoOrganizator(){}

	public static InfoOrganizator getInstance(){
		if(iO == null){
			iO = new InfoOrganizator();
			//System.out.println("First instance of InfoOrganizator!");
		}
		else{
			System.out.println("You can't create another instance of InfoOrganizator!");
		}

		return iO;
	}

	/**
	 * Función que dispone el inicio de la ejecución del programa a través de los datos introducidos en el fichero xml.
	 * Dependiendo de si la entidad principal es simple o múltiple la ejecución del programa seguirá un curso u otro.
	 * 
	 */

	public void mainExecution(){
		String mainEntitySize = XMLReader.mainEntity_array.get(0);
		String urlType = XMLReader.mainEntity_array.get(1);
		String mainEntityXpath;
		String urlRoot = XMLReader.mainEntity_array.get(3);
		c = 0;

		if(mainEntitySize.contains("simple")){			
			simpleEntityExecution();
		}
		else{			
			mainEntityXpath = XMLReader.mainEntity_array.get(2);

			if(XMLReader.nextPage_array.isEmpty()){
				System.out.println("Downloading main entities...");
				multMainEntity_array = iD.downloadArray(XMLReader.url, mainEntityXpath, null);

				if(urlType.contains("incomplete")){
					multMainEntity_array = iD.completeURLs(multMainEntity_array, urlRoot);
				}
	
				multEntityExecution();
			}
			else{
				nextPageExecution(mainEntityXpath, urlRoot);
			}
		}
	}

	/**
	 * Función que ejecuta la descarga de las URLs de las diferentes páginas de las que se quiere extraer información.
	 * La ejecución seguirá un curso u otro dependiendo de si se quiere navegar entre las páginas a través de la regla
	 * xPath perteneciente a un botón o bien a través de un patrón especificado en la URL de la entidad principal.
	 * 
	 * @param mainEntityXpath, Regla xPath de la entidad principal.
	 * @param urlRoot, Raíz de la URL de la entidad principal.
	 */

	public void nextPageExecution(String mainEntityXpath, String urlRoot){		
		String nextPageType = XMLReader.nextPage_array.get(0);
		String nextPageSize = XMLReader.nextPage_array.get(1);
		String nextPageRule = XMLReader.nextPage_array.get(2);
		String urlType = XMLReader.mainEntity_array.get(1);

		switch(nextPageType){
		case "button":			
			multMainEntity_array = iD.nextPagesButton(XMLReader.url, nextPageSize, nextPageRule, mainEntityXpath, urlType, urlRoot);				

			break;
		case "pattern":
			String nextPageInitValue = XMLReader.nextPage_array.get(3);
			String nextPageIncrement = XMLReader.nextPage_array.get(4);

			multMainEntity_array = iD.nextPagesPattern(XMLReader.url, nextPageSize, nextPageRule, nextPageInitValue, nextPageIncrement, mainEntityXpath, urlType, urlRoot);

			break;
		default:
			break;
		}

		multEntityExecution();
	}

	/**
	 * Función que realiza la descarga y almacenamiento de los valores de los atributos localizados en una única entidad.
	 * 
	 */

	public void simpleEntityExecution(){
		String rerun = XMLReader.confFile_array.get(3);
		int confFileId = 0;

		try{
			confFileId = getConfFileId();
		}catch(SQLException e1){
			e1.printStackTrace();
		}

		if(rerun.contains("update")){
			ConnectDB.db.deleteStringParamsByConfFile(confFileId);
		}

		String date = dateMaker();
		
		System.out.println("\nDownloading nodes from entity...");

		for(int i = 0; i < XMLReader.attributes_array.size(); i++){
			String attributeRule = XMLReader.attributes_array.get(i).get(1);

			attributeValues_array = iD.downloadArray(XMLReader.url, attributeRule, null);

			for(int j = 0; j < attributeValues_array.size(); j++){
				try{
					insertValuesDB(attributeValues_array.get(j), i, XMLReader.url, date);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}

		ResultSet rS = ConnectDB.db.existsDefaultQuery(confFileId);
		boolean b = true;

		try {
			while(rS.next()){     
				if(rS.getString("name").equals("Default query")){
					b = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(b){
			String query = "SELECT name, value, entity, date FROM string_info WHERE id_cf = \""+confFileId+"\"";
			ConnectDB.db.insertDefaultQuery("Default query", query, confFileId, 1, 1);
		}
	}

	/**
	 * Función que realiza la descarga y almacenamiento de los valores de los atributos localizados en varias entidades.
	 * 
	 */

	public void multEntityExecution(){
		String rerun = XMLReader.confFile_array.get(3);
		int confFileId = 0;

		try{
			confFileId = getConfFileId();
		}catch(SQLException e1){
			e1.printStackTrace();
		}

		if(rerun.contains("update")){
			ConnectDB.db.deleteStringParamsByConfFile(confFileId);
		}

		for(int i = 0; i < multMainEntity_array.size(); i++){
			String date = dateMaker();
			
			System.out.println("\nDownloading nodes from entity "+(i+1)+"...");

			for(int j = 0; j < XMLReader.attributes_array.size(); j++){
				String attributeRule = XMLReader.attributes_array.get(j).get(1);

				//String date = dateMaker();
				attributeValues_array = iD.downloadArray(multMainEntity_array.get(i), attributeRule, null);

				for(int k = 0; k < attributeValues_array.size(); k++){
					try{
						insertValuesDB(attributeValues_array.get(k), j, multMainEntity_array.get(i), date);
					}catch(SQLException e){
						e.printStackTrace();
					}
				}
			}
		}

		ResultSet rS = ConnectDB.db.existsDefaultQuery(confFileId);
		boolean b = true;

		try {
			while(rS.next()){     
				if(rS.getString("name").equals("Default query")){
					b = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(b){
			String query = "SELECT name, value, entity, date FROM string_info WHERE id_cf = \""+confFileId+"\"";
			ConnectDB.db.insertDefaultQuery("Default query", query, confFileId, 1, 1);
		}
	}

	/**
	 * Función que introduce en la base de datos los valores de los atributos descargados anteriormente.
	 * 
	 * @param value, Valor que contiene el atributo que se ha descargado.
	 * @param index, Índice del atributo del que se quiere almacenar su valor. Se necesita para distinguir entre un atributo y otro.
	 * @param urlEntity, Identificador de la entidad de una determinada url para distinguir los atributos de una entidad de los de otra entidad.
	 * @param date, Fecha en la que empezaron a ser descargados los atributos de una entidad.
	 * @throws SQLException
	 */

	public void insertValuesDB(String value, int index, String urlEntity, String date) throws SQLException{
		int numEntity = urlEntity.hashCode();
		int confFileId = getConfFileId(), webPortalId = 0, categoryId = 0;
		ResultSet webPortalRS = ConnectDB.db.getWebPortalId(XMLReader.confFile_array.get(1));
		ResultSet categoryRS = ConnectDB.db.getCategoryId(XMLReader.confFile_array.get(2));
		String nameAttribute = XMLReader.attributes_array.get(index).get(0);
		c++;

		while(webPortalRS.next()){
			webPortalId = webPortalRS.getInt(1);
		}

		while(categoryRS.next()){
			categoryId = categoryRS.getInt(1);
		}

		ConnectDB.db.insertStringParams(nameAttribute, value, numEntity, date, webPortalId, confFileId, categoryId);
	}

	public int countNodes(){
		return c;
	}

	/**
	 * Función que devuelve el Id del fichero de configuración asignado en la base de datos a partir de su nombre.
	 * 
	 * @return Devuelve el Id del fichero de configuración.
	 * @throws SQLException
	 */

	public int getConfFileId() throws SQLException{
		int confFileId = 0;

		ResultSet confFileRS = ConnectDB.db.getConfFileId(XMLReader.confFile_array.get(0));

		while(confFileRS.next()){
			confFileId = confFileRS.getInt(1);
		}

		return confFileId;
	}

	/**
	 * Función que genera la fecha actual del sistema para asignarsela o bien a la entrada del fichero de configuración
	 * o bien a los atributos de cada entidad.
	 * 
	 * @return Devuelve la fecha del sistema.
	 */

	public String dateMaker(){
		String date;
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DATE));
		String month = Integer.toString((cal.get(Calendar.MONTH)+1));
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minutes = Integer.toString(cal.get(Calendar.MINUTE));

		date = year+"-"+month+"-"+day+" "+hour+":"+minutes+":00";

		return date;
	}

	/**
	 * Función para visualizar las URLs de las entidades de las que se van a extraer información.
	 * 
	 */

	public void showMultMainEntity_array(){
		System.out.println("********** List of main entities **********\n");

		if(!(multMainEntity_array == null)){
			for(int i = 0; i < multMainEntity_array.size(); i++){
				System.out.println(i+": "+multMainEntity_array.get(i));
			}
		}
		else{
			System.out.println("This attribute hasn't values");
		}

		System.out.println();
	}

	/**
	 * Función para visualizar los valores del array del atributo actual que está almacenado en la variable multAttribute_array.
	 *
	 */

	public void showAttributeValues_array(){
		if(!(attributeValues_array == null)){
			for(int i = 0; i < attributeValues_array.size(); i++){
				System.out.println(i+": "+attributeValues_array.get(i));
			}
		}
		else{
			System.out.println("This attribute hasn't values");
		}
	}
}