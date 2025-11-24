package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mainServices.databaseconnection;

public class Authentication {

	static FirstInterface firstpage=new FirstInterface();

	public void insertdetails(user us) {
		
		try {
			Connection connection= databaseconnection.dbconnection();
			String Query="insert into login values(?,?)";
			PreparedStatement statement=connection.prepareStatement(Query);
			statement.setString(1, us.getUsername());
			statement.setString(2, us.getPassword());

			int result=statement.executeUpdate();
			if(result>0)
			{
				System.out.println("user Register Succesfully ");
			}else {
				System.out.println("Failed to register user  ");
			}
			} catch (ClassNotFoundException e) {
				System.out.println("User allredy register login using username and password ");
			e.printStackTrace();
		} catch (SQLException e) {
	        System.out.println("An error occurred during registration.");
			e.printStackTrace();
		}
	}


	public void authenticat(String name, String pass) {
		
		try {
			Connection connection= databaseconnection.dbconnection();
		    String query = "SELECT * FROM login WHERE username = ? AND password = ?";

			PreparedStatement statement=connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, pass);
			ResultSet set=statement.executeQuery();
			if(set.next())
			{
				 System.out.println("Login Succesfully ");
				 System.out.println();
				 System.out.println();
				 firstpage.FirstPage();
			}
			else {
				 System.out.println(" Details mismatch, try again.");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}
}
