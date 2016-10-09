package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Juanca
 * 
 * Clase creada para realizar las llamadas a las descargas de la información de las webs.
 *
 */

public class InfoDownloader{
	
	/**
	 * Constructor vacío de la clase InfoDownloader para el uso de las funciones de descarga sin necesidad de inicializar
	 * ningún valor. Será el encargado de las llamadas a función de la clase HTMLParser.
	 * 
	 */
	
	public InfoDownloader(){}
	
	/**
	 * Función que descarga la información de una web y la devuelve en un array de String.
	 * 
	 * @param url, Dirección URL de donde se quiere descargar los valores.
	 * @param xPath, Ruta xPath del elemento html que se quiere descargar de la web indicada.
	 * @return Devuelve el array con los valores deseados.
	 */
	
	public ArrayList<String> downloadArray(String url, String xPath, String flag){
		ArrayList<String> values_array = new ArrayList<String>();
		
		HTMLParser hP = new HTMLParser(url, xPath);
		values_array = hP.downloadAsArray(flag);
		
		return values_array;
	}
	
	/**
	 * Función que descarga la información de una web y la devuelve en un String.
	 * 
	 * @param url, Dirección URL de donde se quiere descargar los valores.
	 * @param xPath, Ruta xPath del elemento html que se quiere descargar de la web indicada.
	 * @return Devuelve el String con el valor deseado.
	 */
	
	public String downloadString(String url, String xPath){
		String value;
		
		HTMLParser hP = new HTMLParser(url, xPath);
		value = hP.downloadAsString();
		
		return value;
	}
	
	/**
	 * Función que descarga todo el contenido xml de la URL especificada como entidad principal.
	 * 
	 * @param url, Dirección URL de donde se quiere descargar el contenido xml.
	 * @throws Exception
	 */
	
	public void downloadContent(String url) throws Exception{
		HTMLParser hP = new HTMLParser(url);
        StringBuffer webContent = hP.downloadContent();        
        
        System.out.println(webContent);
	}
	
	/**
	 * Función que concatena la raíz de una URL al resto de las direcciones que se encontraban incompletas. Esta función
	 * funciona para un grupo de más de una URL incompleta.
	 * 
	 * @param incompleteURLs_array, Array que contiene al grupo de URLs incompletas.
	 * @param urlRoot, Raíz de la URL que se concatenará con el resto de las direcciones incompletas.
	 * @return Devuelve un array con las URLs completas.
	 */
	
	public ArrayList<String> completeURLs(ArrayList<String> incompleteURLs_array, String urlRoot){
		ArrayList<String> completeURLs_array = new ArrayList<String>();
		
		for(int i = 0; i < incompleteURLs_array.size(); i++){
			String incompleteURL = incompleteURLs_array.get(i);
			String completeURL = urlRoot + incompleteURL;
			
			completeURLs_array.add(completeURL);
		}
		
		return completeURLs_array;
	}
	
	/**
	 * Función que concatena la raíz de una URL al una dirección que se encontraba incompleta. Esta función trabaja sólo
	 * para una URL incompleta.
	 * 
	 * @param incompleteURL, Variable que contiene a la URL incompleta.
	 * @param urlRoot, Raíz de la URL que se concatenará con el resto de la dirección incompleta.
	 * @return Devuelve un String con la URL completa.
	 */
	
	public String completeURL(String incompleteURL, String urlRoot){		
		return urlRoot + incompleteURL;
	}
	
	/**
	 * Función que realiza las acciones necesarias para la navegación entre páginas que contienen entidades principales
	 * de las cuales se desea descargar información cuando en la web se dispone de un botón siguiente enlazado.
	 * 
	 * @param url, Dirección URL de la entidad principal. Es la especificada en el fichero de configuración.
	 * @param nextPageSize, Variable que determina si se quieren recorrer todas las páginas con entidades o sólo algunas.
	 * @param nextPageXpath, Ruta xPath que contiene la URL que nos conduce a la siguiente página.
	 * @param nextPageNumPages, Número de páginas que se desean recorrer.
	 * @param mainEntityXpath, Ruta xPath que contiene la URL de la entidad principal.
	 * @param urlType, Tipo de la URL en referencia a si la web nos la proporciona completa o incompleta.
	 * @param urlRoot, Raíz de la URL para concatenarla a una dirección que está incompleta.
	 * @return Devuelve un array con todas las URLs de las entidades de las que se desea extraer información.
	 */
	
