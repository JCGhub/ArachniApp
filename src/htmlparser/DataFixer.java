package htmlparser;

import java.util.*;

import javax.swing.JOptionPane;
import java.util.regex.*;

public class DataFixer{
	private String namePortal;
	private String comArt[] = {"el","la","los","las","lo","de","del","the"};
	private String comAddr[] = {"calle","plaza","avenida","c","avda","alameda",
								"callejon","cc","ave","carril","p"};
	
	public DataFixer(String namePortal){
		this.namePortal = namePortal;
	}
	
	public String fixName(String str){
		String str1, str2, str3 = "", res = "";
		Pattern patt;
		Matcher matStr;
		boolean aux = false;		
		
		str1 = str.toLowerCase();			
		str2 = str1.replace(" ", "/");
			
		String[] arrayStr = str2.split("/");
			
		for(int i = 0; i < arrayStr.length; i++){
			for(int j = 0; j < comArt.length; j++){
				patt = Pattern.compile("^"+comArt[j]+"$");
					
				matStr = patt.matcher(arrayStr[i]);
					
				if(matStr.find()){
					aux = true;
				}
			}
				
			if(!aux){
				str3 = str3+"/"+arrayStr[i];
			}
				
			aux = false;
		}
		
		String charIn = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    String charOut = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = str3;
	    
	    for(int i = 0; i < charIn.length(); i++){
	        output = output.replace(charIn.charAt(i), charOut.charAt(i));
	    }
	    
	    res = output;	    
		res = res.substring(1, res.length());
		
		return res;
	}
	
	public String fixURL(String str){
		String res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			res = "https://www.tripadvisor.es" + str;
			
			break;
		case "Yelp":
			res = "https://www.yelp.es" + str;
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixNumPages(String str){
		String str1, str2, res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			str2 = str1.replace("Página1de", "");
			res = str2.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixVal(String str){
		String str1, str2, str3, res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			str2 = str1.replace("Puntuaciónde", "");
			str3 = str2.replace("estrellas", "");
			res = str3.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixAddress(String str){
		String str1, str2, str3, str4, str5 = "", res = "";
		Pattern patt;
		Matcher matStr;
		boolean aux = false;		
		
		str1 = str.toLowerCase();
		
		String charIn = "/.,ºª";
		char charOut = '\0';
	    String output = str1;
	    
	    for(int i = 0; i < charIn.length(); i++){
	        output = output.replace(charIn.charAt(i), charOut);
	    }
	    
	    str2 = output;
	    str3 = str2.replace("  ", "/");
	    str4 = str3.replace(" ", "/");
		
		String[] arrayStr = str4.split("/");
			
		for(int i = 0; i < arrayStr.length; i++){
			for(int j = 0; j < comAddr.length; j++){
				patt = Pattern.compile("^"+comAddr[j]+"$");
					
				matStr = patt.matcher(arrayStr[i]);
					
				if(matStr.find()){
					aux = true;
				}
			}
				
			if(!aux){
				str5 = str5+"/"+arrayStr[i];
			}
				
			aux = false;
		}
		
		String charIn2 = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    String charOut2 = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output2 = str5;
	    
	    for(int i = 0; i < charIn.length(); i++){
	        output2 = output2.replace(charIn2.charAt(i), charOut2.charAt(i));
	    }
	    
	    res = output2;	    
		res = res.substring(1, res.length());
		
		return res;
	}
	
	public String fixTel(String str){
		String str1, str2, str3, res = "";
		Pattern patt;
		Matcher matStr;
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			str2 = str1.replace("\n", "");
			
			String charIn = "+()-?.,";
			char charOut = '\0';
		    String output = str2;
		    
		    for(int i = 0; i < charIn.length(); i++){
		        output = output.replace(charIn.charAt(i), charOut);
		    }
		    
		    str3 = output;
		    
		    patt = Pattern.compile("[0-9]{9}$");			
			matStr = patt.matcher(str3);

			while(matStr.find()){
				res = matStr.group();
			}
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixCoord(String str){
		String str1 = "", str2, str3, res = "";
		Pattern patt;
		Matcher matStr;
		
		switch(namePortal){
		case "TripAdvisor":
			res = str.replace("\n", "");
			
			break;
		case "Yelp":
			patt = Pattern.compile("[\\W]+[0-9]{1,2}\\W[0-9]{6}%2C[\\W][0-9]{1,2}\\W[0-9]{6}");			
			matStr = patt.matcher(str);

			while(matStr.find()){
				str1 = matStr.group();
			}
				
			str2 = str1.replace("=", "");
			str3 = str2.replace("%2C", ", ");
			res = str3.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
}