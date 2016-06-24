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
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void closeConnection() {
        try {
            Conn.close();
            System.out.println("Successful disconnection with the server!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void createDB(String name) {
        try {
            String Query = "CREATE DATABASE " + name;
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The database " + name + " has been created successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void createTableNames(String name){
        try {
            String Query = "CREATE TABLE "+ name + ""
            		+ "(idRest int(255) NOT NULL AUTO_INCREMENT, "
            		+ "nRest text NOT NULL, "
            		+ "nRestKey text NOT NULL, "
            		+ "urlRest text NOT NULL, "
            		+ "idComm int(255), "
            		+ "idVal int(255), "
            		+ "idInfo int(255), "
            		+ "PRIMARY KEY(idRest))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=latin1";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The table " + name + " has been created successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createTableComm(String name){
        try {
            String Query = "CREATE TABLE "+ name + ""
            		+ "(idComm int(255) NOT NULL AUTO_INCREMENT, "
            		+ "idRest int(255) NOT NULL, "
            		+ "ctext text, "
            		+ "PRIMARY KEY(idComm))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=latin1";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The table " + name + " has been created successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createTableInfo(String name){
        try {
            String Query = "CREATE TABLE "+ name + ""
            		+ "(idInfo int(255) NOT NULL AUTO_INCREMENT, "
            		+ "idRest int(255) NOT NULL, "
            		+ "nComm text, "
            		+ "valoration text, "
            		+ "address text, "
            		+ "addressKey text, "
            		+ "tel text, "
            		+ "coord text, "
            		+ "PRIMARY KEY(idInfo))"
            		+ "ENGINE=InnoDB DEFAULT CHARSET=latin1";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The table " + name + " has been created successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertDataTableNames(String tName, String nRest, String nRestKey, String urlRest){
        try {
            String Query = "INSERT INTO "+tName+" (idRest, nRest, nRestKey, urlRest, idComm, idVal, idInfo) "
            		+ "VALUES (NULL, '"+nRest+"', '"+nRestKey+"', '"+urlRest+"', NULL, NULL, NULL);";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "Data names inserted successfully!");
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos del restaurante "+nRest);
        }
    }
    
    public void insertDataTableComm(String tName, int idRest, String comm){
        try {
            String Query = "INSERT INTO "+tName+" (idComm, idRest, ctext) "
            		+ "VALUES (NULL, '"+idRest+"', '"+comm+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "Data comments inserted successfully!");
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Error en el almacenamiento de comentarios de restaurante");
        }
    }
    
    public void insertDataTableInfo(String tName, int idRest, String nComm, String valoration, String address, String addressKey, String tel, String coord){
        try {
            String Query = "INSERT INTO "+tName+" (idInfo, idRest, nComm, valoration, address, addressKey, tel, coord) "
            		+ "VALUES (NULL, '"+idRest+"', '"+nComm+"', '"+valoration+"', '"+address+"', '"+addressKey+"', '"+tel+"', '"+coord+"');";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            //JOptionPane.showMessageDialog(null, "Data comments inserted successfully!");
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Error en el almacenamiento de comentarios de restaurante");
        }
    }
    
    public ResultSet getNames(String table_name){
    	ResultSet rSet = null;
    	
        try {
            String Query = "SELECT idRest, nRest, urlRest FROM " + table_name;
            
            Statement st = Conn.createStatement();
            rSet = st.executeQuery(Query);
 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisici�n de datos");
        }
        
        return rSet;
    }
    
    public void deleteTable(String name) {
        try {
            String Query = "DROP TABLE "+ name + "";
            
            Statement st = Conn.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "The table " + name + " has been deleted successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}