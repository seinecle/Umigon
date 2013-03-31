/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.ResultsExporter;
import Twitter.Tweet;
import Utils.Clock;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

/**
 *
 * @author C. Levallois
 */
public class TweetLooper {

    ArrayList<Tweet> setTweets;
    ArrayList<Tweet> setTweetsToReturn;
    public Multiset<String> setLangDetected;
    Iterator<String> setCatIterator;
    Tweet tweet;
    Iterator<Tweet> setTweetsIterator;
    int originalNumberTweet;
    int countPositiveTweets;
    int countNegativeTweets;
    int countNeutralTweets;
    int positiveCorrectlyRetrieved;
    int negativeCorrectlyRetrieved;
    int neutralCorrectlyRetrieved;
    private int positiveRetrieved;
    private int negativeRetrieved;
    private int neutralRetrieved;

    public TweetLooper(ArrayList<Tweet> setTweets) {
        this.setTweets = setTweets;
        originalNumberTweet = setTweets.size();
    }

    public ArrayList<Tweet> applyLevel1(boolean loadFromTrainingFile) throws LangDetectException, IOException {

        ClassifierMachine cm = new ClassifierMachine(loadFromTrainingFile);
        setTweets = cm.classify(setTweets);
        setTweetsIterator = setTweets.iterator();
        setTweetsToReturn = new ArrayList();
        boolean printAllTweets = true;

        Clock reportClock = new Clock("generating report");

//        ResultsExporter io = new ResultsExporter(setTweets, "D:\\Docs Pro Clement\\E-projects\\SEMEVAL 2013\\task2-Umigon-B-Twitter-constrained.output");
//        io.writeSemeValOutput();

        setTweetsIterator = setTweets.iterator();
        Multiset<String> multisetCategories = HashMultiset.create();
        Map<String, String> mapExample = new HashMap();
        int tweetsWithoutCategory = 0;
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();



            if (printAllTweets) {
                System.out.println("categories " + tweet.getSetCategoriesToString());
                System.out.println(tweet.getText());
            }

            if (tweet.getSetCategories().isEmpty()) {
                tweetsWithoutCategory++;
            }


//            if (tweet.getSetCategories().contains("0621")) {
//                System.out.println("facts: (user: " + tweet.getUser() + ") " + tweet.getText());
//            }
//            System.out.println("user: " + tweet.getUser());
//            System.out.println("tweet: " + tweet.getText());
//            System.out.println("categories: " + tweet.getSetCategoriesString());


            if (loadFromTrainingFile) {
                if (tweet.getTrainingSetCat().equals("positive") || tweet.getTrainingSetCat().equals("4")) {
                    countPositiveTweets++;
                    if (tweet.getSetCategories().contains("011")) {
                        positiveCorrectlyRetrieved++;
                    } else {
                        System.out.println("positive tweet recognized as " + tweet.getSetCategoriesToString());
                        System.out.println(tweet.getText());
                    }
                }
                if (tweet.getTrainingSetCat().equals("negative") || tweet.getTrainingSetCat().equals("0")) {
                    countNegativeTweets++;
                    if (tweet.getSetCategories().contains("012")) {
                        negativeCorrectlyRetrieved++;
                    } else {
                        System.out.println("negative tweet recognized as " + tweet.getSetCategoriesToString());
                        System.out.println(tweet.getText());
                    }

                }

                if (tweet.getTrainingSetCat().contains("neutral") || tweet.getTrainingSetCat().contains("objective") || tweet.getTrainingSetCat().equals("2")) {
                    countNeutralTweets++;
                    if (!tweet.getSetCategories().contains("012") & !tweet.getSetCategories().contains("011")) {
                        neutralCorrectlyRetrieved++;
                    } else {
                        System.out.println("neutral tweet recognized as " + tweet.getSetCategoriesToString());
                        System.out.println(tweet.getText());
                    }
                }


            }

            if (tweet.getSetCategories().contains("011")) {
                positiveRetrieved++;
            }

            if (tweet.getSetCategories().contains("012")) {
                negativeRetrieved++;
            }

            if (!tweet.getSetCategories().contains("012") & !tweet.getSetCategories().contains("011")) {
                neutralRetrieved++;
            }


            setCatIterator = tweet.getSetCategories().iterator();
            while (setCatIterator.hasNext()) {
                String catNumber = setCatIterator.next();
                multisetCategories.add(catNumber);
                if (!mapExample.values().contains(tweet.getText())) {
                    mapExample.put(catNumber, tweet.getText());
                }
            }
            setTweetsToReturn.add(tweet);
        }

