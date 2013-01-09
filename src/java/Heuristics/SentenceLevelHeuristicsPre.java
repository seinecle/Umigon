/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Heuristics.Heuristic;
import Twitter.Tweet;
import Twitter.TweetLoader;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelHeuristicsPre {

    private String status;
    private Tweet tweet;
    private Heuristic heuristic;

    public SentenceLevelHeuristicsPre(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsPercentage();
        containsPunctuation();
        return tweet;
    }

    public void containsPercentage() {
        boolean res = status.matches(".*\\d%.*");
        if (res) {
            tweet.addToSetCategories("0621");
        }
    }

    public void containsPunctuation() {
        //multiple exclamation marks
        boolean res = status.matches(".*!!+.*");
        if (res) {
            tweet.addToSetCategories("022");
        }

        //smiley :)
        res = status.matches(".*:\\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :O
        res = status.matches(".*:O*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :|
        res = status.matches(".*:\\|*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley :(
        res = status.matches(".*:\\(+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley ;)
        res = status.matches(".*;\\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //question mark
        res = status.matches(".*\\?+.*");
        if (res) {
            tweet.addToSetCategories("040");
        }

        //kisses
        res = status.matches(".*xx?+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

    }

    public void containsTimeIndication() {
        for (String term : TweetLoader.Hloader.mapH4.keySet()) {
            if (status.contains(term)) {
                heuristic = TweetLoader.Hloader.getMapH12().get(term);
                String result = heuristic.checkFeatures(status, term);
                if (result != null) {
                    tweet.addToSetCategories(result);
                }
            }
        }
    }

    public void containsMoreThan2Mentions() {
        int countArobase = StringUtils.countMatches(status, "@");
        if (countArobase > 2) {
        }
    }
}
