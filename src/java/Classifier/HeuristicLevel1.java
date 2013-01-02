/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import TextCleaning.StatusCleaner;
import Twitter.Tweet;
import Twitter.TweetLoader;
import Utils.Clock;
import Utils.MultisetMostFrequentFiltering;
import Utils.NGramFinder;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class HeuristicLevel1 {

    Set<Tweet> setTweets;
    Multiset<String> setLangDetected;
    final private String punctuation = "!?.'\"-,()#=";

    public HeuristicLevel1(Set<Tweet> setTweets) {
        this.setTweets = setTweets;
    }

    public void applyLevel1() throws LangDetectException {

        DetectorFactory.loadProfile("D:\\Docs Pro Clement\\NetBeansProjects\\TwitterCollection\\profiles");
        Detector detector;
        setLangDetected = TreeMultiset.create();

        Iterator<Tweet> setTweetsIterator = setTweets.iterator();
        Tweet tweet;
        String status;
        HashMultiset<String> nGrams;
        Iterator<String> nGramsIterator;
        String nGramOrig;
        String nGram;
        int count = 0;
        String lang;
        Heuristic heuristic;
        Clock heuristicsClock = new Clock("starting the analysis of tweets");
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            if (tweet.getText().isEmpty()) {
                continue;
            }



            detector = DetectorFactory.create();
            detector.append(tweet.getText());
            try {
                lang = detector.detect();
                setLangDetected.add(lang);
                if (!lang.equals("en")) {
                    continue;
                }
            } catch (LangDetectException e) {
                System.out.println("tweet without language detected: " + tweet.getText());
                continue;
            }

//            System.out.println("curr tweet: " + tweet.toString());
            status = tweet.getText();
            status = StatusCleaner.clean(status);
//            System.out.println(status);
//            System.out.println("lang: " + lang);



            SentenceLevelRules sentenceRules = new SentenceLevelRules(tweet, status);
            tweet = sentenceRules.applyRules();


            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            while (nGramsIterator.hasNext()) {
                nGramOrig = nGramsIterator.next();
                nGram = nGramOrig.toLowerCase();
//                System.out.println("status: " + status);
                String nGramStripped = StringUtils.strip(nGram, punctuation);
//                if (
//                        //                        nGram.contains("#free") & 
//                        status.contains("We are Hiring")) {
//                    System.out.println(status);
//                }
                if (TweetLoader.Hloader.getMapH1().keySet().contains(nGram)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGram);
                    boolean result = (heuristic.checkFeatures(status, nGramOrig)) ? tweet.addToSetCategories(11) : tweet.addToSetCategories(12);
//                    System.out.println("res in H:" + result);
                } else if (TweetLoader.Hloader.getMapH1().keySet().contains(nGramStripped)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGramStripped);
                    boolean result = (heuristic.checkFeatures(status, nGramOrig)) ? tweet.addToSetCategories(11) : tweet.addToSetCategories(12);
//                    System.out.println("res in HStripped:" + result);

                }
                if (TweetLoader.Hloader.getMapH2().keySet().contains(nGram)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGram);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(12);
                    }
                    if (heuristic.isAllCaps(nGramOrig)) {
                        tweet.addToSetCategories(22);
                    }
                } else if (TweetLoader.Hloader.getMapH2().keySet().contains(nGramStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);

                    heuristic = TweetLoader.Hloader.getMapH2().get(nGramStripped);
                    if (heuristic.checkFeatures(status, StringUtils.strip(nGramOrig, punctuation))) {
                        tweet.addToSetCategories(12);
                    }
                    if (heuristic.isAllCaps(StringUtils.strip(nGramOrig, punctuation))) {
                        tweet.addToSetCategories(22);
                    }

                }
                if (TweetLoader.Hloader.getMapH3().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(22);
                    }
                } else if (TweetLoader.Hloader.getMapH3().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGramStripped);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(22);
                    }
                }


                if (TweetLoader.Hloader.getMapH4().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(4);
                    }
                } else if (TweetLoader.Hloader.getMapH4().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH4().get(nGramStripped);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(4);
                    }
                }
                if (TweetLoader.Hloader.getMapH5().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(5);
                    }
                } else if (TweetLoader.Hloader.getMapH5().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH5().get(nGramStripped);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(5);
                    }
                }
                if (TweetLoader.Hloader.getMapH6().keySet().contains(nGram) || TweetLoader.Hloader.getMapH6().keySet().contains(nGramStripped)) {
                    tweet.addToSetCategories(6);
                }


                if (TweetLoader.Hloader.getMapH7().keySet().contains(nGram) || TweetLoader.Hloader.getMapH7().keySet().contains(nGramStripped)) {
                    tweet.addToSetCategories(7);
                }

                if (TweetLoader.Hloader.getMapH8().keySet().contains(nGram)
                        || TweetLoader.Hloader.getMapH8().keySet().contains(nGramStripped)) {
                    tweet.addToSetCategories(8);
                }
                if (TweetLoader.Hloader.getMapH9().keySet().contains(nGram)
                        || TweetLoader.Hloader.getMapH9().keySet().contains(nGramStripped)) {
                    tweet.addToSetCategories(9);
                }
            }
            if (tweet.getSetCategoriesToString().equals("NO CATEGORY")) {
                count++;
            }

            if (( //                                        tweet.getSetCategories().contains(10) 
                    //                                        & tweet.getSetCategories().contains(1)
                    //                                        || 
                    tweet.getSetCategoriesToString().equals("NO CATEGORY"))) {
                System.out.println("tweet: " + tweet.getText());
                System.out.println("tweet classified as: " + tweet.getSetCategoriesToString());
            }
        }

        heuristicsClock.closeAndPrintClock();

        Clock reportClock = new Clock("generating report");

        Iterator<Integer> setCatIterator;
        setTweetsIterator = setTweets.iterator();
        Multiset<Integer> multisetCategories = HashMultiset.create();
        Map<Integer, String> mapExample = new HashMap();
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            setCatIterator = tweet.getSetCategories().iterator();
            while (setCatIterator.hasNext()) {
                Integer integer = setCatIterator.next();
                multisetCategories.add(integer);
                if (!mapExample.values().contains(tweet.getText())) {
                    mapExample.put(integer, tweet.getText());
                }
            }
        }
        reportClock.closeAndPrintClock();


        System.out.println("---- Omegan report for @HP -----------");
        System.out.println(
                "Number of tweets analyzed: " + setTweets.size());
        System.out.println("---------------");
        System.out.println("Most frequent languages used in tweets:");
        setLangDetected = new MultisetMostFrequentFiltering(setLangDetected).keepMostfrequent(5);
        Iterator<String> setLangDetectedIterator = Multisets.copyHighestCountFirst(setLangDetected).elementSet().iterator();
        while (setLangDetectedIterator.hasNext()) {
            String string = setLangDetectedIterator.next();
            System.out.println(string + ", " + setLangDetected.count(string));
        }
        System.out.println("---------------");
        System.out.println("Number of tweets in English: " + setLangDetected.count("en"));

        System.out.print(
                "Number of tweets attributed to one or several categories: " + (setLangDetected.count("en") - count));
        System.out.println(" (which means that " + (100 - (Math.round((double) (count * 10000) / setLangDetected.count("en")) / 100)) + "% of all tweets were attributed to a category)");

        System.out.println("---------------");
        System.out.println("Distribution of the tweets in different categories:");
        System.out.println("(note: a tweet can belong to several categories)");
        System.out.println("****");

        Iterator<Integer> multisetCategoriesIterator = Multisets.copyHighestCountFirst(multisetCategories).elementSet().iterator();
        while (multisetCategoriesIterator.hasNext()) {
            Integer integer = multisetCategoriesIterator.next();
            String example = mapExample.get(integer);
            System.out.println(Categories.get(integer) + ", " + multisetCategories.count(integer) + "x");
            System.out.println("Example 1: \"" + example + "\n");
            System.out.println("****");
        }
        System.out.println("---------------");
        System.out.println("---End of report---");


    }
}
