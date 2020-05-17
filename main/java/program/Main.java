package program;

import java.util.ArrayList;

public class Main {

    private static String currentTaggedSong;
    private static ArrayList<String> originalChords;
    private static String currentSongUrl;

    /** should return null if no song found **/
    public static String newSong(String query) {
        reset();

        currentSongUrl = query;
        if (!query.contains("tabs.ultimate-guitar.com/tab")) {
            currentSongUrl = SearchUG.getURL(query);
            if (currentSongUrl == null) {
                return null;
            }
        }

        currentTaggedSong = HtmlUtils.getRelevantHTML(currentSongUrl);
        if (currentTaggedSong == null) {
            return null;
        }

        originalChords = HtmlUtils.getChords(currentTaggedSong);

        return songToString(currentTaggedSong, 0);
    }

    public static String transposeSong(int transposedBy) {
        if (currentTaggedSong == null) {
            throw new IllegalArgumentException("transposeSong called before newSong");
        }
        String transposedSong = currentTaggedSong;

        ArrayList<String> newChords = MusicUtils.transposeChords(originalChords, transposedBy);
        assert (newChords.size() == originalChords.size());

        for (int i=0; i<originalChords.size(); i++) {
            transposedSong = transposedSong.replace(originalChords.get(i), HtmlUtils.removeTags(newChords.get(i)));
        }

        return songToString(transposedSong, transposedBy);
    }

    private static String songToString(String song, int transposedBy) {
        String output = MusicUtils.title + "\n";
        output += "Available at: " + currentSongUrl + "\n\n";
        output += "Capo: " + MusicUtils.capo + "\n";
        output += "Currently transposed by: " + transposedBy + "\n";
        output += "\n\n" + HtmlUtils.removeTags(song);
        return output;
    }

    private static void reset() {
        currentTaggedSong = null;
        originalChords = null;
        currentSongUrl = null;
        MusicUtils.capo = 0;
    }
}
