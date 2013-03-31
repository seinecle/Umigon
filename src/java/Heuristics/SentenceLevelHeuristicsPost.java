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
        if (countArobase > 2 & !tweet.getSetCategories().contains("012")) {
            tweet.addToSetCategories("061", -1);
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
                tweet.addToSetCategories("012", -1);
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
                indexesPos = (Set<Integer>) tweet.getMapCategoriesToIndex().get("011");
                indexesNeg = (Set<Integer>) tweet.getMapCategoriesToIndex().get("012");
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
                        tweet.deleteFromSetCategories("011");
                        System.out.println("moderator detected, positive sentiment deleted");
                        System.out.println("tweet: " + tweet.getText());
                    }
                    if (indexPos > index && indexNeg < index) {
                        tweet.deleteFromSetCategories("012");
                        System.out.println("moderator detected, negative sentiment deleted");
                        System.out.println("tweet: " + tweet.getText());
                    }
                }


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
            tweet.addToSetCategories("002", -1);
        }
        if (temp.split(" ").length < 4) {
            tweet.addToSetCategories("002", -1);
        }
    }

    private void whenAllElseFailed() {
        //what to do when a tweet contains both positive and negative markers?
        //classify it as negative, except if it ends by a positive final note
        if (tweet.getSetCategories().contains("011") & tweet.getSetCategoriesToString().contains("012")) {
            if (tweet.getFinalNote() == null || tweet.getFinalNote() == -1) {
                tweet.deleteFromSetCategories("011");
            } else if (tweet.getFinalNote() == 1) {
                tweet.deleteFromSetCategories("012");
            }
        }

        //indicates when a tweet is positive without being promotted
        if (tweet.getSetCategories().contains("011") & !tweet.getSetCategoriesToString().contains("061")) {
//                System.out.println("positive tweets without promotion: (user: "+tweet.getUser()+") "+tweet.getText());
            tweet.addToSetCategories("0111", -1);
        }

    }
}
