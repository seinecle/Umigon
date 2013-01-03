/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.Tweet;
import Utils.Clock;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class TweetLooper {

    Set<Tweet> setTweets;
    public Multiset<String> setLangDetected;
    Iterator<String> setCatIterator;
    Tweet tweet;
    Iterator<Tweet> setTweetsIterator;
    int originalNumberTweet;

    public TweetLooper(Set<Tweet> setTweets) {
        this.setTweets = setTweets;
        originalNumberTweet = setTweets.size();
    }

    public void applyLevel1() throws LangDetectException {

        setTweets = new ClassifierMachine().classify(setTweets);
        setTweetsIterator = setTweets.iterator();

        Clock reportClock = new Clock("generating report");


        setTweetsIterator = setTweets.iterator();
        Multiset<String> multisetCategories = HashMultiset.create();
        Map<String, String> mapExample = new HashMap();
        int tweetsWithoutCategory = 0;
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            if (tweet.getSetCategories().isEmpty()) {
                tweetsWithoutCategory++;
            }
            setCatIterator = tweet.getSetCategories().iterator();
            while (setCatIterator.hasNext()) {
                String catNumber = setCatIterator.next();
                multisetCategories.add(catNumber);
                if (!mapExample.values().contains(tweet.getText())) {
                    mapExample.put(catNumber, tweet.getText());
                }
            }
        }
        reportClock.closeAndPrintClock();


        System.out.println("---- Omegan report for @HP -----------");
        System.out.println(
                "Number of tweets analyzed: " + originalNumberTweet);
        System.out.println("---------------");
        System.out.println("Number of tweets in English: " + setTweets.size());

        System.out.print(
                "Number of tweets attributed to one or several categories: " + (setTweets.size() - tweetsWithoutCategory));
        System.out.println(" (which means that " + (100 - (Math.round((double) (tweetsWithoutCategory * 10000) / setTweets.size()) / 100)) + "% of all tweets were attributed to a category)");

        System.out.println("---------------");
        System.out.println("Distribution of the tweets in different categories:");
        System.out.println("(note: a tweet can belong to several categories)");
        System.out.println("****");

        Iterator<String> multisetCategoriesIterator = Multisets.copyHighestCountFirst(multisetCategories).elementSet().iterator();
        while (multisetCategoriesIterator.hasNext()) {
            String catNumber = multisetCategoriesIterator.next();
//            System.out.println("cat number: " + catNumber);
            String example = mapExample.get(catNumber);
            System.out.println(Categories.get(catNumber) + ", " + multisetCategories.count(catNumber) + "x");
            System.out.println("Example 1: \"" + example + "\"\n");
            System.out.println("****");
        }
        System.out.println("---------------");
        System.out.println("---End of report---");


    }
}
