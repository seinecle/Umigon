/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import javax.ejb.Stateless;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class StatusEligibleHeuristics {

    private String status;
    private Tweet tweet;

    public StatusEligibleHeuristics() {
    }

    public Tweet applyRules(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
        isStatusEmpty();
        return tweet;
    }

    private void isStatusEmpty() {
        if (status.isEmpty()) {
            tweet.addToListCategories("002", -1);
        }
    }

}