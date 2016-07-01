package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

public class InfoStorer{	
	
	Map<String, String> mapNameURL = new HashMap<String, String>();
	Map<String, String> mapNameURL2 = new HashMap<String, String>();
	Map<String, ArrayList<String>> mapNameComm = new HashMap<String, ArrayList<String>>();
	ArrayList<String> arrayComm = new ArrayList<String>();
	ArrayList<String> arrayParam = new ArrayList<String>();
	ArrayList<String> arrayInfo = new ArrayList<String>();
	public String pName, pURL, xPath1, xPath2;
	String xPathName, xPathURL, xPathPages, xPathComm, xPathNumComm, xPathVal, 
		   xPathTel, xPathAddress, xPathCoord;
	public String mainURL, table_names, table_comm, table_info, namePortal;
	int nPortal, numPagesRest;
	
	public InfoStorer(ArrayList<String> arrayParam){
		this.arrayParam = arrayParam;
	}
	
	public void initializePortalParameters(){
		pName = arrayParam.get(0);
		pURL = arrayParam.get(1);
		xPath1 = arrayParam.get(2);
		xPath2 = arrayParam.get(3);
	}
	
	public void restPagination(){
		int URLNumPatt;
		String URLorigin, URLPatt, patt;
		
		switch(nPortal){
		case 1:
			downloadNumPagesRest(xPathPages);
			
			URLNumPatt = 0;
			URLorigin = mainURL;
			URLPatt = "oa";
			patt = "[[oaxx]]";
			
			for(int i = 0; i < numPagesRest; i++){
				String newPatt = URLPatt+URLNumPatt;
				String URLgen = URLorigin.replace(patt, newPatt);
				
				URLNumPatt = URLNumPatt + 30;
				URLorigin = URLgen;
				patt = newPatt;
				
				downloadNameURL(URLorigin);
			}
			
			break;
		case 2:			
			break;
		case 3:
			downloadNumPagesRest(xPathPages);
			
			URLNumPatt = 0;
			URLorigin = mainURL;
			URLPatt = "start=";
			patt = "[[start=xx]]";
			
			for(int i = 0; i < numPagesRest; i++){
				String newPatt = URLPatt+URLNumPatt;
				String URLgen = URLorigin.replace(patt, newPatt);
				
				URLNumPatt = URLNumPatt + 10;
				URLorigin = URLgen;
				patt = newPatt;
				
				downloadNameURL(URLorigin);
			}

			break;
		default:
			JOptionPane.showMessageDialog(null, "You has not chosen any restaurant!");			
			break;
		}
	}
	
	public void downloadBasicArray(String xPath){
		HTMLParser hP = new HTMLParser(pURL, xPath);
		arrayInfo = hP.downloadAsArray();
	}
	
	public void getBasicArray(){
		for(int i = 0; i < arrayInfo.size(); i++){
			System.out.println("Info: "+arrayInfo.get(i));
		}
	}
	
	public void downloadNameURL(String URL){
		HTMLParser hP = new HTMLParser(URL, xPathName, xPathURL);
        mapNameURL2 = hP.downloadAsMap(namePortal);
        
        for(String key : mapNameURL2.keySet()){
        	if(mapNameURL.containsKey(key)){
        		//System.out.println("***** YA HAY UN RESTAURANTE CON EL NOMBRE "+key+" *****");
        	}
        	else{
        		mapNameURL.put(key, mapNameURL2.get(key));
        	}		    
        }
    }	
	
	public void downloadComm(String key, String URL){		
		HTMLParser hP = new HTMLParser(URL, xPathComm);
        arrayComm = hP.downloadAsArray();
           
        mapNameComm.put(key, arrayComm);
    }
	
	public String downloadNumComm(String key, String URL){
		String str;
		
		HTMLParser hP = new HTMLParser(URL, xPathNumComm);
		str = hP.downloadAsString(namePortal, "numComm");
		
		return str;
	}
	
	public String downloadVal(String key, String URL){
		String str;
		
		HTMLParser hP = new HTMLParser(URL, xPathVal);
		str = hP.downloadAsString(namePortal, "val");
		
		return str;
	}
	
	public String[] downloadAddress(String key, String URL){
		DataFixer dF = new DataFixer(namePortal);
		String[] str = new String[2];
		
		HTMLParser hP = new HTMLParser(URL, xPathAddress);
		str[0] = hP.downloadAsString(namePortal, "address");
		str[1] = dF.fixAddress(str[0]);
		
		return str;
	}
	
