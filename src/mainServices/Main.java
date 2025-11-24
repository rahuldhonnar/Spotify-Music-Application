package mainServices;

import java.util.Scanner;
import services.Register;
import services.songsList;

public class Main {

	static Register regi=new Register();
	static songsList songlist=new songsList();
	static Scanner sc=new Scanner(System.in);

	public static void main(String[] args) {
		boolean flag=true;
		System.out.println(" Welcome to spotify ");
		while(flag) {
			System.out.println("\n1 register user \n2 Login ");
			System.out.println("Enter your Choice ");
			int Choice=sc.nextInt();
			switch (Choice) {
			case 1->{
				regi.register();
			}
			case 2->{
				regi.login();
			}
			default->{
				System.out.println("Invalid Details ");
			}
			}  
		}

	}

}
