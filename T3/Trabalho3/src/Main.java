import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import views.LoginView;

public class Main {
	public static void main (String[] args) throws ClassNotFoundException {
		
		DBManager db = new DBManager();
		List<HashMap<String,Object>> obj = db.selectFromDb("SELECT * from Mensagem");
		System.out.println(obj);
		

		new LoginView();		
	}
	
	
}