	public String downloadTel(String key, String URL){
		String str;
		
		HTMLParser hP = new HTMLParser(URL, xPathTel);
		str = hP.downloadAsString(namePortal, "tel");
		
		return str;
	}
	
	public String downloadCoord(String key, String URL){
		String str;
		
		HTMLParser hP = new HTMLParser(URL, xPathCoord);
		str = hP.downloadAsString(namePortal, "coord");
		
		return str;
	}
	
	public void downloadNumPagesRest(String xPathPages){
		String numPagesStr;		
		HTMLParser hP = new HTMLParser(mainURL, xPathPages);
		
		System.out.println("Counting pages of "+namePortal+"...");
			
		numPagesStr = hP.downloadAsString(namePortal, "numPagesRest");			
		numPagesRest = Integer.parseInt(numPagesStr);
	}
	
	public void displayMapNameURL(){
		if(mapNameURL.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			int n = 1;
			
			for(String key : mapNameURL.keySet()){
			    System.out.println("\nRestaurante "+n+": "+key);
			    
			    n++;
			}
		}
	}
	
	public void displayMapComm(){
		if(mapNameComm.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			for(String key : mapNameComm.keySet()){
			    System.out.println("\nRestaurante: "+key+"\nComment: "+mapNameComm.get(key).get(1));
			}
        }
	}
	
	public void displayMapCommByIndex(String key){
		if(mapNameComm.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			if(mapNameComm.containsKey(key)){
				System.out.println("\nRestaurante: "+key+"\nComment: "+mapNameComm.get(key).get(1));
			}
			else{
				System.out.println("ERROR: There aren't restaurants with this name!");
			}
        }
	}
	
	public void insertNameURL(ConnectDB db){
		DataFixer dF = new DataFixer(namePortal);
		
		for(String key : mapNameURL.keySet()){
			String nameFixed = dF.fixName(key);
			
			db.insertDataTableNames(table_names, key, nameFixed, mapNameURL.get(key));
		}
	}
	
	public void insertComm(ConnectDB db){
		ResultSet rSet = db.getNames(table_names);
		int i = 1;
		
		try{
			while(rSet.next()){
				if(i < 5){ //Sólo los 5 primeros restaurantes, de manera provisional
					//System.out.println("Entrando en datos del restaurante "+i);					
				    if(mapNameURL.containsKey(rSet.getString("nRest"))){
				    	downloadComm(rSet.getString("nRest"), mapNameURL.get(rSet.getString("nRest")));
				    	ArrayList<String> arrayComm = getArrayComm(); //Cogemos solo el array de comentarios actual
				    	
				    	for(String comm : arrayComm){
				    		//System.out.println("Entrando en comentarios del restaurante "+i);
				    		db.insertDataTableComm(table_comm, i, comm);
				    		//System.out.println("Comentario introducido");
						}
					}
				    
				    i++;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void insertInfo(ConnectDB db){
		ResultSet rSet = db.getNames(table_names);
		int i = 1;
		
		try{
			while(rSet.next()){
				if(mapNameURL.containsKey(rSet.getString("nRest"))){			    	
			    	ArrayList<String> arrayVal = new ArrayList<String>();
			    	String[] arrayAddress = downloadAddress(rSet.getString("nRest"),mapNameURL.get(rSet.getString("nRest")));
			    	
			    	arrayVal.add(downloadNumComm(rSet.getString("nRest"),mapNameURL.get(rSet.getString("nRest"))));
			    	arrayVal.add(downloadVal(rSet.getString("nRest"),mapNameURL.get(rSet.getString("nRest"))));
			    	arrayVal.add(arrayAddress[0]);
			    	arrayVal.add(arrayAddress[1]);
			    	arrayVal.add(downloadTel(rSet.getString("nRest"),mapNameURL.get(rSet.getString("nRest"))));
			    	arrayVal.add(downloadCoord(rSet.getString("nRest"),mapNameURL.get(rSet.getString("nRest"))));
			    	
			    	db.insertDataTableInfo(table_info, i, arrayVal.get(0), arrayVal.get(1), arrayVal.get(2), arrayVal.get(3), arrayVal.get(4), arrayVal.get(5));
				}
				
				System.out.println("**"+i+"**: Insertando información del restaurante: "+rSet.getString("nRest"));
				i++;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getMapNameURL(){
		return mapNameURL;
	}
	
	public ArrayList<String> getArrayComm(){
		return arrayComm;
	}
	
	public int getNumPagesRest(){
		return numPagesRest;
	}
	
	public int getNumRestaurants(){
		return getMapNameURL().size();
	}
}