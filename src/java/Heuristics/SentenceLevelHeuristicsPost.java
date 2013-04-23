/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import TextCleaning.StatusCleaner;
import Admin.ControllerBean;
import Twitter.Tweet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
        whenAllElseFailed();
        return tweet;
    }

    public void containsMoreThan2Mentions() {
        int countArobase = StringUtils.countMatches(status, "@");
        if (countArobase > 2 & !tweet.getListCategories().contains("012")) {
            tweet.addToListCategories("061", -1);
        }
    }

    public void isIronicallyPositive() {
        if (tweet.getListCategories().contains("011") & tweet.getListCategories().contains("012")) {
            for (String term : ControllerBean.Hloader.getSetIronicallyPositive()) {
                if (status.contains(term)) {
                    tweet.deleteFromListCategories("012");
                }
            }
        }
    }

    public void containsNegation() {
        StatusCleaner statusCleaner = new StatusCleaner();
        status = statusCleaner.removePunctuationSigns(status).toLowerCase().trim();

        Set<String> termsInStatus = new HashSet();
        termsInStatus.addAll(Arrays.asList(status.split(" ")));
        if (!tweet.getListCategories().isEmpty()) {
            return;
        }
        for (String term : ControllerBean.Hloader.setNegations) {
            if (termsInStatus.contains(term)) {
                tweet.addToListCategories("012", -1);
            }
        }
    }

    public void containsModerator() {
        StatusCleaner statusCleaner = new StatusCleaner();
        int index;
        int indexPos = 0;
        int indexNeg = 0;
        String negCat;
        status = statusCleaner.removePunctuationSigns(status).toLowerCase().trim();

        Set<String> termsInStatus = new HashSet();
        termsInStatus.addAll(Arrays.asList(status.split(" ")));
        Iterator<Integer> iterator;
        for (String term : ControllerBean.Hloader.setModerators) {
            if (termsInStatus.contains(term)) {
                Set<Integer> indexesPos;
                Set<Integer> indexesNeg;
                index = status.indexOf(term);
                indexesPos = tweet.getAllIndexesForCategory("011");
                indexesNeg = tweet.getAllIndexesForCategory("012");
                if (!indexesPos.isEmpty() && !indexesNeg.isEmpty()) {
                    iterator = indexesPos.iterator();
                    while (iterator.hasNext()) {
                        Integer currIndex = iterator.next();
                        if (indexPos < currIndex) {
                            indexPos = currIndex;
                        }
                    }
                    iterator = indexesNeg.iterator();
                    while (iterator.hasNext()) {
                        Integer currIndex = iterator.next();
                        if (indexNeg < currIndex) {
                            indexNeg = currIndex;
                        }
                    }
                    if (indexPos < index && indexNeg > index) {
                        if (term.equals("even if") ||term.equals("despite") ||term.equals("in spite of")||term.equals("although")) {
                            tweet.deleteFromListCategories("012");
                        } else {
                            tweet.deleteFromListCategories("011");
                        }
//                        System.out.println("moderator detected, positive sentiment deleted");
//                        System.out.println("tweet: " + tweet.getText());
                    }
                    if (indexPos > index && indexNeg < index) {
                        if (term.equals("even if") ||term.equals("despite") ||term.equals("in spite of")||term.equals("although")) {
                            tweet.deleteFromListCategories("011");
                        } else {
                            tweet.deleteFromListCategories("012");
                        }
//                        System.out.println("moderator detected, negative sentiment deleted");
//                        System.out.println("tweet: " + tweet.getText());
                    }
                    if (index<indexPos && indexPos < indexNeg) {
                        if (term.equals("even if") ||term.equals("despite") ||term.equals("in spite of")||term.equals("although")) {
                            tweet.deleteFromListCategories("011");
                        }
                    }
                    if (index<indexNeg && indexNeg < indexPos) {
                        if (term.equals("even if") ||term.equals("despite") ||term.equals("in spite of")||term.equals("although")) {
                            tweet.deleteFromListCategories("012");
                        }
                    }
                }


            }
        }
    }

    private void isStatusGarbled() {
//        if (status.contains("Social innovation")) {
//            System.out.println("brass monkey");
//        }
        if (!tweet.getListCategories().isEmpty()) {
            return;
        }
        String temp = status.replaceAll("\\@[^ \t\n]*", "");
        temp = temp.replaceAll("\\#[^ \t\n]*", "");
        temp = temp.replaceAll("http[^ \t\n]*", "");
        temp = temp.replaceAll(" +", " ").trim();
        temp = temp.replaceAll(punctuation, "").trim();
        temp = temp.replaceAll(" +", " ");

        if (temp.length() < 5) {
            tweet.addToListCategories("002", -1);
        }
        if (temp.split(" ").length < 4) {
            tweet.addToListCategories("002", -1);
        }
    }

    private void whenAllElseFailed() {
        //what to do when a tweet contains both positive and negative markers?
        //classify it as negative, except if it ends by a positive final note
        if (tweet.getListCategories().contains("011") & tweet.getListCategories().contains("012")) {
            if (tweet.getFinalNote() == null || tweet.getFinalNote() == -1) {
                tweet.deleteFromListCategories("011");
            } else if (tweet.getFinalNote() == 1) {
                tweet.deleteFromListCategories("012");
            }
        }

        //indicates when a tweet is positive without being promotted
        if (tweet.getListCategories().contains("011") & !tweet.getListCategories().contains("061")) {
//                System.out.println("positive tweets without promotion: (user: "+tweet.getUser()+") "+tweet.getText());
            tweet.addToListCategories("0111", -1);
        }

    }
}
