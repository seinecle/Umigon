/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import RuleInterpreter.Interpreter;
import Singletons.HeuristicsLoader;
import Twitter.Tweet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class HashtagLevelHeuristics {

    private Tweet tweet;
    private Heuristic heuristic;
    private HashSet<String> hashtags;
    private Iterator<String> hashtagsIterator;
    private String hashtag;
    private String result;
    private List<String> listHashtags;

    @Inject
    HeuristicsLoader HLoader;
    @Inject
    TermLevelHeuristics termLevelHeuristic;

    public HashtagLevelHeuristics() {
    }

    public Tweet applyRules(Tweet tweet) {
        this.tweet = tweet;

        this.listHashtags = tweet.getHashtags();
        if (this.listHashtags == null & StringUtils.contains(tweet.getText(), "#")) {
            String[] terms = tweet.getText().split(" ");
            this.listHashtags = new ArrayList();
            for (String string : terms) {
                if (string.startsWith("#")) {
                    this.listHashtags.add(string.replace("#", ""));
                }
            }
        }

        if (listHashtags != null) {
            hashtags = new HashSet(listHashtags);
//            System.out.println("tweet considered");
        } else {
//            System.out.println("tweet returned");
            return tweet;
        }
        hashtagsIterator = hashtags.iterator();
        while (hashtagsIterator.hasNext()) {
            hashtag = hashtagsIterator.next().toLowerCase();
//            if (hashtag.startsWith("stop")) {
//                System.out.println("stop");
//            }
            if (isContainedInHashTagHeuristics()) {
                return tweet;
            }
            isHashTagContainingAnOpinion();
            isHashTagStartingWithAnOpinion();
        }
        return tweet;
    }

    private boolean isContainedInHashTagHeuristics() {
        for (String term : HLoader.getMapH13().keySet()) {
            if (hashtag.contains(term)) {
                heuristic = HLoader.getMapH13().get(term);
                result = termLevelHeuristic.checkFeatures(heuristic, tweet.getText(), hashtag);
                // System.out.println("result: " + result);
                if (result != null) {
                    tweet.addToListCategories(result, -2);
                    return true;
                }

            }
        }
        return false;
    }

    private void isHashTagStartingWithAnOpinion() {
        boolean startsWithNegativeTerm = false;
        for (String term : HLoader.getMapH3().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.startsWith(term)) {
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }
        for (String term : HLoader.getSetNegations()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.startsWith(term)) {
                startsWithNegativeTerm = true;
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }
        for (String term : HLoader.getMapH3().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.startsWith(term)) {
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }

        for (String term : HLoader.getMapH1().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.startsWith(term)) {
                if (!HLoader.getSetFalsePositiveOpinions().contains(term)) {
                    if (!startsWithNegativeTerm) {
                        tweet.addToListCategories("011", -2);
                        break;
                    } else {
                        tweet.addToListCategories("012", -2);
                        break;
                    }
                }
            }
        }
        for (String term : HLoader.getMapH2().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.startsWith(term)) {
                if (!HLoader.getSetFalsePositiveOpinions().contains(term)) {
                    if (!startsWithNegativeTerm) {
                        tweet.addToListCategories("012", -2);
                        break;
                    } else {
                        tweet.addToListCategories("011", -2);
                        break;
                    }
                }
            }
        }
    }

    private void isHashTagContainingAnOpinion() {
        for (String term : HLoader.getMapH1().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (HLoader.getSetNegations().contains(hashtag)) {
                        tweet.addToListCategories("012", -2);
                        break;

                    }
                    if (HLoader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToListCategories("011", -2);
                        tweet.addToListCategories("022", -2);
                        break;
                    } else {
                        if (hashtag.equals(term)) {
                            tweet.addToListCategories("011", -2);
                            break;
                        }
                    }
                } else {
                    tweet.addToListCategories("011", -2);
                    break;

                }
            }
        }
        for (String term : HLoader.getMapH2().keySet()) {
            if (term.length() < 4) {
                continue;
            }
            term = term.replace(" ", "");
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (HLoader.getSetNegations().contains(hashtag)) {
                        tweet.addToListCategories("011", -2);
                        break;
                    }
                    if (HLoader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToListCategories("012", -2);
                        tweet.addToListCategories("022", -2);
                        break;
                    } else {
                        if (hashtag.equals(term)) {
                            tweet.addToListCategories("012", -2);
                            break;
                        }
                    }
                } else {
                    tweet.addToListCategories("012", -2);
                    break;

                }
            }
        }
    }
}
