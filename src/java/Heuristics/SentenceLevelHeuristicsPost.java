/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import TextCleaning.StatusCleaner;
import Twitter.ControllerBean;
import Twitter.Tweet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelHeuristicsPost {

    private String status;
    private Tweet tweet;
    final private String punctuation = "[\\!\\?\\.'\\\\\"\\-,\\(\\)\\#=]+";

    public SentenceLevelHeuristicsPost(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsMoreThan2Mentions();
        containsModerator();
        isIronicallyPositive();
//        containsNegation();
        isStatusGarbled();
        return tweet;
    }

    public void containsMoreThan2Mentions() {
        int countArobase = StringUtils.countMatches(status, "@");
        if (countArobase > 2 & !tweet.getSetCategories().contains("012")) {
            tweet.addToSetCategories("061",-1);
        }
    }

    public void isIronicallyPositive() {
        if (tweet.getSetCategories().contains("011") & tweet.getSetCategories().contains("012")) {
            for (String term : ControllerBean.Hloader.getSetIronicallyPositive()) {
                if (status.contains(term)) {
                    tweet.deleteFromSetCategories("012");
                }
            }
        }
    }

    public void containsNegation() {
        StatusCleaner statusCleaner = new StatusCleaner();
        status = statusCleaner.removePunctuationSigns(status).toLowerCase().trim();

        Set<String> termsInStatus = new HashSet();
        termsInStatus.addAll(Arrays.asList(status.split(" ")));
        if (!tweet.getSetCategories().isEmpty()) {
            return;
        }
        for (String term : ControllerBean.Hloader.setNegations) {
            if (termsInStatus.contains(term)) {
                tweet.addToSetCategories("012",-1);
            }
        }
    }

    public void containsModerator() {
        StatusCleaner statusCleaner = new StatusCleaner();
        status = statusCleaner.removePunctuationSigns(status).toLowerCase().trim();
        HashSet setModerators = new HashSet();
        setModerators.add("but");

        Set<String> termsInStatus = new HashSet();
        termsInStatus.addAll(Arrays.asList(status.split(" ")));
        for (String term : ControllerBean.Hloader.setNegations) {
            if (termsInStatus.contains(term)) {
//                tweet.addToSetCategories("012");
            }
        }
    }

    private void isStatusGarbled() {
//        if (status.contains("Social innovation")) {
//            System.out.println("brass monkey");
//        }
        if (!tweet.getSetCategories().isEmpty()) {
            return;
        }
        String temp = status.replaceAll("\\@[^ \t\n]*", "");
        temp = temp.replaceAll("\\#[^ \t\n]*", "");
        temp = temp.replaceAll("http[^ \t\n]*", "");
        temp = temp.replaceAll(" +", " ").trim();
        temp = temp.replaceAll(punctuation, "").trim();
        temp = temp.replaceAll(" +", " ");

        if (temp.length() < 5) {
            tweet.addToSetCategories("002",-1);
        }
        if (temp.split(" ").length < 4) {
            tweet.addToSetCategories("002",-1);
        }
    }
}
