package program;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class SearchUG {
    public static String getURL(String query) {
        query = query.replace(" ", "+");
        query = query.replace("'", "%27");


        String searchURL = "https://www.ultimate-guitar.com/search.php?title=" + query + "&page=1&type=300";

        String output = getRawPage(searchURL);
        if (output == null) {
            return null;
        }
        output = condenseRawPage(output);
        output = chooseURL(output);
        return output;
    }

    private static String getRawPage(String url) {
        String output = "";
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                output += inputLine + "\n";
            }
        } catch (Exception e) {
            return null;
        }
        return output;
    }

    private static String condenseRawPage(String rawPage) {
        rawPage = rawPage.replace("{", "\n{");
        String[] splitPage = rawPage.split("\n");
        String output = "";

        for (String line : splitPage) {
            if (line.contains("&quot;siteId&quot;:")) {
                break;
            }

            if (line.contains("&quot;isBot&quot;:")) {
                continue;
            }

            if ((line.contains("{&quot;id&quot;:")) || (line.contains("{&quot;is_acoustic&quot;:"))) {
                output += line + "\n";
            }
        }
    return output;
    }

    private static String chooseURL(String condensedPage) {
        int mostVotes = -1;
        String mostVotesURL = "URL REMAINED NULL";

        String[] splitData = condensedPage.split("\n");

        boolean nextLineChosen = false;
        int currentVotesNumber;
        for (String line : splitData) {
            if (nextLineChosen) {
                if (getUrlField(line) != null) {
                    mostVotesURL = getUrlField(line);
                }
                nextLineChosen = false;
                continue;
            }

            try {
                currentVotesNumber = Integer.parseInt(getVotes(line));
            } catch (Exception e) { continue; }

            if (currentVotesNumber > mostVotes) {
                mostVotes = currentVotesNumber;
                nextLineChosen = true;
            }
        }
        return mostVotesURL;
    }

    private static String getVotes(String line) {
        String target = "&quot;" + "votes" + "&quot;:";
        if (!line.contains(target)) {
            return null;
        }

        String output;
        output = line.substring(line.indexOf(target));
        output = output.substring(target.length());

        if (!output.contains(",&")) {
            throw new IllegalArgumentException("Seems like the requested field is at the end of the line.\n" +
                    "votes" + "\n" +
                    line);
        }

        output = output.substring(0, output.indexOf(",&"));

        return output;
    }

    private static String getUrlField(String line) {
        String target = "&quot;tab_url&quot;:";
        try {
            String output = line.substring(line.indexOf(target));
            output = output.substring(output.indexOf("http"));
            output = output.substring(0, output.indexOf("&quot;"));

            return output;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}