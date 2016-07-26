package htmlparser;

import java.util.ArrayList;

public class InfoOrganizator{
	ArrayList<String> multMainEnt = new ArrayList<String>();
	ArrayList<String> multAtt = new ArrayList<String>();
	String simpleAtt;
	
	ArrayList<String> arrayMainEnt = new ArrayList<String>();
	ArrayList<String> arrayConfAttributes = new ArrayList<String>();
	ArrayList<ArrayList<String>> arrayPredEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arraySecEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arrayAtt = new ArrayList<ArrayList<String>>();
	String url;
	InfoDownloader iD = new InfoDownloader();
	
	public InfoOrganizator(String url, ArrayList<String> arrayConfAttributes, ArrayList<String> arrayMainEnt, ArrayList<ArrayList<String>> arrayPredEnt, ArrayList<ArrayList<String>> arraySecEnt, ArrayList<ArrayList<String>> arrayAtt){
		this.url = url;
		this.arrayConfAttributes = arrayConfAttributes;
		this.arrayMainEnt = arrayMainEnt;
		this.arrayPredEnt = arrayPredEnt;
		this.arraySecEnt = arraySecEnt;
		this.arrayAtt = arrayAtt;
	}
	
	public void mainExec(){
		String mainEntSize = arrayMainEnt.get(0);
			
		if(mainEntSize.contains("simple")){
			if(arrayPredEnt.isEmpty() && arraySecEnt.isEmpty()){
				AttExec();
			}
			else{
				complexExec();
			}
		}
		else{
			if(mainEntSize.contains("multiple")){
				if(arrayPredEnt.isEmpty() && arraySecEnt.isEmpty()){
					String mainEntXPath = arrayMainEnt.get(1);
				
					multMainEnt = iD.downloadArray(url, mainEntXPath, null);
					iD.showArrayData(multMainEnt);
				
					AttExec();
				}
				else{
					complexExec();
				}
			}
			else{
				System.out.println("ERROR: ["+mainEntSize+"] no se corresponde con un tamaño para la entidad principal.");
			}
		}
	}
	
	public void complexExec(){
		for(int i = 0; i < arrayPredEnt.size(); i++){
			if(arrayPredEnt.get(i).get(2).contains("PRED_INCOMPLETE_URL")){
				String mainEntXPath = arrayMainEnt.get(1);
				
				multMainEnt = iD.downloadArray(url, mainEntXPath, null);

				multMainEnt = iD.completeURLs(multMainEnt, arrayPredEnt.get(i).get(1));
				iD.showArrayData(multMainEnt);
				
				AttExec();
			}
		}
	}
	
	public void AttExec(){
		for(int i = 0; i < multMainEnt.size(); i++){
			System.out.println("***** Entity's Attributes of: "+multMainEnt.get(i)+" *****");
			
			for(int j = 0; j < arrayAtt.size(); j++){
				String attSize = arrayAtt.get(j).get(2);
				
				if(attSize.contains("simple")){
					simpleAtt = iD.downloadString(multMainEnt.get(i), arrayAtt.get(j).get(1));
					
					iD.showStrData(simpleAtt);
					System.out.println();
				}
				else{
					if(attSize.contains("multiple")){						
						multAtt = iD.downloadArray(multMainEnt.get(i), arrayAtt.get(j).get(1), null);
						
						iD.showArrayData(multAtt);
						System.out.println();
					}
					else{
						System.out.println("ERROR: ["+attSize+"] no se corresponde con un tamaño para un atributo.");
					}
				}
			}
			
			System.out.println("-----------------------------------------------------------");
		}
	}
}