package htmlparser;

import java.util.ArrayList;

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
	
	public InfoOrganizator(String url, ArrayList<String> arrayConfAttributes, ArrayList<String> arrayMainEnt, ArrayList<ArrayList<String>> arrayPredEnt, ArrayList<ArrayList<String>> arrayAtt){
		this.url = url;
		this.arrayConfAttributes = arrayConfAttributes;
		this.arrayMainEnt = arrayMainEnt;
		this.arrayPredEnt = arrayPredEnt;
		this.arrayAtt = arrayAtt;
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
						multMainEnt = iD.completeURLs(multMainEnt, urlType);
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
				
				//System.out.println("multMainEnt size: "+multMainEnt.size());
				
				iD.showArrayData(multMainEnt);
				
				break;
			case "PRED_NEXTPAGE_PATT":
				/*mainEntXPath = arrayMainEnt.get(1);
				
				multMainEnt = iD.downloadArray(url, mainEntXPath, null);

				multMainEnt = iD.completeURLs(multMainEnt, arrayPredEnt.get(i).get(1));
				iD.showArrayData(multMainEnt);*/
				
				break;
			default:
				break;
			}
			
			
		}
		
		/*if(mainEntSize.contains("simple")){
			simpleMEAttExec();
		}
		else{
			multMEAttExec();
		}*/
	}
	
	public void simpleMEAttExec(){
		for(int j = 0; j < arrayAtt.size(); j++){
			String attSize = arrayAtt.get(j).get(2);
			
			if(attSize.contains("simple")){
				simpleAtt = iD.downloadString(url, arrayAtt.get(j).get(1));
				
				iD.showStrData(simpleAtt);
				System.out.println();
			}
			else{
				if(attSize.contains("multiple")){						
					multAtt = iD.downloadArray(url, arrayAtt.get(j).get(1), null);
					
					iD.showArrayData(multAtt);
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
			
			System.out.println("-----------------------------------------------------------\n");
		}
	}
}