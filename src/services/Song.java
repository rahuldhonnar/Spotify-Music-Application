package services;

public class Song {
	
	private static int idCounter = 1;
	private String id;
	private String title;
	private String artist;
	private String genre;
	protected int duration;
	
	public Song(String id, String title, String artist, String genre, int duration) {
		this.id = " " + (idCounter++);
		this.title = title;
		this.artist = artist;
		this.genre = genre;
		this.duration = duration;
	}
	
	public static void resetCounter() {
	    idCounter = 0;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	


	@Override
	public String toString() {
		return String.format("%-10s | %-20s | %-15s | %-10s | %2d min |", 
				id, title, artist, genre, duration);

	}

	public boolean add(Song songs) {
		return songs.add(songs);
	}
	

	
	
	
	
	

}
