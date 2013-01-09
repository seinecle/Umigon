/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class StatusEligibleHeuristics {

    private String status;
    private Tweet tweet;
    private Heuristic heuristic;
    final private String punctuation = "[\\!\\?\\.'\\\\\"\\-,\\(\\)\\#=]+";

    public StatusEligibleHeuristics(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        isStatusEmpty();
        isStatusGarbled();
        return tweet;
    }

    private void isStatusEmpty() {
        if (status.isEmpty()) {
            tweet.addToSetCategories("002");
        }
    }

    private void isStatusGarbled() {
//        if (status.contains("Social innovation")) {
//            System.out.println("brass monkey");
//        }
        String temp  = status.replaceAll("\\@[^ \t\n]*", "");
        temp = temp.replaceAll("\\#[^ \t\n]*", "");
        temp = temp.replaceAll("http[^ \t\n]*", "");
        temp = temp.replaceAll(" +", " ").trim();
        temp = temp.replaceAll(punctuation,"").trim();
        temp = temp.replaceAll(" +"," ");

        if (temp.length() < 5) {
            tweet.addToSetCategories("002");
        }
        if (temp.split(" ").length < 4) {
            tweet.addToSetCategories("002");
        }
    }
}
