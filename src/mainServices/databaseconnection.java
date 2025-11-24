package mainServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {

	private static  String drive="com.mysql.cj.jdbc.Driver";
	private static  String dburl="jdbc:mysql://localhost:3306/spotify";
	private static final String user="root";
	private static final String pass="Rahul";
	
	public static Connection dbconnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(drive);
		return DriverManager.getConnection(dburl,user,pass);
		
	}
}
