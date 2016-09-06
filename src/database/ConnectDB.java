package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import javax.swing.JOptionPane;

public class ConnectDB{
	private static Connection Conn;
	 
    public void MySQLConnection(String user, String pass, String db_name){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/"+db_name, user, pass);
            System.out.println("Successful connection with the server!");
        } catch (ClassNotFoundException ex) {
        	System.out.println("ERROR: Unsuccessful connection with the server!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
        	System.out.println("ERROR: Unsuccessful connection with the server!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void closeConnection() {
        try {
            Conn.close();
            System.out.println("Successful disconnection with the server!");
        } catch (SQLException ex) {
        	System.out.println("ERROR: Unsuccessful disconnection with the server!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void createTables() {
        try {
            /*String createDBQuery = "CREATE DATABASE IF NOT EXISTS " + name + " "
            		+ "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;\n";*/
            		
            String catQuery = "CREATE TABLE category "
            		+ "(id int(255) NOT NULL AUTO_INCREMENT, "
            		+ "name varchar(50) NOT NULL, "
            		+ "PRIMARY KEY(id))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            		
            String confQuery = "CREATE TABLE conf_file "
					+ "(id int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(100) NOT NULL, "
					+ "category varchar(50) NOT NULL, "
					+ "PRIMARY KEY(id))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			String intQuery = "CREATE TABLE integer_info "
					+ "(id int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(200) NOT NULL, "
					+ "value bigint(255) DEFAULT NULL, "
					+ "entity bigint(255) NOT NULL, "
					+ "url_root varchar(255) DEFAULT NULL, "
					+ "conf_file int(255) NOT NULL, "
					+ "category int(255) NOT NULL, "
					+ "PRIMARY KEY(id))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			String strQuery = "CREATE TABLE string_info "
					+ "(id int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(200) NOT NULL, "
					+ "value text, "
					+ "entity bigint(255) NOT NULL, "
					+ "url_root varchar(255) DEFAULT NULL, "
					+ "conf_file int(255) NOT NULL, "
					+ "category int(255) NOT NULL, "
					+ "PRIMARY KEY(id))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            
            Statement st = Conn.createStatement();
            //st.executeUpdate(createDBQuery);
            st.executeUpdate(catQuery);
            st.executeUpdate(confQuery);
            st.executeUpdate(intQuery);
            st.executeUpdate(strQuery);
            JOptionPane.showMessageDialog(null, "The tables have been created successfully!");
        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, "ERROR: The tables haven't been created successfully!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertConfFileParams(String name, String category){
    	try {
            String Query = "INSERT INTO conf_file (id, name, category) "
            		+ "VALUES (NULL, '"+name+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Configuration file params have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public void insertCategoryParams(String name){
    	try {
            String Query = "INSERT INTO category (id, name) "
            		+ "VALUES (NULL, '"+name+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Category have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public void insertIntegerParams(String name, int value, int entity, String url_root, int conf_file, int category){
    	try {
            String Query = "INSERT INTO integer_info (id, name, value, entity, url_root, conf_file, category) "
            		+ "VALUES (NULL, '"+name+"', '"+value+"', '"+entity+"', '"+url_root+"', '"+conf_file+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Integer information have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public void insertStringParams(String name, String value, int entity, String url_root, int conf_file, int category){
    	try {
            String Query = "INSERT INTO string_info (id, name, value, entity, url_root, conf_file, category) "
            		+ "VALUES (NULL, '"+name+"', '"+value+"', '"+entity+"', '"+url_root+"', '"+conf_file+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "String information have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public ResultSet getNameFile(){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT name FROM conf_file";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisici�n de datos");
        }
        
        return rSet;
    }
    
    public ResultSet getCategory(){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT name FROM category";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisici�n de datos");
        }
        
        return rSet;
    }
    
    public ResultSet getConfFileId(String name){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT id FROM conf_file WHERE name='"+name+"'";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisici�n de datos");
        }
        
        return rSet;
    }
    
    public ResultSet getCategoryId(String cat){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT id FROM category WHERE name='"+cat+"'";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisici�n de datos");
        }
        
        return rSet;
    }
    
    public void deleteTables() {
        try {
            String Query = "DROP TABLE category, conf_file, integer_info, string_info";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The tables have been deleted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}