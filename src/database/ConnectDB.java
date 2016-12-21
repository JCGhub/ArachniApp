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
        	System.out.println("ERROR 1: Unsuccessful connection with the server! "+ex);
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
        	System.out.println("ERROR 2: Unsuccessful connection with the server! "+ex);
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
            		+ "name varchar(100) NOT NULL, "
            		+ "PRIMARY KEY(id_wp))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            		
            String confQuery = "CREATE TABLE conf_file "
					+ "(id_cf int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(100) NOT NULL, "
					+ "id_wp int(255) NOT NULL, "
					+ "id_cat int(255) NOT NULL, "
					+ "file_date date NOT NULL,"
					+ "PRIMARY KEY(id_cf),"
					+ "CONSTRAINT conf_file_ibfk_1 FOREIGN KEY(id_wp) REFERENCES web_portal(id_wp) ON DELETE restrict ON UPDATE restrict,"
					+ "CONSTRAINT conf_file_ibfk_2 FOREIGN KEY(id_cat) REFERENCES category(id_cat) ON DELETE restrict ON UPDATE restrict)"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
					
			String strQuery = "CREATE TABLE string_info "
					+ "(id_str int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(100) NOT NULL, "
					+ "value text NOT NULL, "
					+ "entity bigint(255) NOT NULL, "
					+ "date datetime NOT NULL,"
					+ "id_wp int(255) NOT NULL, "
					+ "id_cf int(255) NOT NULL, "
					+ "id_cat int(255) NOT NULL, "
					+ "PRIMARY KEY(id_str),"
					+ "CONSTRAINT string_info_ibfk_1 FOREIGN KEY(id_wp) REFERENCES web_portal(id_wp) ON DELETE restrict ON UPDATE restrict,"
					+ "CONSTRAINT string_info_ibfk_2 FOREIGN KEY(id_cf) REFERENCES conf_file(id_cf) ON DELETE restrict ON UPDATE restrict,"
					+ "CONSTRAINT string_info_ibfk_3 FOREIGN KEY(id_cat) REFERENCES category(id_cat) ON DELETE restrict ON UPDATE restrict)"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			/*String graphicQuery = "CREATE TABLE graphic "
            		+ "(id_graph int(255) NOT NULL AUTO_INCREMENT, "
            		+ "name varchar(50) NOT NULL, "
            		+ "PRIMARY KEY(id_graph))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			String graphicDefaultData = "INSERT INTO graphic (id_graph, name) VALUES "
            		+ "(1, 'Text'), "
            		+ "(2, 'Bars'), "
            		+ "(3, 'Pie');";*/
			
			String roleQuery = "CREATE TABLE role "
            		+ "(id_role int(255) NOT NULL AUTO_INCREMENT, "
            		+ "name varchar(50) NOT NULL, "
            		+ "PRIMARY KEY(id_role))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			String roleDefaultData = "INSERT INTO role (id_role, name) VALUES "
            		+ "(1, 'Standard'), "
            		+ "(2, 'Maker'), "
            		+ "(3, 'Supermaker');";
			
			String userQuery = "CREATE TABLE user "
					+ "(id_user int(255) NOT NULL AUTO_INCREMENT, "
					+ "user_name varchar(100) NOT NULL, "
					+ "password varchar(100) NOT NULL, "
					+ "email varchar(100) NOT NULL, "
					+ "name varchar(100) NOT NULL, "
					+ "surname varchar(150) NOT NULL, "
					+ "id_role int(255) NOT NULL, "
					+ "PRIMARY KEY(id_user),"
					+ "CONSTRAINT user_ibfk_1 FOREIGN KEY(id_role) REFERENCES role(id_role) ON DELETE restrict ON UPDATE restrict)"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			String queriesQuery = "CREATE TABLE query "
					+ "(id_query int(255) NOT NULL AUTO_INCREMENT, "
					+ "name varchar(100) NOT NULL, "
					+ "value text NOT NULL, "
					+ "id_cf int(255) NOT NULL, "
					+ "id_role int(255) NOT NULL, "
					+ "id_user int(255) NOT NULL, "
					+ "PRIMARY KEY(id_query),"
					+ "CONSTRAINT query_ibfk_1 FOREIGN KEY(id_cf) REFERENCES conf_file(id_cf) ON DELETE restrict ON UPDATE restrict,"
					+ "CONSTRAINT query_ibfk_2 FOREIGN KEY(id_role) REFERENCES role(id_role) ON DELETE restrict ON UPDATE restrict,"
					+ "CONSTRAINT query_ibfk_3 FOREIGN KEY(id_user) REFERENCES user(id_user) ON DELETE restrict ON UPDATE restrict)"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;";			
            
            Statement st = Conn.createStatement();
            //st.executeUpdate(createDBQuery);
            st.executeUpdate(catQuery);
            st.executeUpdate(catDefaultData);
            st.executeUpdate(webPortalQuery);
            st.executeUpdate(confQuery);
            st.executeUpdate(strQuery);
            //st.executeUpdate(graphicQuery);
            //st.executeUpdate(graphicDefaultData);
            st.executeUpdate(roleQuery);
            st.executeUpdate(roleDefaultData);
            st.executeUpdate(userQuery);
            st.executeUpdate(queriesQuery);
            
            JOptionPane.showMessageDialog(null, "The tables have been created SUCCESSFULLY!");
        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, "ERROR: The tables haven't been created!",
            	    "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertConfFileParameters(String name, int web_portal, int category, String date){
    	try {
            String Query = "INSERT INTO conf_file (id_cf, name, id_wp, id_cat, file_date) "
            		+ "VALUES (NULL, '"+name+"', '"+web_portal+"', '"+category+"', '"+date+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            
            JOptionPane.showMessageDialog(null, "Conf. file parameters have been inserted SUCCESSFULLY!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted!",
            	    "CONFIGURATION FILE ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void insertWebPortalParameters(String name){
    	try {
            String Query = "INSERT INTO web_portal (id_wp, name) "
            		+ "VALUES (NULL, '"+name+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Web portal parameters have been inserted SUCCESSFULLY!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Configuration file params haven't been inserted!",
            	    "WEB PORTAL ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void insertStringParams(String name, String value, int entity, String date, int web_portal, int conf_file, int category){
    	try {
            String Query = "INSERT INTO string_info (id_str, name, value, entity, date, id_wp, id_cf, id_cat) "
            		+ "VALUES (NULL, '"+name+"', '"+value+"', '"+entity+"', '"+date+"', '"+web_portal+"', '"+conf_file+"', '"+category+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
        	Thread t = new Thread(new Runnable(){
                public void run(){
                    JOptionPane.showMessageDialog(null, "Warning: Information haven't been inserted!",
                    	    "DATA WARNING", JOptionPane.WARNING_MESSAGE);
                }
            });
        	t.start();
        }
    }
    
    public void insertDefaultQuery(String name, String query, int confFileId, int roleId, int userId){
    	System.out.println(name+", "+query+", "+confFileId+", "+roleId+", "+userId);
    	try {
            String Query = "INSERT INTO query (id_query, name, value, id_cf, id_role, id_user) "
            		+ "VALUES (NULL, '"+name+"', '"+query+"', '"+confFileId+"', '"+roleId+"', '"+userId+"');";
            
            System.out.println("\n"+Query);
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Default query haven't been inserted!",
                    "DATA ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteStringParamsByConfFile(int confFileId){
    	try {
            String Query = "DELETE FROM string_info WHERE id_cf = '"+confFileId+"'";;
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Information haven't been deleted!",
            	    "DATA ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public ResultSet getNameConfFile(){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT name FROM conf_file";
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: Getting conf. file name has failed!",
            	    "CONFIGURATION FILE ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting web portal has failed!",
            	    "WEB PORTAL ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting category has failed!",
            	    "CATEGORY ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting conf. file identificator has failed!",
            	    "CONFIGURATION FILE ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting web portal identificator has failed!",
            	    "WEB PORTAL ERROR", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "ERROR: Getting category identificator has failed!",
            	    "CATEGORY ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        return rSet;
    }
    
    /*public void deleteTables() {
        try {
            String Query = "DROP TABLE category, conf_file, integer_info, string_info, web_portal";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The tables have been deleted successfully!");
        } catch (SQLException ex) {
        	JOptionPane.showMessageDialog(null, "ERROR: The tables haven't been deleted successfully!");
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}