	public ArrayList<String> nextPagesButton(String url, String nextPageSize, String nextPageXpath, String mainEntityXpath, String urlType, String urlRoot){
		ArrayList<String> currentUrl_array = new ArrayList<String>();
		ArrayList<String> multMainEntity_array = new ArrayList<String>();
		String currentUrl = url, previousUrl = "";
		boolean lastPage = false;
		int k = 0;
		
		if(nextPageSize.contains("all")){
			while((!(downloadString(currentUrl, nextPageXpath).isEmpty()) || lastPage == false)){
				if(downloadString(currentUrl, nextPageXpath).isEmpty() && lastPage == false){
					System.out.println("Downloading main entities (indefinite pages | last page)...");
					currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
					lastPage = true;
					
					if(urlType.contains("incomplete")){
						currentUrl_array = completeURLs(currentUrl_array, urlRoot);
					}
					
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}
				else{
					String nextUrl = downloadString(currentUrl, nextPageXpath);
						
					if(urlType.contains("incomplete")){
						//System.out.println("** nextUrl: "+completeURL(nextUrl, urlRoot));
						//System.out.println("** previousUrl: "+previousUrl);
						
						if(!(completeURL(nextUrl, urlRoot).equals(previousUrl))){
							System.out.println("Downloading main entities (indefinite pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);						
							currentUrl_array = completeURLs(currentUrl_array, urlRoot);
							
							previousUrl = currentUrl;
							currentUrl = completeURL(nextUrl, urlRoot);
						}
						else{
							System.out.println("Downloading main entities (indefinite pages | anti-loop)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);						
							currentUrl_array = completeURLs(currentUrl_array, urlRoot);
							
							for(int i = 0; i < currentUrl_array.size(); i++){
								multMainEntity_array.add(currentUrl_array.get(i));
							}
							
							return multMainEntity_array;
						}
					}
					else{
						//System.out.println("** nextUrl: "+nextUrl);
						//System.out.println("** previousUrl: "+previousUrl);
						
						if(nextUrl != previousUrl){
							System.out.println("Downloading main entities (indefinite pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
							
							previousUrl = currentUrl;
							currentUrl = nextUrl;
						}
						else{
							System.out.println("Downloading main entities (indefinite pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
							
							for(int i = 0; i < currentUrl_array.size(); i++){
								multMainEntity_array.add(currentUrl_array.get(i));
							}
							
							return multMainEntity_array;
						}
					}
						
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}
			}
		}
		else{
			int numPages = Integer.parseInt(nextPageSize);
			
			while(((!(downloadString(currentUrl, nextPageXpath).isEmpty()) || lastPage == false)) && k < numPages){
				if(downloadString(currentUrl, nextPageXpath).isEmpty() && lastPage == false){
					System.out.println("Downloading main entities (several pages | last page)...");
					currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
					lastPage = true;
					
					if(urlType.contains("incomplete")){
						currentUrl_array = completeURLs(currentUrl_array, urlRoot);
					}
					
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}
				else{
					String nextUrl = downloadString(currentUrl, nextPageXpath);
						
					if(urlType.contains("incomplete")){
						//System.out.println("** nextUrl: "+completeURL(nextUrl, urlRoot));
						//System.out.println("** previousUrl: "+previousUrl);
						
						if(!(completeURL(nextUrl, urlRoot).equals(previousUrl))){
							System.out.println("Downloading main entities (several pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);						
							currentUrl_array = completeURLs(currentUrl_array, urlRoot);
							
							previousUrl = currentUrl;
							currentUrl = completeURL(nextUrl, urlRoot);
						}
						else{
							System.out.println("Downloading main entities (several pages | anti-loop)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);						
							currentUrl_array = completeURLs(currentUrl_array, urlRoot);
							
							for(int i = 0; i < currentUrl_array.size(); i++){
								multMainEntity_array.add(currentUrl_array.get(i));
							}
							
							return multMainEntity_array;
						}
					}
					else{
						//System.out.println("** nextUrl: "+nextUrl);
						//System.out.println("** previousUrl: "+previousUrl);
						
						if(nextUrl != previousUrl){
							System.out.println("Downloading main entities (several pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
							
							previousUrl = currentUrl;
							currentUrl = nextUrl;
						}
						else{
							System.out.println("Downloading main entities (several pages)...");
							currentUrl_array = downloadArray(currentUrl, mainEntityXpath, null);
							
							for(int i = 0; i < currentUrl_array.size(); i++){
								multMainEntity_array.add(currentUrl_array.get(i));
							}
							
							return multMainEntity_array;
						}
					}
						
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}
				k++;
			}
		}

		return multMainEntity_array;
	}
	
	/**
	 * Función que realiza las acciones necesarias para la navegación entre páginas que contienen entidades principales
	 * de las cuales se desea descargar información, usando un patrón establecido en la URL para la paginación.
	 * 
	 * @param url, Dirección URL de la entidad principal. Es la especificada en el fichero de configuración.
	 * @param nextPageSize, Variable que determina si se quieren recorrer todas las páginas con entidades o sólo algunas.
	 * @param nextPageRule, URL modificada con la señalización del patrón que se quiere modificar para la paginación.
	 * @param nextPageInitValue, Valor inicial con el que empieza el patrón. Suele ser 0 o 1, dependiendo de la web a examinar.
	 * @param nextPageIncrement, Valor que se va incrementando al valor del patrón.
	 * @param mainEntityXpath, Ruta xPath que contiene la URL de la entidad principal.
	 * @param urlType, Tipo de la URL en referencia a si la web nos la proporciona completa o incompleta.
	 * @param urlRoot, Raíz de la URL para concatenarla a una dirección que está incompleta.
	 * @return Devuelve un array con todas las URLs de las entidades de las que se desea extraer información.
	 */
	
	public ArrayList<String> nextPagesPattern(String url, String nextPageSize, String nextPageRule, String nextPageInitValue, String nextPageIncrement, String mainEntityXpath, String urlType, String urlRoot){
		ArrayList<String> currentUrl_array = new ArrayList<String>();
		ArrayList<String> multMainEntity_array = new ArrayList<String>();
		String currentUrl = nextPageRule, strMatch = "", newUrl = url;		
		int increment = Integer.parseInt(nextPageIncrement);
		int initValue = Integer.parseInt(nextPageInitValue);
		int countValue = initValue;
		Pattern regEx;
		Matcher match;
		boolean lastPage = false;
		int k = 0;
		
		regEx = Pattern.compile("#p#.+#p#");
		match = regEx.matcher(nextPageRule);
		
		while(match.find()){
			strMatch = match.group();
		}
		
		System.out.println("nextPageSize: "+nextPageSize);
		
		if(nextPageSize.contains("all")){
			while(lastPage == false){
				countValue = countValue + increment;
				
				String valueStrCount = Integer.toString(countValue);
				
				String newPatt = strMatch.replace("pattern", valueStrCount);
				String newPattFixed1 = newPatt.replaceAll("#p#", "");
				
				//System.out.println("** new Pattern "+newPatt);
				//System.out.println("** new Pattern fixed "+newPattFixed2);
				
				System.out.println("** current Url: "+newUrl);
				//System.out.println("** xPath: "+mainEntityXpath);
				
				currentUrl_array = downloadArray(newUrl, mainEntityXpath, "pat");
					
				newUrl = currentUrl.replace(strMatch, newPattFixed1);
				
				if(currentUrl_array.get(0).contains("Error404")){
					System.out.println("Last page (pattern) detected!");
					lastPage = true;
				}
				else{
					if(urlType.contains("incomplete")){
						currentUrl_array = completeURLs(currentUrl_array, urlRoot);
					}
					
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}							
			}
		}
		else{
			int numPages = Integer.parseInt(nextPageSize);
			
			while(lastPage == false && k < numPages){
				countValue = countValue + increment;
				
				String valueStrCount = Integer.toString(countValue);
				
				String newPatt = strMatch.replace("pattern", valueStrCount);
				String newPattFixed1 = newPatt.replaceAll("#p#", "");
				
				//System.out.println("** new Pattern "+newPatt);
				//System.out.println("** new Pattern fixed "+newPattFixed2);
				
				System.out.println("** current Url: "+newUrl);
				//System.out.println("** xPath: "+mainEntityXpath);
				
				currentUrl_array = downloadArray(newUrl, mainEntityXpath, "pat");
					
				newUrl = currentUrl.replace(strMatch, newPattFixed1);
				
				if(currentUrl_array.get(0).contains("Error404")){
					System.out.println("Last page (pattern) detected!");
					lastPage = true;
				}
				else{
					if(urlType.contains("incomplete")){
						currentUrl_array = completeURLs(currentUrl_array, urlRoot);
					}
					
					for(int i = 0; i < currentUrl_array.size(); i++){
						multMainEntity_array.add(currentUrl_array.get(i));
					}
				}	
				k++;
			}
		}
	
		return multMainEntity_array;
	}
}