        reportClock.closeAndPrintClock();

        System.out.println(
                "---- Omegan report for @HP -----------");
        System.out.println(
                "Number of tweets analyzed: " + originalNumberTweet);

        System.out.print(
                "Number of tweets attributed to one or several categories: " + (setTweets.size() - tweetsWithoutCategory));
        System.out.println(
                " (which means that " + (100 - ((double) (tweetsWithoutCategory
                * 10000) / setTweets.size()) / 100) + "% of all tweets were attributed to a category)");

        System.out.println(
                "---------------");
        System.out.println(
                "Distribution of the tweets in different categories:");
        System.out.println(
                "(note: a tweet can belong to several categories)");
        System.out.println(
                "****");

        Iterator<String> multisetCategoriesIterator = Multisets.copyHighestCountFirst(multisetCategories).elementSet().iterator();

        while (multisetCategoriesIterator.hasNext()) {
            String catNumber = multisetCategoriesIterator.next();
//            System.out.println("cat number: " + catNumber);
            String example = mapExample.get(catNumber);
            System.out.println(Categories.get(catNumber) + ", " + multisetCategories.count(catNumber) + "x");
            System.out.println("Example 1: \"" + example + "\"\n");
            System.out.println("****");
        }

        System.out.println(
                "---------------");
        System.out.println(
                "---End of report---");

        if (loadFromTrainingFile) {
            System.out.println(
                    "number of positive tweets in the training set: " + countPositiveTweets);
            System.out.println(
                    "number of negative tweets in the training set: " + countNegativeTweets);
            System.out.println(
                    "number of neutral tweets in the training set: " + countNeutralTweets);
            int totalTweets = countPositiveTweets + countNegativeTweets + countNeutralTweets;

            System.out.println(
                    "total (should be 500): " + (totalTweets));

            System.out.println(
                    "");
            float ppos = (float) positiveCorrectlyRetrieved / positiveRetrieved;
            float pneg = (float) negativeCorrectlyRetrieved / negativeRetrieved;
            float pneut = (float) neutralCorrectlyRetrieved / neutralRetrieved;

            System.out.println(
                    "Precision for positive: " + (ppos));
            System.out.println(
                    "Precision for negative: " + (pneg));
            System.out.println(
                    "Precision for neutral: " + (pneut));

            System.out.println(
                    "");
            float rpos = (float) positiveCorrectlyRetrieved / countPositiveTweets;
            float rneg = (float) negativeCorrectlyRetrieved / countNegativeTweets;
            float rneut = (float) neutralCorrectlyRetrieved / countNeutralTweets;

            System.out.println(
                    "Recall for positive: " + (rpos));
            System.out.println(
                    "Recall for negative: " + (rneg));
            System.out.println(
                    "Recall for neutral: " + (rneut));

            System.out.println(
                    "");
            float fpos = 2 * (ppos * rpos) / (ppos + rpos);
            float fneg = 2 * (pneg * rneg) / (pneg + rneg);
            float fneut = 2 * (pneut * rneut) / (pneut + rneut);

            System.out.println(
                    "F1 for positive: " + (fpos));
            System.out.println(
                    "F1 for negative: " + (fneg));
            System.out.println(
                    "F1 for neutral: " + (fneut));

            System.out.println(
                    "");
            System.out.println(
                    "F1 overall: " + ((fpos * countPositiveTweets + fneg * countNegativeTweets + fneut * countNeutralTweets) / totalTweets));

        }
        return setTweetsToReturn;
    }
}
