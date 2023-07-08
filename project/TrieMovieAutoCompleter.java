package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        for (String movieName : ID_MAP.keySet()) {
            String og = movieName;
            String[] nameArr = movieName.split(" ");
            IDeque<String> suffixDeque = new ArrayDeque<String>();
            IDeque<String> key = new ArrayDeque<String>();
            for (int i = 0; i < nameArr.length; i++) {
                key.addBack(nameArr[i].toLowerCase());
            }
            for(int j = 0; j < nameArr.length; j++){
                if(!titles.containsKey(key)){
                    IDeque<String> temp1 = new ArrayDeque<String>();
                    temp1.addBack(og);
                    titles.put(key, temp1);
                    key.removeFront();
                }
                else{
                    IDeque<String> temp = titles.get(key);
                    temp.addBack(og);
                    key.removeFront();
                }
            }
        }

    }

    public static IDeque<String> complete(String term) {
        IDeque<String> termdeq = new ArrayDeque<String>();
        String[] nameArr = term.split(" ");
        for(int i = 0; i < nameArr.length; i++){
            termdeq.addBack(nameArr[i]);
        }
        ICollection<IDeque<String>> ultdeq = titles.getCompletions(termdeq);
        IDeque<String> retval = new ArrayDeque<String>();
        for(IDeque<String> deq : ultdeq){
            for(String s : deq){
                if (!(retval.contains(s))) {
                    retval.addBack(s);
                }
            }
        }
        return retval;


    }
}
