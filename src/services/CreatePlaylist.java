package services;

import java.sql.*;
import java.util.Scanner;

import mainServices.databaseconnection;

public class CreatePlaylist {

    static Scanner sc = new Scanner(System.in);

    public void openPlaylistMenu(String playlistName) {
        boolean running = true;

        while (running) {
            System.out.println("\n--------- Playlist: " + playlistName + " ------------");
            System.out.println("1. Add Song");
            System.out.println("2. View All Songs");
            System.out.println("3. Play a Song");
            System.out.println("4. Remove a Song");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> 
                {
                	addSongToPlaylist(playlistName);
                
                }
                case 2 ->{
                	viewSongsInPlaylist(playlistName);
                }
                case 3 ->{
                	playSongFromPlaylist(playlistName);
                }
                case 4 ->{
                	removeSongFromPlaylist(playlistName);
                }
                case 5 -> {
                    System.out.println("Returning to Main Menu...");
                    running = false;
                }
                default ->{
                	System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
    private void addSongToPlaylist(String playlistName) {
        try (Connection con = databaseconnection.dbconnection()) {
            sc.nextLine(); // clear leftover newline
            System.out.print("Enter song name to add: ");
            String songName = sc.nextLine().trim();

            // Get playlist ID
            String getPlaylistId = "SELECT id FROM playlists WHERE name = ?";
            PreparedStatement ps1 = con.prepareStatement(getPlaylistId);
            ps1.setString(1, playlistName);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                System.out.println("Playlist not found.");
                return;
            }
            int playlistId = rs1.getInt("id");

            // Find song details
            String getSong = "SELECT id, title, artist, genre, duration FROM songs WHERE LOWER(title) LIKE LOWER(?)";
            PreparedStatement ps2 = con.prepareStatement(getSong);
            ps2.setString(1, "%" + songName + "%");
            ResultSet rs2 = ps2.executeQuery();

            if (!rs2.next()) {
                System.out.println("Song not found in library.");
                return;
            }

            int songId = rs2.getInt("id");
            String title = rs2.getString("title");
            String artist = rs2.getString("artist");
            String genre = rs2.getString("genre");
            String duration = rs2.getString("duration");

            // Check if already exists
            String checkQuery = "SELECT * FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";
            PreparedStatement ps3 = con.prepareStatement(checkQuery);
            ps3.setInt(1, playlistId);
            ps3.setInt(2, songId);
            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {
                System.out.println("⚠️ Song already exists in playlist: " + title);
                return;
            }

            // ✅ Correct Insert
            String insert = """
                INSERT INTO playlist_songs (playlist_id, song_id, song_name, artist_name, genre, duration)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement psInsert = con.prepareStatement(insert);
            psInsert.setInt(1, playlistId);
            psInsert.setInt(2, songId);
            psInsert.setString(3, title);
            psInsert.setString(4, artist);
            psInsert.setString(5, genre);
            psInsert.setString(6, duration);
            psInsert.executeUpdate();

            System.out.println("✅ Song added to playlist successfully: " + title + " by " + artist);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void viewSongsInPlaylist(String playlistName) {
        try {
        	Connection con = databaseconnection.dbconnection();

            String query = """
                SELECT s.id, s.title, s.artist, s.genre, s.duration
                FROM songs s
                JOIN playlist_songs ps ON s.id = ps.song_id
                JOIN playlists p ON ps.playlist_id = p.id
                WHERE p.name = ?
            """;

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, playlistName);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nSongs in playlist: " + playlistName);
            System.out.println("-----------------------------------------------------------------------------");
            System.out.printf("%-5s %-25s %-20s %-15s %-10s%n", "ID", "Title", "Artist", "Genre", "Duration");
            System.out.println("------------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-25s %-20s %-15s %-10s%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("genre"),
                        rs.getString("duration"));
            }
            if (!found) System.out.println("No songs in this playlist.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void playSongFromPlaylist(String playlistName) {
        try {
        	Connection con = databaseconnection.dbconnection();
        
            System.out.print("Enter song name to play: ");
            String songName = sc.next().trim();

            String query = """
                SELECT s.title, s.artist, s.duration
                FROM songs s
                JOIN playlist_songs ps ON s.id = ps.song_id
                JOIN playlists p ON ps.playlist_id = p.id
                WHERE p.name = ? AND LOWER(s.title) LIKE LOWER(?)
            """;

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, playlistName);
            ps.setString(2, "%" + songName + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Loading Song...");
                Thread.sleep(1000);
                System.out.println();
                System.out.println( "\t\t\t"+rs.getString("title") );
                System.out.println();
                System.out.println();
                System.out.println("                >||<                   ");
                System.out.println("Now Playing: " + rs.getString("artist") + " - " +
                        " (" + rs.getString("duration") + " min)");
            } else {
                System.out.println("Song not found in this playlist!");
            }

        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void removeSongFromPlaylist(String playlistName) {
        try {
        	Connection con = databaseconnection.dbconnection();
            System.out.print("Enter song name to remove: ");
            String songName = sc.nextLine().trim();

            String query = """
                DELETE ps FROM playlist_songs ps
                JOIN playlists p ON ps.playlist_id = p.id
                WHERE p.name = ? AND ps.song_id = (
                    SELECT id FROM songs WHERE LOWER(title) LIKE LOWER(?) LIMIT 1
                )
            """;

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, playlistName);
            ps.setString(2, "%" + songName + "%");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Song removed successfully.");
            } else {
                System.out.println("Song not found in this playlist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
