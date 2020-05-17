package program;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HtmlUtils {
    public static String getRelevantHTML(String url) {
        final String start = "[tab]";
        final String end = "&quot;,&quot;revision_id";

        boolean inTab = false;
        String output = "";

        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {

                if (inputLine.contains(start)) {
                    inTab = true;
                }

                if (inTab) {
                    output += inputLine + "\n";
                }

                if (inputLine.contains("[/tab]")) {
                    inTab = false;
                }

                if (inputLine.contains("capo&quot;:") && (MusicUtils.capo==0)) {
                    String preCapo = "capo&quot;:";
                    inputLine = inputLine.substring(inputLine.indexOf(preCapo) + preCapo.length());
                    inputLine = inputLine.substring(0, inputLine.indexOf(","));
                    MusicUtils.capo = Integer.parseInt(inputLine);
                }

                if (inputLine.contains("<title>")) {
                    MusicUtils.title = inputLine.substring(inputLine.indexOf("<title>")+7, inputLine.indexOf("@")-1);
                }
            }
        } catch (Exception e) {
            return null;
        }
        output = output.substring(output.indexOf(start));
        output = output.substring(0, output.indexOf(end));
        output = output.replace("\\n","\n");

        return escapeHTML(output);
    }

    private static String escapeHTML(String input) {
        String output = input;
        HashMap<String, String> replacements = new HashMap<String, String>();
        replacements.put("&amp;", "&");
        replacements.put("\\&quot;", "\"");

        for (String toReplace : replacements.keySet()) {
            output = output.replace(toReplace, replacements.get(toReplace));
        }

        return output;
    }

    public static String removeTags(String input) {
        String output = input;
        final String[] replacements = new String[] {"[ch]", "[/ch]", "[tab]", "[/tab]", "\\r" };

        for (String toGo : replacements) {
            output = output.replace(toGo,"");
        }

        return output;
    }

    public static String addChTags(String input) {
        return "[ch]" + input + "[/ch]";
    }

    public static ArrayList<String> getChords(String input) {
        ArrayList<String> chords = new ArrayList<String>();
        String temp = input;

        String currentChord;
        int currentChordIndex;
        while (true) {
            currentChordIndex = temp.indexOf("[ch]");
            if (currentChordIndex == -1) {
                break;
            }

            currentChord = temp.substring(currentChordIndex, temp.indexOf("[/ch]")+5);
            if (!chords.contains(currentChord)) {
                chords.add(currentChord);
            }
            temp = temp.substring(temp.indexOf("[/ch]")+5);
        }

        return chords;
    }
}
