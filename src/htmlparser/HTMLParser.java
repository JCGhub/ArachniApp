package htmlparser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.apache.commons.lang3.StringEscapeUtils;

import htmlparser.iLogger;
import htmlparser.KeyValue;
import htmlparser.DefaultLogger;

public class HTMLParser{

	public enum Method {GET, POST};
    
	String Url;
	ArrayList<KeyValue> parameters;
	String xPath;
	ArrayList<KeyValue> requestProperties = new ArrayList<KeyValue>();
	ArrayList<KeyValue> responseProperties = new ArrayList<KeyValue>();
	private iLogger logger = null;
	Method method = Method.GET;
	private Document doc = null;
	boolean https = false;
	int connectTimeout = 10000;
	boolean encode = true;
    

	public HTMLParser(String URL) {
        init(URL, Method.GET, new ArrayList<KeyValue>(), new String(), new DefaultLogger());
	}
    
	public HTMLParser(String URL, String xPath) {
		init(URL, Method.GET, new ArrayList<KeyValue>(), xPath, new DefaultLogger());
	}
	
	private void init(String Url, Method method, ArrayList<KeyValue> parameters, String xPath, iLogger logger) {
        this.Url = Url;
        this.parameters = parameters;
        this.xPath = xPath;
        this.setLogger(logger);
        this.method = method;
        
        this.https = Url.toLowerCase().startsWith("https");
    }
	
