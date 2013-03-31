/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;

/**
 *
 * @author C. Levallois
 */
public class StatusEligibleHeuristics {

    private String status;
    private Tweet tweet;

    public StatusEligibleHeuristics(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        isStatusEmpty();
        return tweet;
    }

    private void isStatusEmpty() {
        if (status.isEmpty()) {
            tweet.addToSetCategories("002",-1);
        }
    }

}
