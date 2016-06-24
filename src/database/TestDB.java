package database;

public class TestDB{
	public static void main(String[] args){
		ConnectDB db = new ConnectDB();
		db.MySQLConnection("root", "", "lookneat_db");
		
		//db.createTableNames("test_table");
		//db.insertDataTableNames("testtable", "El faro", "https://tripadvisor.es/elfaro");
		
		db.closeConnection();
	}
}