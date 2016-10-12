package htmlparser;

import database.ConnectDB;

public class Starter{
	
	private XMLReader xR;
	ConnectDB db;
	static String xmlRoute;
	
	public static void main(String[] args){
		if(args.length > 1){
            System.out.println("Error: Too much arguments in console!");
        }else{ 
        	if(args.length == 0){
        		System.out.println("Error: You must specify the name of the file that you want to process!");
        	}
        	else{
        		xmlRoute = args[0];
        	}
        }
	}
}