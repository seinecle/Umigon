/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Heuristics.HashtagLevelHeuristics;
import Heuristics.Heuristic;
import Heuristics.SentenceLevelHeuristicsPost;
import Heuristics.SentenceLevelHeuristicsPre;
import LanguageDetection.LanguageDetector;
import TextCleaning.StatusCleaner;
import Twitter.Tweet;
import Twitter.TweetLoader;
import Utils.Clock;
import Utils.NGramFinder;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.collect.HashMultiset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class ClassifierMachine {

    String status;
    HashMultiset<String> nGrams;
    Iterator<String> nGramsIterator;
    String nGramOrig;
    String nGram;
    String nGramStripped;
    int count = 0;
    Heuristic heuristic;
    SentenceLevelHeuristicsPre sentenceHeuristicsPre;
    SentenceLevelHeuristicsPost sentenceHeuristicsPost;
    HashtagLevelHeuristics hashtagHeuristics;
    final private String punctuation = "!?.'\"-,()#=";
    Tweet tweet;

    public ClassifierMachine() {
    }

    public Set<Tweet> classify(Set<Tweet> setTweets) throws LangDetectException {
        Iterator<Tweet> setTweetsIterator = setTweets.iterator();

        Clock heuristicsClock = new Clock("starting the analysis of tweets");
        String lang;
        Set<Tweet> setTweetsClassified = new HashSet();
        LanguageDetector ld = new LanguageDetector();

        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            status = tweet.getText();

            if (status.isEmpty()) {
                continue;
            }

            if (!ld.detectEnglish(status)) {
//                System.out.println("non english tweet detected! "+status);
                continue;
            }

            tweet.setSetCategories(null);
//            System.out.println("curr tweet: " + tweet.toString());
            status = StatusCleaner.clean(status);
//            System.out.println(status);
//            System.out.println("lang: " + lang);
            sentenceHeuristicsPre = new SentenceLevelHeuristicsPre(tweet, status);
            tweet = sentenceHeuristicsPre.applyRules();

            hashtagHeuristics = new HashtagLevelHeuristics(tweet);
            tweet = hashtagHeuristics.applyRules();

            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            String result;
            String nGramLowerCase;
            String nGramLowerCaseStripped;

            while (nGramsIterator.hasNext()) {
                nGramOrig = nGramsIterator.next();
                nGramLowerCase = nGramOrig.toLowerCase();
                //this condition puts the ngram in lower case, except if it is all in upper case (this is a valuable info)
                //                System.out.println("status: " + status);
                nGramStripped = StringUtils.strip(nGramOrig, punctuation);
                nGramLowerCaseStripped = StringUtils.strip(nGramOrig, punctuation);
//                if ( //                        nGram.contains("#free") & 
//                        status.contains(" Your tweets open daily")) {
//                    System.out.println(status);
//                }
                if (TweetLoader.Hloader.getMapH1().keySet().contains(nGramLowerCase)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGramLowerCase);
                    result = (heuristic.checkFeatures(status, nGramOrig));
                    // System.out.println("result: " + result);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToSetCategories(result);

                    }
                } else if (TweetLoader.Hloader.getMapH1().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGramLowerCaseStripped);
                    result = (heuristic.checkFeatures(status, nGramOrig));
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH2().keySet().contains(nGramLowerCase)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGram);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }

                } else if (TweetLoader.Hloader.getMapH2().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, StringUtils.strip(nGramOrig, punctuation));
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH3().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGramLowerCase);

                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH3().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH4().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH4().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH5().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH5().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH6().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH6().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH6().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH6().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH7().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH7().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH7().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH7().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH8().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH8().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH8().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH8().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH9().keySet().contains(nGramLowerCase)) {
                    heuristic = TweetLoader.Hloader.getMapH9().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH9().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH9().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }


            }
            sentenceHeuristicsPost = new SentenceLevelHeuristicsPost(tweet, status);
            tweet = sentenceHeuristicsPost.applyRules();

            setTweetsClassified.add(tweet);
            if (tweet.getSetCategories().isEmpty()) {
                System.out.println("tweet with no cat: " + status);
            }
//            if ((StringUtils.countMatches(tweet.getText(), "@") > 2) & tweet.getSetCategories().isEmpty()) {
//                System.out.println("tweet with no cat: " + status);
//            }
//            if (tweet.getSetCategories().contains("011") & tweet.getSetCategories().contains("012") ) {
//                System.out.println("tweet with pos and neg tone: " + status);
//            }

        }
        heuristicsClock.closeAndPrintClock();
        return setTweetsClassified;


    }
}