package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mainServices.databaseconnection;



public class songsList {

	static Scanner sc=new Scanner(System.in);
	static List<Song> songs=new ArrayList<Song>();
	static List<Song> Downloadedsongs=new ArrayList<Song>();
	static int currentUserId = 1;
	static boolean loaded = false;

	public static final void AllSong()
	{

		try {
			Connection connection=mainServices.databaseconnection.dbconnection();
			String query="select *from songs";
			PreparedStatement statement=connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
            
			boolean found = false;
			System.out.println("====================================================================================================");
			System.out.println("||			                   All Songs				                         || ");
			System.out.println("====================================================================================================");

			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.printf("| %-5s | %-30s | %-25s | %-15s | %-10s |%n",
					"ID", "Title", "Artist", "Genre", "Duration");		
			System.out.println("-----------------------------------------------------------------------------------------------------");

			while (resultSet.next()) {
				found = true;		     

				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String artist = resultSet.getString("artist");
				String genre = resultSet.getString("genre");
				String duration = resultSet.getString("duration");



				System.out.printf("| %-5d | %-30s | %-25s | %-15s | %-10s |%n",
						id, title, artist, genre, duration);
				System.out.println("-----------------------------------------------------------------------------------------------------");

			}
			if (!found) {
				System.out.println("songs  records not found.");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public Song findsonginlist(String name)
	{
		Song song = null;
		try {
			Connection connection=databaseconnection.dbconnection();
			String query = "SELECT * FROM songs WHERE LOWER(title) LIKE LOWER(?)";
			PreparedStatement statement=connection.prepareStatement(query);
			statement.setString(1, "%" + name.trim() + "%");


			ResultSet resultSet = statement.executeQuery();

			while(resultSet.next())
			{
				System.out.println("-----------------------------------------------------------------------------------------------------");

				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String artist = resultSet.getString("artist");
				String genre = resultSet.getString("genre");
				String duration = resultSet.getString("duration");


				System.out.println("-----------------------------------------------------------------------------------------------------");
				System.out.printf("| %-5d | %-30s | %-25s | %-15s | %-10s |%n",
						id, title, artist, genre, duration);
				System.out.println("-----------------------------------------------------------------------------------------------------");
			}
			boolean found=true;
			if(!found)
			{
				System.out.println("Song not Found ");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return song;
	}
	public void playSong(String name)
	{
		boolean found=true;

		try {
			Connection connection=databaseconnection.dbconnection();
			String query = "SELECT * FROM songs WHERE LOWER(title) = LOWER(?)";

			PreparedStatement statement=connection.prepareStatement(query);

			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();

			if(resultSet.next())
			{
				System.out.println("Loading song ");
				try {
					Thread.sleep(2000);
					System.out.println("          >||<        ");
					String title = resultSet.getString("title");

					String duration = resultSet.getString("duration");

					System.out.println("-------------------------------------------------------");
					System.out.println(" Now Playing song : " + name + " (" +duration+"  min )" );
					System.out.println("--------------------------------------------------------");

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(!found)
			{
				System.out.println("Song not Found ");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void filterbyArtist(String name)
	{
		try {
			Connection connection=databaseconnection.dbconnection();
			String query="select * from songs where artist=?";
			PreparedStatement statement=connection.prepareStatement(query);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String artist = resultSet.getString("artist");

				System.out.println (id+"  :"+title +"   : "+artist);
			}
			boolean found=true;
			if(!found)
			{
				System.out.println("Artist not found "+name);
			}
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void filterbygener(String name)
	{
		try {
			Connection connection=databaseconnection.dbconnection();
			String query="select * from songs where gener=?";
			PreparedStatement statement=connection.prepareStatement(query);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String gener = resultSet.getString("gener");

				System.out.println (id+"  :"+title +"   : "+gener);
			}
			boolean found=true;
			if(!found)
			{
				System.out.println("Artist not found "+name);
			}
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void filterDuration() {
		System.out.println("Enter minimum duration:");
		double min = sc.nextDouble();
		System.out.println("Enter maximum duration:");
		double max = sc.nextDouble();
		try {
			Connection connection=databaseconnection.dbconnection();
			String query = "SELECT * FROM songs WHERE duration BETWEEN ? AND ?";
			PreparedStatement statement=connection.prepareStatement(query);

			statement.setDouble(1, min);
			statement.setDouble(2, max);

			ResultSet resultSet = statement.executeQuery();

			boolean found = false;

			System.out.printf("%-5s | %-30s | %-20s | %-10s | %-10s%n", 
					"ID", "Title", "Artist", "Genre", "Duration");
			System.out.println("--------------------------------------------------------------------------");

			while (resultSet.next()) {
				found = true;

				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String artist = resultSet.getString("artist");
				String genre = resultSet.getString("genre");
				double duration = resultSet.getDouble("duration");

				System.out.println("-----------------------------------------------------------------------------");
				System.out.printf("%-5s %-25s %-20s %-15s %-10s%n", "ID", "Title", "Artist", "Genre", "Duration");
				System.out.println("------------------------------------------------------------------------------");

			}

			if (!found) {
				System.out.println("❌ No songs found between " + min + " and " + max+ " minutes.");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void downloadSong() {
		System.out.print("Enter the song name to download: ");
		String name = sc.nextLine().trim();

		try {
			Connection connection = databaseconnection.dbconnection();
		
			String songQuery = "SELECT id, title, artist FROM songs WHERE LOWER(title) LIKE LOWER(?)";
			PreparedStatement ps = connection.prepareStatement(songQuery);
			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int songId = rs.getInt("id");
				String title = rs.getString("title");
				String artist = rs.getString("artist");

				String checkQuery = "SELECT * FROM downloaded_songs WHERE song_id = ? AND user_id = ?";
				PreparedStatement checkPs = connection.prepareStatement(checkQuery);
				checkPs.setInt(1, songId);
				checkPs.setInt(2, currentUserId);
				ResultSet checkRs = checkPs.executeQuery();

				if (checkRs.next()) {
					System.out.println("Song already downloaded: " + title);
					return;
				}

				String insertQuery = "INSERT INTO downloaded_songs (user_id, song_id) VALUES (?, ?)";
				PreparedStatement insertPs = connection.prepareStatement(insertQuery);
				insertPs.setInt(1, currentUserId);
				insertPs.setInt(2, songId);
				insertPs.executeUpdate();

				System.out.println("Downloading song: " + title + " ...");
				Thread.sleep(2000); 
				System.out.println("Song downloaded successfully: " + title + " - " + artist);

			} else {
				System.out.println("Song not found: " + name);
			}

		} catch (SQLException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void viewDownloadedSongs() {
		
		try {
			Connection connection = databaseconnection.dbconnection();
		
			String query = "SELECT s.title, s.artist, s.genre, s.duration " +
					"FROM songs s JOIN downloaded_songs ds ON s.id = ds.song_id " +
					"WHERE ds.user_id = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, currentUserId);
			ResultSet rs = ps.executeQuery();

			System.out.println("\n===== Downloaded Songs =====");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.printf("%-5s %-25s %-20s %-15s %-10s%n", "ID", "Title", "Artist", "Genre", "Duration");
			System.out.println("------------------------------------------------------------------------------");


			int index = 1;
			while (rs.next()) {
				System.out.printf("%-5d %-25s %-20s %-15s %-10s\n",
						index++,
						rs.getString("title"),
						rs.getString("artist"),
						rs.getString("genre"),
						rs.getString("duration"));
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void removeDownloadedSong() {
		System.out.print("Enter the song name to remove: ");
		String songName = sc.nextLine().trim();

		try {
			Connection connection = databaseconnection.dbconnection();
		
			String deleteQuery = "DELETE FROM downloaded_songs WHERE song_id = " +
					"(SELECT id FROM songs WHERE LOWER(title) LIKE LOWER(?) LIMIT 1) " +
					"AND user_id = ?";
			PreparedStatement ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, "%" + songName + "%");
			ps.setInt(2, currentUserId);
			int rows = ps.executeUpdate();

			if (rows > 0) {
				System.out.println("Song removed from downloads: " + songName);
			} else {
				System.out.println("Song not found in downloads: " + songName);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
