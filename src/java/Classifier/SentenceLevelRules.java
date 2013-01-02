/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.Tweet;
import Twitter.TweetLoader;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelRules {

    private String status;
    private Tweet tweet;
    private Heuristic heuristic;

    public SentenceLevelRules(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsPercentage();
        return tweet;
    }

    public void containsPercentage() {
        boolean res = (status.matches(".*\\d%.*") | status.matches(".*\\d/\\d.*"));
        if (res) {
            tweet.addToSetCategories(621);
        }
    }
    public void containsTimeIndication() {
        for (String term: TweetLoader.Hloader.mapH4.keySet()){
            if (status.contains(term)){
                heuristic = TweetLoader.Hloader.getMapH4().get(term);
                heuristic.checkFeatures(status, term); ? tweet.addToSetCategories(11) : tweet.addToSetCategories(12);
                
            }
        }
    }
}
