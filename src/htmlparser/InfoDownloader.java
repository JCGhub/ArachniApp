package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

public class InfoDownloader{
	ArrayList<String> arrayData = new ArrayList<String>();
	String strData;
	
	public InfoDownloader(){}
	
	public ArrayList<String> downloadArray(String url, String xPath, String pred){
		HTMLParser hP = new HTMLParser(url, xPath);
		arrayData = hP.downloadAsArray();
		
		return arrayData;
	}
	
	public String downloadString(String url, String xPath){
		HTMLParser hP = new HTMLParser(url, xPath);
		strData = hP.downloadAsString();
		
		return strData;
	}
	
	public void downloadContent(String url) throws Exception{
		HTMLParser hP = new HTMLParser(url);
        StringBuffer response = hP.downloadContent();        
        System.out.println(response);
	}
	
	public ArrayList<String> completeURLs(ArrayList<String> array, String s){
		ArrayList<String> aAux = new ArrayList<String>();
		
		for(int i = 0; i < array.size(); i++){
			String sAux = array.get(i);
			String compl = s + sAux;
			aAux.add(compl);
		}
		
		return aAux;
	}
	
	public String completeURL(String url, String s){		
		return s+url;
	}
	
	public ArrayList<String> nextPagesBtn(String url, String nextPageXPath, String mainEntXPath, String urlRoot){
		ArrayList<String> currArray = new ArrayList<String>();
		ArrayList<String> multMainEnt = new ArrayList<String>();
		String currUrl = url, prevUrl = "";
		boolean lastPage = false;
		
		while(!(downloadString(currUrl, nextPageXPath).isEmpty()) || lastPage == false){
			if(downloadString(currUrl, nextPageXPath).isEmpty() && lastPage == false){
				currArray = downloadArray(currUrl, mainEntXPath, null);
				lastPage = true;
				
				if(urlRoot != null){
					currArray = completeURLs(currArray, urlRoot);
				}
				
				for(int i = 0; i < currArray.size(); i++){
					multMainEnt.add(currArray.get(i));
				}
			}
			else{
				String nextUrl = downloadString(currUrl, nextPageXPath);
					
				if(urlRoot != null){
					System.out.println("** nextUrl: "+completeURL(nextUrl, urlRoot));
					System.out.println("** prevUrl: "+prevUrl);
					if(!(completeURL(nextUrl, urlRoot).equals(prevUrl))){
						currArray = downloadArray(currUrl, mainEntXPath, null);						
						currArray = completeURLs(currArray, urlRoot);
						
						prevUrl = currUrl;
						currUrl = completeURL(nextUrl, urlRoot);
					}
					else{
						currArray = downloadArray(currUrl, mainEntXPath, null);						
						currArray = completeURLs(currArray, urlRoot);
						
						for(int i = 0; i < currArray.size(); i++){
							multMainEnt.add(currArray.get(i));
						}
						
						return multMainEnt;
					}
				}
				else{
					System.out.println("** nextUrl: "+nextUrl);
					System.out.println("** prevUrl: "+prevUrl);
					if(nextUrl != prevUrl){
						currArray = downloadArray(currUrl, mainEntXPath, null);
						
						prevUrl = currUrl;
						currUrl = nextUrl;
					}
					else{
						currArray = downloadArray(currUrl, mainEntXPath, null);
						
						for(int i = 0; i < currArray.size(); i++){
							multMainEnt.add(currArray.get(i));
						}
						
						return multMainEnt;
					}
				}
					
				for(int i = 0; i < currArray.size(); i++){
					multMainEnt.add(currArray.get(i));
				}
			}
		}

		return multMainEnt;
	}
	
	public ArrayList<String> getArray(){
		return arrayData;
	}
	
	public String getString(){
		return strData;
	}
	
	public void showArrayData(ArrayList<String> array){
		if(!(array == null)){
			for(int i = 0; i < array.size(); i++){
				System.out.println(i+": "+array.get(i));
			}
		}
		else{
			System.out.println("El array está vacío");
		}
	}
	
	public void showStrData(String str){
		if(!(str.isEmpty())){
			System.out.println(str);
		}
		else{
			System.out.println("El string está vacío");
		}
	}
	
	/*public void restPagination(){
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
				
				//downloadNameURL(URLorigin);
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
				
				//downloadNameURL(URLorigin);
			}

			break;
		default:
			JOptionPane.showMessageDialog(null, "You has not chosen any restaurant!");			
			break;
		}
	}*/
	
	/*public void downloadNumPagesRest(String xPathPages){
		String numPagesStr;		
		HTMLParser hP = new HTMLParser(mainURL, xPathPages);
		
		System.out.println("Counting pages of "+namePortal+"...");
			
		numPagesStr = hP.downloadAsString(namePortal, "numPagesRest");			
		numPagesRest = Integer.parseInt(numPagesStr);
	}*/
}