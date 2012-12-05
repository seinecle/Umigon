/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.Tweet;
import Twitter.TweetLoader;
import Utils.NGramFinder;
import com.google.common.collect.HashMultiset;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class HeuristicLevel1 {

    Set<Tweet> setTweets;
    final private String punctuation = "!?.'\"-,()";

    public HeuristicLevel1(Set<Tweet> setTweets) {
        this.setTweets = setTweets;
    }

    public void applyLevel1() {
        Iterator<Tweet> setTweetsIterator = setTweets.iterator();
        Tweet tweet;
        String status;
        HashMultiset<String> nGrams;
        Iterator<String> nGramsIterator;
        String nGramOrig;
        String nGram;
        int count = 0;
        Heuristic heuristic;
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
//            System.out.println("curr tweet: " + tweet.toString());
            status = tweet.getText();
            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            while (nGramsIterator.hasNext()) {
                nGramOrig = nGramsIterator.next();
                nGram = nGramOrig.toLowerCase();
//                if (nGramOrig.equals("#HorriblyBuilt")) {
//                    System.out.println("nGramOrig: " + nGramOrig);
//                }
//                System.out.println("status: " + status);
                String nGramStripped = StringUtils.strip(nGram, punctuation);
//                if (nGram.equals("i was wondering")) {
//                    System.out.println("i was wondering");
//                }
                if (TweetLoader.Hloader.getMapH1().keySet().contains(nGram)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGram);
                    boolean result = (heuristic.checkFeatures(status, nGramOrig)) ? tweet.addToSetCategories(1) : tweet.addToSetCategories(2);
//                    System.out.println("res in H:" + result);
                } else if (TweetLoader.Hloader.getMapH1().keySet().contains(nGramStripped)) {
//                    System.out.println("positive detected");
                    heuristic = TweetLoader.Hloader.getMapH1().get(nGramStripped);
                    boolean result = (heuristic.checkFeatures(status, nGramOrig)) ? tweet.addToSetCategories(1) : tweet.addToSetCategories(2);
//                    System.out.println("res in HStripped:" + result);

                }
                if (TweetLoader.Hloader.getMapH2().keySet().contains(nGram)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGram);
                    heuristic = TweetLoader.Hloader.getMapH2().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(2);
                    }
                    if (heuristic.isAllCaps()) {
                        tweet.addToSetCategories(3);
                    }
                } else if (TweetLoader.Hloader.getMapH2().keySet().contains(nGramStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);

                    heuristic = TweetLoader.Hloader.getMapH2().get(nGramStripped);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(2);
                    }
                    if (heuristic.isAllCaps()) {
                        tweet.addToSetCategories(3);
                    }

                }
                if (TweetLoader.Hloader.getMapH3().keySet().contains(nGram)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGram);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(3);
                    }
                } else if (TweetLoader.Hloader.getMapH3().keySet().contains(nGramStripped)) {
                    heuristic = TweetLoader.Hloader.getMapH3().get(nGramStripped);
                    if (heuristic.checkFeatures(status, nGramOrig)) {
                        tweet.addToSetCategories(3);
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

            if ((tweet.getSetCategories().contains(1) & tweet.getSetCategories().contains(2)
//                    || tweet.getSetCategoriesToString().equals("NO CATEGORY")
                    )) {
                System.out.println("tweet: " + tweet.getText());
                System.out.println("tweet classified as: " + tweet.getSetCategoriesToString());
            }
        }

        System.out.println(
                "number of tweets: " + setTweets.size());
        System.out.println(
                "number of tweets without category: " + count);
        System.out.println("Tweets categorized: " + (100 - (Math.round((double) (count * 10000) / setTweets.size()) / 100)) + "%");

    }
}
