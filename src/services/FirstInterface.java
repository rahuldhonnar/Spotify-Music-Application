package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import mainServices.databaseconnection;

public class FirstInterface {

	private static final Scanner sc = new Scanner(System.in);
	static songsList list=new songsList();
	static CreatePlaylist plist=new CreatePlaylist();

	public void FirstPage() {
		boolean running = true;
		list.AllSong();
		while (running) {
			System.out.println("\n===== MAIN MENU =====");
			System.out.println("1  Search Song");
			System.out.println("2  Play Song");
			System.out.println("3  Filter by Artist");
			System.out.println("4  Filter by Genre");
			System.out.println("5  Filter by Duration");
			System.out.println("6  Create Playlist");
			System.out.println("7  Add Song to Playlist");
			System.out.println("8  View Playlists");
			System.out.println("9  Download Song");
			System.out.println("10 View Downloaded Songs");
			System.out.println("11 Logout");
			System.out.print("Enter your choice: ");

			int choice = sc.nextInt();
			sc.nextLine(); 

			switch (choice) {
			case 1 ->
			{
				searchSong();
			}
			case 2 -> {
				playSong();
			}
			case 3 -> {
				filterByArtist();
			}
			case 4 ->{
				filterByGenre();
			}
			case 5 ->{
				filterByDuration();
			}
			case 6 ->{
				createPlaylist();
			}
			case 7 ->{
				addSongToPlaylist();
			}
			case 8 ->{
				viewPlaylists();

			}
			case 9 ->{
				downloadSong();
			}
			case 10 ->{ 
				viewDownloadedSongs();
				System.out.println();
				boolean download=true;
				while(download)
				{
					System.out.println("1. Remove Song ");
					System.out.println("2.Back to Main Menu");
					System.out.println("ENter your choice ");
					int dch=sc.nextInt();
					sc.nextLine();
					switch (dch){
					case 1-> {
						removedownloadedsong();
					}
					case 2->{
						download=false;
					}
					default->{
						System.out.println("Invalid choice ...");
					}
					}
				}
			}
			case 11 -> {
				running = false;
				System.out.println("Logout successful!");
			}
			default -> System.out.println("Invalid choice. Try again!");
			}
		}
	}

