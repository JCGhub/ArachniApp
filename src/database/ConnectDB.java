package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            		+ "(id_cat int(255) NOT NULL AUTO_INCREMENT, "
            		+ "name varchar(50) NOT NULL, "
            		+ "PRIMARY KEY(id_cat))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            
            String catDefaultData = "INSERT INTO category (id_cat, name) VALUES "
            		+ "(1, 'Cookery'), "
            		+ "(2, 'News'), "
            		+ "(3, 'Computing'), "
            		+ "(4, 'Sports'), "
            		+ "(5, 'Academic'), "
            		+ "(6, 'Science'), "
            		+ "(7, 'Investigation'), "
            		+ "(8, 'Administration'), "
            		+ "(9, 'Entertainment'), "
            		+ "(10, 'Hostelry'), "
            		+ "(11, 'Others');";
            
            String webPortalQuery = "CREATE TABLE web_portal "
            		+ "(id_wp int(255) NOT NULL AUTO_INCREMENT, "
            		+ "name varchar(255) NOT NULL, "
            		+ "PRIMARY KEY(id_wp))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            		
            String confQuery = "CREATE TABLE conf_file "
					+ "(id_cf int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(100) NOT NULL, "
					+ "id_wp int(255) NOT NULL, "
					+ "id_cat int(255) NOT NULL, "
					+ "file_date datetime NOT NULL,"
					+ "PRIMARY KEY(id_cf),"
					+ "FOREIGN KEY(id_wp) REFERENCES web_portal(id_wp),"
					+ "FOREIGN KEY(id_cat) REFERENCES category(id_cat))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			String intQuery = "CREATE TABLE integer_info "
					+ "(id_inti int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(200) NOT NULL, "
					+ "value bigint(255) NOT NULL, "
					+ "entity bigint(255) NOT NULL, "
					+ "id_wp int(255) DEFAULT NULL, "
					+ "id_cf int(255) NOT NULL, "
					+ "id_cat int(255) NOT NULL, "
					+ "PRIMARY KEY(id_inti),"
					+ "FOREIGN KEY(id_wp) REFERENCES web_portal(id_wp),"
					+ "FOREIGN KEY(id_cf) REFERENCES conf_file(id_cf),"
					+ "FOREIGN KEY(id_cat) REFERENCES category(id_cat))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			String strQuery = "CREATE TABLE string_info "
					+ "(id_stri int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(200) NOT NULL, "
					+ "value text NOT NULL, "
					+ "entity bigint(255) NOT NULL, "
					+ "date datetime NOT NULL,"
					+ "id_wp int(255) DEFAULT NULL, "
					+ "id_cf int(255) NOT NULL, "
					+ "id_cat int(255) NOT NULL, "
					+ "PRIMARY KEY(id_stri),"
					+ "FOREIGN KEY(id_wp) REFERENCES web_portal(id_wp),"
					+ "FOREIGN KEY(id_cf) REFERENCES conf_file(id_cf),"
					+ "FOREIGN KEY(id_cat) REFERENCES category(id_cat))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            
            Statement st = Conn.createStatement();
            //st.executeUpdate(createDBQuery);
            st.executeUpdate(catQuery);
            st.executeUpdate(catDefaultData);
            st.executeUpdate(webPortalQuery);
            st.executeUpdate(confQuery);
            st.executeUpdate(intQuery);
            st.executeUpdate(strQuery);
            JOptionPane.showMessageDialog(null, "The tables have been created successfully!");
        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, "ERROR: The tables haven't been created successfully!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertConfFileParameters(String name, int web_portal, int category, String date){
    	try {
            String Query = "INSERT INTO conf_file (id_cf, name, id_wp, id_cat, file_date) "
            		+ "VALUES (NULL, '"+name+"', '"+web_portal+"', '"+category+"', '"+date+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Configuration file params have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public void insertWebPortalParameters(String name){
    	try {
            String Query = "INSERT INTO web_portal (id_wp, name) "
            		+ "VALUES (NULL, '"+name+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Configuration file params have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted successfully!");
        }
    }
    
    public void insertCategoryParams(String name){
    	try {
            String Query = "INSERT INTO category (id_cat, name) "
            		+ "VALUES (NULL, '"+name+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "Category have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Category hasn't been inserted successfully!");
        }
    }
    
    public void insertIntegerParams(String name, int value, int entity, int web_portal, int conf_file, int category){
    	try {
            String Query = "INSERT INTO integer_info (id_inti, name, value, entity, id_wp, id_cf, id_cat) "
            		+ "VALUES (NULL, '"+name+"', '"+value+"', '"+entity+"', '"+web_portal+"', '"+conf_file+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "Integer information have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Integer information haven't been inserted successfully!");
        }
    }
    
    public void insertStringParams(String name, String value, int entity, String date, int web_portal, int conf_file, int category){
    	try {
            String Query = "INSERT INTO string_info (id_stri, name, value, entity, date, id_wp, id_cf, id_cat) "
            		+ "VALUES (NULL, '"+name+"', '"+value+"', '"+entity+"', '"+date+"', '"+web_portal+"', '"+conf_file+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "String information have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: String information haven't been inserted successfully!");
        }
    }
    
    public void deleteStringParamsByConfFile(int confFileId){
    	try {
            String Query = "DELETE FROM string_info WHERE id_cf = '"+confFileId+"'";;
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "String information have been inserted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: String information haven't been inserted successfully!");
        }
    }
    
    public ResultSet getNameConfFile(){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT name FROM conf_file";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
        }
        
        return rSet;
    }
    
    public ResultSet getWebPortal(){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT name FROM web_portal";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
        }
        
        return rSet;
    }
    
    public ResultSet getConfFileId(String name){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT id_cf FROM conf_file WHERE name='"+name+"'";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
        }
        
        return rSet;
    }
    
    public ResultSet getWebPortalId(String name){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT id_wp FROM web_portal WHERE name='"+name+"'";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
        }
        
        return rSet;
    }
    
    public ResultSet getCategoryId(String cat){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT id_cat FROM category WHERE name='"+cat+"'";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting information has failed!");
        }
        
        return rSet;
    }
    
    public void deleteTables() {
        try {
            String Query = "DROP TABLE category, conf_file, integer_info, string_info, web_portal";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The tables have been deleted successfully!");
        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, "ERROR: The tables haven't been deleted successfully!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}