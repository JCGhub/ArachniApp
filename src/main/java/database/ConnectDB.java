package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * @author Juanca
 *
 * Clase que contiene las funciones necesarias para la gestión y manipulación de la base de datos de la aplicación.
 * 
 */

public class ConnectDB{
	Connection Conn;
	Statement statement;
	public static ConnectDB db;

	private ConnectDB(){
		String url= "jdbc:mysql://127.0.0.1/";
		String dbName = "arachniappdb";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";

		try{
			Class.forName(driver).newInstance();
			this.Conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
		}
		catch(Exception sqle){
			sqle.printStackTrace();
		}
	}

	public static ConnectDB getInstance(){
		if(db == null){
			db = new ConnectDB();
			System.out.println("Successful connection with the server!");
		}
		else{
			System.out.println("You can't create another instance of ConnectDB!");
		}

		return db;
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

	public void insertConfFileParameters(String name, int web_portal, int category, String date){
		try {
			String Query = "INSERT INTO conf_file (id_cf, name, id_wp, id_cat, file_date) "
					+ "VALUES (NULL, '"+name+"', '"+web_portal+"', '"+category+"', '"+date+"');";

			statement = db.Conn.createStatement();
			statement.executeUpdate(Query);

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

			statement = db.Conn.createStatement();
			statement.executeUpdate(Query);
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

			statement = db.Conn.createStatement();
			statement.executeUpdate(Query);
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
		try {
			String Query = "INSERT INTO query (id_query, name, value, id_cf, id_role, id_user) "
					+ "VALUES (NULL, '"+name+"', '"+query+"', '"+confFileId+"', '"+roleId+"', '"+userId+"');";

			statement = db.Conn.createStatement();
			statement.executeUpdate(Query);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "ERROR: Default query haven't been inserted!",
					"DATA ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public ResultSet existsDefaultQuery(int id_cf){
		ResultSet rSet = null;

		try {
			String Query = "SELECT name FROM query WHERE id_cf='"+id_cf+"'";

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "ERROR: Getting conf. file name has failed!",
					"CONFIGURATION FILE ERROR", JOptionPane.ERROR_MESSAGE);
		}

		return rSet;
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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

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

			statement = db.Conn.createStatement();
			rSet = statement.executeQuery(Query);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "ERROR: Getting category identificator has failed!",
					"CATEGORY ERROR", JOptionPane.ERROR_MESSAGE);
		}

		return rSet;
	}
}