	public ArrayList<String> downloadAsArray(String flag) {
		ArrayList<String> results = new ArrayList<String>();
		//System.out.println("Url: "+Url+", xPath: "+xPath);
		
		if(!(xPath.isEmpty())){            
			try{
				if((doRequest().toString().contains("Error404")) && flag.contains("pat")){
					results.add("Error404");
					
					return results;
				}
				else{
					if(doRequest().toString().contains("Error404")){
						System.out.println("You've inserted an inexistent URL.");
						
						return results;
					}
					else{
						try{
			                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
			                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			                
			                XPath xpath = XPathFactory.newInstance().newXPath();
			                
			                NodeList nodes = (NodeList) xpath.evaluate(xPath, getDoc(), XPathConstants.NODESET);
			                
			                System.out.println("Array Search results: "+nodes.getLength()+" nodes.");
			                
			                for (int i = 0; i < nodes.getLength(); i++) {
			                	String str = nodes.item(i).getTextContent();
			                    String strCod = StringEscapeUtils.unescapeHtml4(str);
			                    
			                    //System.out.println("Current node value: "+strCod);                    
			                    results.add(strCod);
			                }
			               
			                return results;
			            } catch (XPathExpressionException e) {
			                    e.printStackTrace();
			                    logger.log(e.getMessage());
			            } catch (ParserConfigurationException e) {
			                    e.printStackTrace();
			                    logger.log(e.getMessage());
			            } catch (Exception e) {
			                    e.printStackTrace();
			                    logger.log(e.getMessage());
			            }      
			        }
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
			
			/*
            try{
                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                
                XPath xpath = XPathFactory.newInstance().newXPath();
                
                NodeList nodes = (NodeList) xpath.evaluate(xPath, getDoc(), XPathConstants.NODESET);
                
                System.out.println("Array Search results: "+nodes.getLength()+" nodes.");
                
                for (int i = 0; i < nodes.getLength(); i++) {
                	String str = nodes.item(i).getTextContent();
                    String strCod = StringEscapeUtils.unescapeHtml4(str);
                    
                    //System.out.println("Current node value: "+strCod);                    
                    results.add(strCod);
                }
               
                return results;
            } catch (XPathExpressionException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            }      
        }
        */
			
        return null;     
    }
	
	public String downloadAsString(){
		String result, resultCod = "";
		
		if(!(xPath.isEmpty())){
			//System.out.println("Url: "+Url+", xPath: "+xPath);            
            try {
                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                
                XPath xpath = XPathFactory.newInstance().newXPath();

                NodeList nodes = (NodeList) xpath.evaluate(xPath, getDoc(), XPathConstants.NODESET);
                
                System.out.println("String Search results: "+nodes.getLength()+" nodes.");
                
                for(int i = 0; i < nodes.getLength(); i++) {
                    result = nodes.item(i).getTextContent();
                    resultCod = StringEscapeUtils.unescapeHtml4(result);
                }
                
                //System.out.println("Current node value: "+resultCod);                
                return resultCod;
            } catch (XPathExpressionException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            }            
        }
        return null;     
    }
	
    public StringBuffer downloadContent() throws Exception {
        return doRequest();
    }
    
    public StringBuffer downloadContent(int bytes) throws Exception {
        return doRequest(bytes);
    }
	
	private StringBuffer doRequest() throws Exception {
        return doRequest(-1);
    }
    
    private StringBuffer doRequest(int bytes) throws Exception {
        String urlParameters = getParameters();
        String url = Url;
        
        if(method == Method.GET && !urlParameters.isEmpty()) {
            if(url.endsWith("&") || url.endsWith("?")) {
                url = url + urlParameters;
            } else {
                url = url + "?" + urlParameters;
            }
        }
        
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        
        URL obj = new URL(url);
        
        HttpURLConnection con;
        if(https) {
            con = (HttpsURLConnection) obj.openConnection();
        } else {
            con = (HttpURLConnection) obj.openConnection();
        }
        
        con.setConnectTimeout(connectTimeout);
        con.setReadTimeout(connectTimeout);

        //add request header
        String methodName = "GET";
        if(method == Method.POST) {
            methodName = "POST";
        }
        con.setRequestMethod(methodName);
        
        requestProperties.add(new KeyValue("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"));
        requestProperties.add(new KeyValue("Accept-Language", "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3"));
        requestProperties.add(new KeyValue("Accept", "text/javascript,application/javascript,application/ecmascript,text/html,text/xml,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        requestProperties.add(new KeyValue("Connection", "keep-alive"));
        requestProperties.add(new KeyValue("Content-Type", "application/x-www-form-urlencoded,application/json,application/json;charset=UTF-8"));
        requestProperties.add(new KeyValue("Cache-Control", "no-cache"));
        requestProperties.add(new KeyValue("charset","utf-8"));
        
        for(KeyValue par: requestProperties) {
            con.setRequestProperty(par.key, par.value);
        }    

        if(method == Method.GET) {
            // @todo necesita hacer algo diferente a cuando se trata de un POST?
        } else {
            // Send post request
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }
        }

        logger.log("\nSending "+ methodName +" request to URL : " + url, iLogger.Level.INFO);
        if(method == Method.POST) {
            logger.log("POST parameters : " + urlParameters, iLogger.Level.INFO);
        }
        
        int responseCode = con.getResponseCode();
        
        //***** Si la URL existe (responseCode = 200), devolvemos response como el código html *****
        
        StringBuffer response = new StringBuffer();
        
        if(responseCode != 200){
        	//System.out.println("La url NO existe");
        	response.append("Error404");
        	
        	return response;
        }
        
	    logger.log("Response: " + responseCode, iLogger.Level.INFO);
	
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	        
	    if(bytes < 0) {        
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine + "\n");
	            //logger.log(inputLine);
	        }
	    } else {
	        char[] array = new char[bytes];            
	        int read = in.read(array);
	        response.append(array);
	            
	        if(read < bytes) {
	            logger.log("Waring: expecting " + bytes + " bytes at least but received " + read, iLogger.Level.INFO);
	        }
	    }
	        
	    in.close();
	        
	    logger.log("\nResponse header:", iLogger.Level.DEBUG);
	    int i = 0;
	    while(con.getHeaderField(i) != null) {
	        responseProperties.add(new KeyValue(con.getHeaderFieldKey(i), con.getHeaderField(i)));
	        logger.log("\n"+ con.getHeaderFieldKey(i) +": " + con.getHeaderField(i), iLogger.Level.DEBUG);
	        ++i;
	    }
	        
	    logger.log("\n" + response, iLogger.Level.DEBUG);
        /*}
        else{
        	response.append("");
        	JOptionPane.showMessageDialog(null, "404 Error! Page not found!\nYou has tried to connect to an non-existent URL!\n\n("+url+")");
        }*/
        
        return response;
    }
    
    public String getParameters() {
        return parametersToString(parameters, encode);
    }
    
    public static String parametersToString(ArrayList<KeyValue> parameters) {
        return parametersToString(parameters, true);
    }
    
    public static String parametersToString(ArrayList<KeyValue> parameters, boolean encode) {
        String urlParameters = "";
        if(encode) {
            for(KeyValue par: parameters) {
                urlParameters += "&" + par.key + "=" + par.value;
            }
        } else {
            for(KeyValue par: parameters) {
                try {
					urlParameters += "&" + URLEncoder.encode(par.key, "UTF-8") + "=" + URLEncoder.encode(par.value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("Unsupported Encoding");
					e.printStackTrace();
				}
            }
        }
        
        if(!urlParameters.isEmpty()) {
            urlParameters = urlParameters.substring(1, urlParameters.length());
        }
        
        return urlParameters;
    }
    
    public iLogger getLogger() {
        return logger;
    }
    
    public void setLogger(iLogger logger) {
        this.logger = logger;
    }
	
	public Document getDoc() {
        return doc;
    }
}