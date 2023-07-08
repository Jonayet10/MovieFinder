package edu.caltech.cs2.project04;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.datastructures.ArrayDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() {
        for (String movieName: ID_MAP.keySet()) {
            String og = movieName;
            String[] nameArr = movieName.split(" ");
            IDeque<String> suffixDeque = new ArrayDeque<String>();

            for (int i = 0; i < nameArr.length; i++) {
                suffixDeque.addBack(movieName.toLowerCase());                       // everything in suffixDeque might have to be lower case as the tests are checking for terms that are all lower case
                String[] splittedMovieNames = movieName.split(" ", 2);
                if (splittedMovieNames.length > 1) {
                    movieName = splittedMovieNames[1];
                }
            }
            titles.put(og, suffixDeque);
        }
    }
//
    public static IDeque<String> complete(String term) {
        IDeque<String> returned = new ArrayDeque<String>();
        String wordTerm = term + " ";
        for (String key : titles.keySet()) {
            for (String suffix : titles.get(key)) {
                    if (suffix.startsWith(wordTerm) || suffix.equals(term)) {
                        if (!(returned.contains(key))) {
                            returned.addBack(key);
                        }
                }
                }
            }
        return returned;
    }
}
