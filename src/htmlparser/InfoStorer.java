package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;

public class InfoStorer{
	ArrayList<String> arrayData = new ArrayList<String>();
	ArrayList<String> arrayMainEnt = new ArrayList<String>();
	public String strData, url;	
	XMLReader xR;
	ArrayList<String> arrayConfAtt = new ArrayList<String>();
	ArrayList<ArrayList<String>> arraySecEnt = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> arrayAtt = new ArrayList<ArrayList<String>>();
	
	public InfoStorer(XMLReader xR){
		this.xR = xR;
		
		arrayConfAtt = xR.getConfAtt();
		url = xR.getURL();
		arrayMainEnt = xR.getMainEnt();
		arraySecEnt = xR.getSecEnt();
		arrayAtt = xR.getAtt();
	}
	
	public void downloadArray(String xPath){
		HTMLParser hP = new HTMLParser(url, xPath);
		arrayData = hP.downloadAsArray();
	}
	
	public void downloadString(String xPath){
		HTMLParser hP = new HTMLParser(url, xPath);
		strData = hP.downloadAsString();
	}
	
	public ArrayList<String> getArray(){
		return arrayData;
	}
	
	public String getXPathMainEnt(){
		return arrayMainEnt.get(1);
	}
	
	public void showArray(){
		for(int i = 0; i < arrayData.size(); i++){
			System.out.println(i+": "+arrayData.get(i));
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