	private void removedownloadedsong() {
	    try {
	        Connection con = databaseconnection.dbconnection();

	        String selectQuery = "SELECT s.id, s.title, s.artist " +
	                             "FROM songs s JOIN downloadSongs ds ON s.id = ds.song_id";
	        PreparedStatement ps = con.prepareStatement(selectQuery);
	        ResultSet rs = ps.executeQuery();

	        List<Integer> songIds = new ArrayList<>();
	        int index = 1;
	        System.out.println("\n===== Downloaded Songs =====");
	        System.out.printf("%-5s %-25s %-20s%n", "No.", "Title", "Artist");
	        System.out.println("---------------------------------------------");

	        while (rs.next()) {
	            songIds.add(rs.getInt("id"));
	            System.out.printf("%-5d %-25s %-20s%n",
	                    index++, rs.getString("title"), rs.getString("artist"));
	        }

	        if (songIds.isEmpty()) {
	            System.out.println("No downloaded songs found.");
	            return;
	        }

	        System.out.print("Enter the number of the song to remove: ");
	        int choice = sc.nextInt();
	        sc.nextLine();

	        if (choice < 1 || choice > songIds.size()) {
	            System.out.println("Invalid selection.");
	            return;
	        }

	        int songIdToRemove = songIds.get(choice - 1);

	        String deleteQuery = "DELETE FROM downloadSongs WHERE song_id = ?";
	        PreparedStatement psDelete = con.prepareStatement(deleteQuery);
	        psDelete.setInt(1, songIdToRemove);
	        int rows = psDelete.executeUpdate();

	        if (rows > 0) {
	            System.out.println(" Song removed successfully from downloads.");
				viewDownloadedSongs();

	        } else {
	            System.out.println(" Failed to remove the song.");
	        }

	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}


	private void searchSong()
	{
		System.out.print("Enter song name: ");
		String name = sc.nextLine().trim();
		String query = "SELECT * FROM songs WHERE LOWER(title) LIKE LOWER(?)";

		try {
			Connection con = databaseconnection.dbconnection();

			PreparedStatement ps = con.prepareStatement(query); 

			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();
			printSongs(rs);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void playSong()
	{
		System.out.print("Enter song name to play: ");
		String name = sc.nextLine().trim();
		String query = "SELECT * FROM songs WHERE LOWER(title) LIKE LOWER(?)";

		try {
			Connection con = databaseconnection.dbconnection();
			PreparedStatement ps = con.prepareStatement(query); 

			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("Loading song...");
				Thread.sleep(2000);
				System.out.println();
				System.out.println("Song playing:           "+rs.getString("title"));
				System.out.println();
				System.out.println("                    >||<   ");
				System.out.println("Now Playing: " + rs.getString("artist") + " - " +
						" (" + rs.getString("duration") + " min)");
			} else {
				System.out.println("Song not found!");
			}

		} catch (SQLException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void filterByArtist() 
	{
		System.out.print("Enter artist name: ");
		String artist = sc.nextLine().trim();
		String query = "SELECT * FROM songs WHERE LOWER(artist) LIKE LOWER(?)";

		try {
			Connection con = databaseconnection.dbconnection();

			PreparedStatement ps = con.prepareStatement(query); 

			ps.setString(1, "%" + artist + "%");
			ResultSet rs = ps.executeQuery();
			printSongs(rs);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void filterByGenre()
	{
		System.out.print("Enter genre: ");
		String genre = sc.nextLine().trim();
		String query = "SELECT * FROM songs WHERE LOWER(genre) LIKE LOWER(?)";

		try {
			Connection con = databaseconnection.dbconnection();

			PreparedStatement ps = con.prepareStatement(query);

			ps.setString(1, "%" + genre + "%");
			ResultSet rs = ps.executeQuery();
			printSongs(rs);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void filterByDuration()
	{
		System.out.print("Enter minimum duration: ");
		double min = sc.nextDouble();
		System.out.print("Enter maximum duration: ");
		double max = sc.nextDouble();
		sc.nextLine();

		String query = "SELECT * FROM songs WHERE duration BETWEEN ? AND ?";

		try {
			Connection con = databaseconnection.dbconnection();

			PreparedStatement ps = con.prepareStatement(query);

			ps.setDouble(1, min);
			ps.setDouble(2, max);
			ResultSet rs = ps.executeQuery();
			printSongs(rs);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void printSongs(ResultSet rs) throws SQLException
	{
		System.out.println("-----------------------------------------------------------------------------------------------------");
		System.out.printf("| %-5s | %-30s | %-25s | %-15s | %-10s |%n", "ID", "Title", "Artist", "Genre", "Duration");
		System.out.println("-----------------------------------------------------------------------------------------------------");

		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.printf("| %-5d | %-30s | %-25s | %-15s | %-10s |%n",
					rs.getInt("id"),
					rs.getString("title"),
					rs.getString("artist"),
					rs.getString("genre"),
					rs.getString("duration"));
		}
		if (!found) System.out.println("No songs found.");
		System.out.println("-----------------------------------------------------------------------------------------------------");
	}

	private void createPlaylist() {
		System.out.print("Enter playlist name: ");
		String name = sc.nextLine().trim();
		String query = "INSERT INTO playlists(name) VALUES (?)";

		try {
			Connection con = databaseconnection.dbconnection();

			PreparedStatement ps = con.prepareStatement(query);

			ps.setString(1, name);
			ps.executeUpdate();
			System.out.println("Playlist created: " + name);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private void addSongToPlaylist() {
		System.out.print("Enter playlist name: ");
		String playlistName = sc.nextLine().trim();

		try {
			Connection con = databaseconnection.dbconnection();

			String playlistQuery = "SELECT id FROM playlists WHERE LOWER(name) = LOWER(?)";
			PreparedStatement psPlaylist = con.prepareStatement(playlistQuery);
			psPlaylist.setString(1, playlistName);
			ResultSet rsPlaylist = psPlaylist.executeQuery();

			if (!rsPlaylist.next()) {
				System.out.println("Playlist not found!");
				return;
			}

			int playlistId = rsPlaylist.getInt("id");

			System.out.print("Enter song name: ");
			String songName = sc.nextLine().trim();
			String songQuery = "SELECT id, title FROM songs WHERE LOWER(title) LIKE LOWER(?)";
			PreparedStatement psSong = con.prepareStatement(songQuery);
			psSong.setString(1, "%" + songName + "%");
			ResultSet rsSong = psSong.executeQuery();

			if (!rsSong.next()) {
				System.out.println("Song not found!");
				return;
			}

			int songId = rsSong.getInt("id");
			String title = rsSong.getString("title");

			String checkQuery = "SELECT * FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";
			PreparedStatement psCheck = con.prepareStatement(checkQuery);
			psCheck.setInt(1, playlistId);
			psCheck.setInt(2, songId);
			ResultSet rsCheck = psCheck.executeQuery();

			if (rsCheck.next()) {
				System.out.println("Song already in playlist!");
				return;
			}

			String insertQuery = "INSERT INTO playlist_songs(playlist_id, song_id) VALUES (?, ?)";
			PreparedStatement psInsert = con.prepareStatement(insertQuery);
			psInsert.setInt(1, playlistId);
			psInsert.setInt(2, songId);
			psInsert.executeUpdate();

			System.out.println(title + " added to playlist " + playlistName);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void viewPlaylists() {

		try {
			Connection con = databaseconnection.dbconnection();

			String query = "SELECT name FROM playlists";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			System.out.println("\nYour Playlists:");
			boolean found = false;

			List<String> playlistNames = new ArrayList<>();

			while (rs.next()) {
				found = true;
				String name = rs.getString("name");
				playlistNames.add(name);
				System.out.println("- " + name);
			}

			if (!found) {
				System.out.println("No playlists found.");
				return;
			}

			System.out.print("\nEnter playlist name to open: ");
			String selectedName = sc.nextLine().trim();

			if (playlistNames.contains(selectedName)) {
				plist.openPlaylistMenu(selectedName); 
			} else {
				System.out.println("Playlist not found!");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void downloadSong() {
		System.out.print("Enter song name to download: ");
		String songName = sc.nextLine().trim();

		try {
			Connection con = databaseconnection.dbconnection();

			String songQuery = "SELECT id, title, artist FROM songs WHERE LOWER(title) LIKE LOWER(?)";
			PreparedStatement psSong = con.prepareStatement(songQuery);
			psSong.setString(1, "%" + songName + "%");
			ResultSet rsSong = psSong.executeQuery();

			if (!rsSong.next()) {
				System.out.println("Song not found!");
				return;
			}

			int songId = rsSong.getInt("id");
			String title = rsSong.getString("title");
			String artist = rsSong.getString("artist");

			String checkQuery = "SELECT * FROM downloadSongs WHERE song_id = ?";
			PreparedStatement psCheck = con.prepareStatement(checkQuery);
			psCheck.setInt(1, songId);
			ResultSet rsCheck = psCheck.executeQuery();

			if (rsCheck.next()) {
				System.out.println("Song already downloaded: " + title);
				return;
			}

			String insertQuery = "INSERT INTO downloadSongs(song_id) VALUES (?)";
			PreparedStatement psInsert = con.prepareStatement(insertQuery);
			psInsert.setInt(1, songId);
			psInsert.executeUpdate();

			System.out.println("Downloading song: " + title + "...");
			Thread.sleep(2000);
			System.out.println("Downloaded successfully: " + title + " - " + artist);

		} catch (SQLException | ClassNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void viewDownloadedSongs() {
		try {
			Connection con = databaseconnection.dbconnection(); 

			String query = "SELECT s.title, s.artist, s.genre, s.duration " +
					"FROM songs s JOIN downloadSongs ds ON s.id = ds.song_id";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			System.out.println("\n========================= Downloaded Songs ========================================");
			System.out.printf("%-5s %-25s %-20s %-15s %-10s\n", "No.", "Title", "Artist", "Genre", "Duration");
			System.out.println("---------------------------------------------------------------------------------------");

			int index = 1;
			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.printf("%-5d %-25s %-20s %-15s %-10s\n",
						index++,
						rs.getString("title"),
						rs.getString("artist"),
						rs.getString("genre"),
						rs.getString("duration"));
			}
			if (!found) System.out.println("No downloaded songs yet.");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
