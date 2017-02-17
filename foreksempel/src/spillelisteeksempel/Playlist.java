package spillelisteeksempel;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

	private String title;
	// final angir at length settes én gang, i konstruktøren
	private final int maxLength;
	private List<PlaylistTrack> tracks;

	// må oppgi makslengden ved opprettelsen
	public Playlist(int maxLength) {
		this.maxLength = maxLength;
		// husk på å initialisere listen til et listeobjekt
		this.tracks = new ArrayList<PlaylistTrack>();
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		if (! isValidTitle(title)) {
			throw new IllegalArgumentException("Invalid title: " + title);
		}
		this.title = title;
	}

	private boolean isValidTitle(String title) {
		for (int i = 0; i < title.length(); i++) {
			char c = title.charAt(i);
			// returner false hvis bokstav ikke er lovlig
			if (! (Character.isLetter(c) || Character.isDigit(c) || " ,-".indexOf(c) >= 0)) {
				return false;
			}
		}
		return true;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	private void checkPlayLength(int delta) {
		if (getPlayLength() + delta > maxLength) {
			throw new IllegalArgumentException("Du er i ferd med å gjøre spillelista for lang");
		}
	}

	// summerer spillelengden til alle utsnittene
	public int getPlayLength() {
		int sum = 0;
		for (PlaylistTrack track : tracks) {
			sum = sum + track.getPlayLength();
		}
		return sum;
	}
	
	// innkapslingsmetoder for tracks-lista
	
	public int getTrackCount() {
		return tracks.size();
		
	}
	
	public Track getTrack(int i) {
		return tracks.get(i).getTrack();
	}

	public PlaylistTrack getPlaylistTrack(int i) {
		return tracks.get(i);
	}
	
	public int getTrackStart(int i) {
		return tracks.get(i).getStart();
	}

	public int getTrackEnd(int i) {
		return tracks.get(i).getEnd();
	}

	// på hvilken posisjon ligger PlaylistTrack-objektet som er koblet til track?
	public int indexOfTrack(Track track) {
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i).getTrack() == track) {
				return i;
			}
		}
		// fant ikke noe PlaylistTrack-objekt koblet til track
		return -1;
	}
	
	public boolean containsTrack(Track track) {
		return indexOfTrack(track) >= 0;
	}
	
	public void addTrack(Track track) {
		PlaylistTrack pt = new PlaylistTrack(track);
		checkPlayLength(pt.getPlayLength());
		tracks.add(pt);
	}

	public void addTrack(Track track, int start, int end) {
		PlaylistTrack pt = new PlaylistTrack(track, start, end);
		checkPlayLength(pt.getPlayLength());
		tracks.add(pt);
	}

	public void setTrackStartEnd(Track track, int start, int end) {
		PlaylistTrack pt = tracks.get(indexOfTrack(track));
		int oldPlayLength = pt.getPlayLength();
		int newPlayLength = pt.getPlayLength(start, end) - oldPlayLength;
		checkPlayLength(newPlayLength - oldPlayLength);
		pt.setStartEnd(start, end);
	}

	public void removeTrack(Track track) {
		// finner posisjonen til PlaylistTrack-objektet som er koblet til track
		int pos = indexOfTrack(track);
		if (pos >= 0) {
			// fjerner PlaylistTrack-objektet på posisjon vi fant
			tracks.remove(pos);
		}
	}
}
