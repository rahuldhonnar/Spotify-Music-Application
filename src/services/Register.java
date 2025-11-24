package services;

import java.util.Scanner;

public class Register {

	static Authentication Auth=new Authentication();
	static Scanner sc=new Scanner(System.in);
	
	public static void register()
	{
		System.out.println("Enter the name ");
		String name=sc.next();
		System.out.println("Enter Email ID ");
		String Email=sc.next();
		System.out.println("create an password ");
		String pass=sc.next();
		Auth.insertdetails(new user(name, pass));
	}
	public  static void login()
	{
		System.out.println("Enter  Username ");
		String name=sc.next();
		System.out.println("Enter  password ");
		String pass=sc.next();
		Auth.authenticat(name, pass);
	}
}
