/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Heuristics.Heuristic;
import Heuristics.SentenceLevelHeuristics;
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
    SentenceLevelHeuristics sentenceRules;
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
            sentenceRules = new SentenceLevelHeuristics(tweet, status);
            tweet = sentenceRules.applyRules();
            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            String result;

            while (nGramsIterator.hasNext()) {
                nGramOrig = nGramsIterator.next();
                nGram = nGramOrig.toLowerCase();
//                System.out.println("status: " + status);
                nGramStripped = StringUtils.strip(nGram, punctuation);
//                if ( //                        nGram.contains("#free") & 
//                        status.contains(" Your tweets open daily")) {
//                    System.out.println(status);
//                }
                if (TweetLoader.Hloader.getMapH1().keySet().contains(nGram)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGram);
                    result = (heuristic.checkFeatures(status, nGramOrig));
                    // System.out.println("result: " + result);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToSetCategories(result);

                    }
                } else if (TweetLoader.Hloader.getMapH1().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGramStripped);
                    result = (heuristic.checkFeatures(status, nGramOrig));
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH2().keySet().contains(nGram)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGram);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }

                } else if (TweetLoader.Hloader.getMapH2().keySet().contains(nGramStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGramStripped);
                    result = heuristic.checkFeatures(status, StringUtils.strip(nGramOrig, punctuation));
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH3().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGram);

                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH3().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH4().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH4().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH5().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH5().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH6().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH6().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH6().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH6().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH7().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH7().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH7().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH7().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH8().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH8().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH8().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH8().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }

                if (TweetLoader.Hloader.getMapH9().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH9().get(nGram);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                } else if (TweetLoader.Hloader.getMapH9().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH9().get(nGramStripped);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToSetCategories(result);
                    }
                }


            }
            setTweetsClassified.add(tweet);
            if (tweet.getSetCategories().isEmpty()) {
                System.out.println("tweet with not cat: " + status);
            }

        }
        heuristicsClock.closeAndPrintClock();
        return setTweetsClassified;


    }
}