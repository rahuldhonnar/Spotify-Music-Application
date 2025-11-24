package services;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private List<Playlist> playlists = new ArrayList<>();
    static List<Playlist> allPlaylists = new ArrayList<>(); 

    

	public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        System.out.println("Playlist created: " + playlist.getPlayListName());
    }

    public void viewAllPlaylists() {
        if (playlists.isEmpty()) {
            System.out.println("No playlists available.");
        } else {
            System.out.println("\n All Playlists:");
            for (int i = 0; i < playlists.size(); i++) {
                System.out.println((i + 1) + ". " + playlists.get(i).getPlayListName());
            }
        }
    }

    public Playlist findPlaylist(String name) {
        for (Playlist p : playlists) {
            if (p.getPlayListName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        System.out.println(" Playlist not found: " + name);
        return null;
    }
    public List<Playlist> getPlaylists() {
        return playlists;
    }
}
