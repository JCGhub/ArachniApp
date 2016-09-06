package htmlparser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.ConnectDB;

public class InfoOrganizator{
	ArrayList<String> multMainEnt = new ArrayList<String>();
	ArrayList<String> multAtt = new ArrayList<String>();
	String simpleAtt;
	
	ArrayList<String> arrayMainEnt = new ArrayList<String>();
	ArrayList<String> arrayConfAttributes = new ArrayList<String>();
	ArrayList<ArrayList<String>> arrayPredEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arrayAtt = new ArrayList<ArrayList<String>>();
	String url;
	InfoDownloader iD = new InfoDownloader();
	ConnectDB db;
	
	public InfoOrganizator(String url, ArrayList<String> arrayConfAttributes, ArrayList<String> arrayMainEnt, ArrayList<ArrayList<String>> arrayPredEnt, ArrayList<ArrayList<String>> arrayAtt, ConnectDB db){
		this.url = url;
		this.arrayConfAttributes = arrayConfAttributes;
		this.arrayMainEnt = arrayMainEnt;
		this.arrayPredEnt = arrayPredEnt;
		this.arrayAtt = arrayAtt;
		this.db = db;
	}
	
	public void mainExec(){
		String mainEntSize = arrayMainEnt.get(0);
		String urlType = arrayMainEnt.get(1);
			
		if(mainEntSize.contains("simple")){
			if(arrayPredEnt.isEmpty()){
				simpleMEAttExec();
			}
			else{
				complexExec(mainEntSize);
			}
		}
		else{
			if(mainEntSize.contains("multiple")){
				if(arrayPredEnt.isEmpty()){
					String mainEntXPath = arrayMainEnt.get(2);
				
					multMainEnt = iD.downloadArray(url, mainEntXPath, null);
					
					if(urlType.contains("incomplete")){
						multMainEnt = iD.completeURLs(multMainEnt, arrayMainEnt.get(3));
						iD.showArrayData(multMainEnt);
					}
					else{
						if(urlType.contains("complete")){
							iD.showArrayData(multMainEnt);
						}
						else{
							System.out.println("ERROR: ["+urlType+"] no se corresponde con un formato de url para las entidades.");
						}
					}
				
					multMEAttExec();
				}
				else{
					complexExec(mainEntSize);
				}
			}
			else{
				System.out.println("ERROR: ["+mainEntSize+"] no se corresponde con un tamaño para la entidad principal.");
			}
		}
	}
	
	public void complexExec(String mainEntSize){		
		for(int i = 0; i < arrayPredEnt.size(); i++){
			String opc = arrayPredEnt.get(i).get(0);
			String urlType = arrayMainEnt.get(1);
			String mainEntXPath = arrayMainEnt.get(2);
			String urlRoot = null;
			
			if(urlType.contains("incomplete")){
				urlRoot = arrayMainEnt.get(3);
			}
			
			switch(opc){
			case "PRED_NEXTPAGE_BTN":
				String nextPageXPath = arrayPredEnt.get(i).get(2);
				
				multMainEnt = iD.nextPagesBtn(url, nextPageXPath, mainEntXPath, urlRoot);				
				iD.showArrayData(multMainEnt);
				
				break;
			case "PRED_NEXTPAGE_PATT":
				String urlPatt = arrayPredEnt.get(i).get(2);
				String initValue = arrayPredEnt.get(i).get(3);
				String value = arrayPredEnt.get(i).get(4);
				
				multMainEnt = iD.nextPagesPatt(url, urlPatt, initValue, value, mainEntXPath, urlRoot);				
				iD.showArrayData(multMainEnt);
				
				break;
			default:
				break;
			}			
		}
		
		if(mainEntSize.contains("simple")){
			simpleMEAttExec();
		}
		else{
			multMEAttExec();
		}
	}
	
	public void simpleMEAttExec(){
		for(int j = 0; j < arrayAtt.size(); j++){
			String attSize = arrayAtt.get(j).get(2);
			
			if(attSize.contains("simple")){
				simpleAtt = iD.downloadString(url, arrayAtt.get(j).get(1));
				
				try {
					insertSimpleDataDB(simpleAtt, j);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//iD.showStrData(simpleAtt);
				System.out.println();
			}
			else{
				if(attSize.contains("multiple")){						
					multAtt = iD.downloadArray(url, arrayAtt.get(j).get(1), null);
					
					for(int i = 0; i < multAtt.size(); i++){
						try {
							insertMultipleDataDB(multAtt.get(i), j);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					//iD.showArrayData(multAtt);
					System.out.println();
				}
				else{
					System.out.println("ERROR: ["+attSize+"] no se corresponde con un tamaño para un atributo.");
				}
			}
		}
	}
	
	public void multMEAttExec(){
		for(int i = 0; i < multMainEnt.size(); i++){
			System.out.println("\n***** Entity's Attributes of: "+multMainEnt.get(i)+" *****");
			
			for(int j = 0; j < arrayAtt.size(); j++){
				String attSize = arrayAtt.get(j).get(2);
				
				if(attSize.contains("simple")){
					//System.out.println(arrayAtt.get(j).get(1));
					simpleAtt = iD.downloadString(multMainEnt.get(i), arrayAtt.get(j).get(1));
					
					try {
						insertSimpleDataDB(simpleAtt, j);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					//iD.showStrData(simpleAtt);
					System.out.println();
				}
				else{
					if(attSize.contains("multiple")){						
						multAtt = iD.downloadArray(multMainEnt.get(i), arrayAtt.get(j).get(1), null);
						
						for(int k = 0; k < multAtt.size(); k++){
							try {
								insertMultipleDataDB(multAtt.get(k), j);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
						//iD.showArrayData(multAtt);
						System.out.println();
					}
					else{
						System.out.println("ERROR: ["+attSize+"] no se corresponde con un tamaño para un atributo.");
					}
				}
			}
			
			System.out.println("-----------------------------------------------------------\n");
		}
	}
	
	public void insertSimpleDataDB(String s, int i) throws SQLException{
		int entity = url.hashCode();
		int confFileId = 0, categoryId = 0;
		ResultSet confFileRS = db.getConfFileId(arrayConfAttributes.get(0));
		ResultSet categoryRS = db.getCategoryId(arrayConfAttributes.get(1));
		
		while(confFileRS.next()){
			confFileId = confFileRS.getInt(1);
		}
		
		while(categoryRS.next()){
			categoryId = categoryRS.getInt(1);
		}	
		
		db.insertStringParams(arrayAtt.get(i).get(0), s, entity, arrayMainEnt.get(3), confFileId, categoryId);
	}
	
	public void insertMultipleDataDB(String s, int i) throws SQLException{
		int entity = url.hashCode();
		int confFileId = 0, categoryId = 0;
		ResultSet confFileRS = db.getConfFileId(arrayConfAttributes.get(0));
		ResultSet categoryRS = db.getCategoryId(arrayConfAttributes.get(1));
		
		while(confFileRS.next()){
			confFileId = confFileRS.getInt(1);
		}
		
		while(categoryRS.next()){
			categoryId = categoryRS.getInt(1);
		}
		
		db.insertStringParams(arrayAtt.get(i).get(0), s, entity, arrayMainEnt.get(3), confFileId, categoryId);
	}
}