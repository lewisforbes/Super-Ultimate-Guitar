package program;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicUtils {

    public static int capo = 0;
    public static String title;

    /** takes a list of chords with tags and transposes each one **/
    public static ArrayList<String> transposeChords(ArrayList<String> originalChords, int by) {
        ArrayList<String> transposedChords = new ArrayList<String>();
        String currentChord;
        for (String chord : originalChords) {
            if (chord.indexOf("/") == chord.lastIndexOf("/")) {
                currentChord = transposeChord(HtmlUtils.removeTags(chord), by);
            } else {
                String[] splitChord = HtmlUtils.removeTags(chord).split("/");
                currentChord = transposeChord(splitChord[0], by) + "/" + transposeChord(splitChord[1], by);
            }

            transposedChords.add(HtmlUtils.addChTags(currentChord));
        }
        return transposedChords;
    }

    /** inputs and outputs parts of chords without [ch][/ch] tags **/
    private static String transposeChord(String original, int by) {
        String output = HtmlUtils.removeTags(original);
        int length = 1;
        try {
            if ((original.charAt(1)=='b') || (original.charAt(1)=='#')) {
                length = 2;
            }
        } catch (Exception e) { }

        String suffix = "";
        if (length != original.length()) {
            suffix = original.substring(length);
        }
        output = output.substring(0, length);;

        ArrayList<String> sharpScale = new ArrayList<String>(Arrays.asList("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"));
        ArrayList<String> flatScale  = new ArrayList<String>(Arrays.asList("A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"));

        if (flatScale.contains(output)) {
            output = sharpScale.get(flatScale.indexOf(output));
        }

        if (!sharpScale.contains(output)) {
            throw new IllegalArgumentException(output);
        }

        int index = sharpScale.indexOf(output);
        index += by;
        while (index<0) {
            index += sharpScale.size();
        }
        while (index>=12) {
            index -= sharpScale.size();
        }

        return sharpScale.get(index) + suffix;
    }
}
