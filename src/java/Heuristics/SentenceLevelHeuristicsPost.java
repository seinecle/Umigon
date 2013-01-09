/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import Twitter.TweetLoader;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelHeuristicsPost {

    private String status;
    private Tweet tweet;
    private Heuristic heuristic;

    public SentenceLevelHeuristicsPost(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsMoreThan2Mentions();
        return tweet;
    }


    public void containsMoreThan2Mentions() {
        int countArobase = StringUtils.countMatches(status, "@");
        if (countArobase > 2 & !tweet.getSetCategories().contains("012")) {
            tweet.addToSetCategories("061");
        }
    }
}
