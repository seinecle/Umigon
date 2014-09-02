/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois adapted from
 * http://stackoverflow.com/questions/3656762/n-gram-generation-from-a-sentence
 */
public class NGramFinder {

    private HashMultiset<String> freqSetN = HashMultiset.create();
    private String[] words;
//    public static HashMultiset<String> multisetNGrams = HashMultiset.create();
    private HashSet<String> setUniqueNGramsPerLine;
    private HashMultiset<String> setAllNGramsPerLine;
    private HashMultiset<String> multisetToReturn;
    private List<String> listStrings;
    private String stringOriginal;

    public NGramFinder(List<String> listStrings) {
        this.listStrings = listStrings;
    }

    public NGramFinder(String string) {
        this.stringOriginal = string;
    }

    public HashMultiset<String> runIt(int maxgram, boolean binary) {
        multisetToReturn = HashMultiset.create();
        if (stringOriginal != null) {
            listStrings = new ArrayList();
            listStrings.add(stringOriginal);
        }

        for (String string : listStrings) {

            setAllNGramsPerLine = HashMultiset.create();
            setAllNGramsPerLine.addAll(run(string, maxgram));
//            if (mapofLines.get(lineNumber).contains("working memory")) {
//                System.out.println("alert!");
//            }


            //takes care of the binary counting. For the Alchemy API case, this happens in the AlchemyAPI extractor class
            if (binary) {
                setUniqueNGramsPerLine = new HashSet();
                setUniqueNGramsPerLine.addAll(setAllNGramsPerLine);
                multisetToReturn.addAll(setUniqueNGramsPerLine);
            } else {
                multisetToReturn.addAll(setAllNGramsPerLine);
            }

        }
        return multisetToReturn;

    }

    private Set<String> ngrams(int n, String str) {

        Set<String> setToReturn = new HashSet();
        words = str.split("["+WhiteSpaceChars.getWhiteSpaceChars()+"]+");
        for (int i = 0; i < words.length - n + 1; i++) {
            setToReturn.add(concat(words, i, i + n, n));
        }

        return setToReturn;


    }

    private String concat(String[] words, int start, int end, int ngram) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(i > start ? " " : "").append(words[i]);
        }
        return sb.toString();
    }

    private Multiset<String> run(String toBeParsed, int nGram) {
        freqSetN = HashMultiset.create();

        for (int n = 1; n <= nGram; n++) {
            freqSetN.addAll(ngrams(n, toBeParsed));
        }
        //System.out.println(freqList.get(i));
        return freqSetN;
    }
}
