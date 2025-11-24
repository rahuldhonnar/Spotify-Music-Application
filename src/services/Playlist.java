package services;

import java.util.ArrayList;
import java.util.List;


public class Playlist {

	private String playListName;
	
	private  List<Song> playListsong=new ArrayList<Song>();

	public Playlist() {
		super();
	}
	public Playlist(String playListName) {
		super();
		this.playListName=playListName;
		this.playListsong=new ArrayList<Song>();
	}

	public String getPlayListName() {
		return playListName;
	}

	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}

	public List<Song> getSongs() {
		return playListsong;
	}

	public void setSongs(List<Song> songs) {
		playListsong = songs;
	}

	public void  AddSonginplaylist(Song song)
	{
		playListsong.add(song);
		System.out.println(song.getTitle()+ " : Song added succesfully  ");
	}

	
	
	public void showplaylistsong()
	{
		System.out.println("PlayList name :-"+playListName);
		if(playListsong.isEmpty())
		{
			System.out.println("Playlist is Empty ");
		}else {
			for (Song s : playListsong) {
				System.out.println(s.getId()+": "+s.getTitle()+": "+s.getArtist()+": "+s.getGenre()+": "+s.getDuration());

			}
		}
	}

	
	public Song findsonginplaylist(String name) {
		for (Song s : playListsong) {
			if(s.getTitle().equalsIgnoreCase(name))
			{
				return s;
			}
		}
		System.out.println("Song not available in playlist "+name);
		return null;
	}

	public Song playsonginplaylist(String name) {
		Song isavailable=findsonginplaylist(name);
		if(isavailable!=null)
		{
			try {
				Thread.sleep(2000);
				System.out.println("Song is playing ");
				System.out.println("   >||<  ");
				System.out.println(isavailable.getTitle()+" ("+isavailable.getDuration()+":00 min)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isavailable;
	}

	public Song removesonginPlaylist(String name ) {
		for (Song s : playListsong) {
			if(s.getTitle().equalsIgnoreCase(name)) {
				playListsong.remove(s);
				System.out.println("Song Remove Succesfully in PlayList :- "+name);
				return s;
			}
		}
		return null;

	}

}
