/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import Twitter.ControllerBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class HashtagLevelHeuristics {

    private Tweet tweet;
    private Heuristic heuristic;
    private HashSet<String> hashtags;
    private Iterator<String> hashtagsIterator;
    private String hashtag;
    private String result;
    private List<String> listHashtags;

    public HashtagLevelHeuristics(Tweet tweet, boolean loadFromTrainingFile) {
        this.tweet = tweet;
        this.listHashtags = tweet.getHashtags();
        if (loadFromTrainingFile & StringUtils.contains(tweet.getText(),"#")) {
            String[] terms = tweet.getText().split(" ");
            this.listHashtags = new ArrayList();
            for (String string : terms) {
                if (string.startsWith("#")) {
                    this.listHashtags.add(string.replace("#", ""));
                }
            }
        }
    }

    public Tweet applyRules() {
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
            isContainedInHashTagHeuristics();
            isHashTagContainingAnOpinion();
            isHashTagStartingWithAnOpinion();
        }
        return tweet;
    }

    private void isContainedInHashTagHeuristics() {
        for (String term : ControllerBean.Hloader.getMapH13().keySet()) {
            if (hashtag.contains(term)) {
                heuristic = ControllerBean.Hloader.getMapH13().get(term);
                result = heuristic.checkFeatures(tweet.getText(), hashtag);
                // System.out.println("result: " + result);
                if (result != null) {
                    tweet.addToSetCategories(result,-2);
                    break;
                }

            }
        }
    }

    private void isHashTagStartingWithAnOpinion() {
        boolean startsWithNegativeTerm = false;
        for (String term : ControllerBean.Hloader.getMapH3().keySet()) {
            if (hashtag.startsWith(term)) {
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }
        for (String term : ControllerBean.Hloader.setNegations) {
            if (hashtag.startsWith(term)) {
                startsWithNegativeTerm = true;
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }
        for (String term : ControllerBean.Hloader.getMapH3().keySet()) {
            if (hashtag.startsWith(term)) {
                hashtag = StringUtils.removeStart(hashtag, term);
            }
        }

        for (String term : ControllerBean.Hloader.getMapH1().keySet()) {
            if (hashtag.startsWith(term)) {
                if (!ControllerBean.Hloader.getSetFalsePositiveOpinions().contains(hashtag)) {
                    if (!startsWithNegativeTerm) {
                        tweet.addToSetCategories("011",-2);
                        break;
                    } else {
                        tweet.addToSetCategories("012",-2);
                        break;
                    }
                }
            }
        }
        for (String term : ControllerBean.Hloader.getMapH2().keySet()) {
            if (hashtag.startsWith(term)) {
                if (!ControllerBean.Hloader.getSetFalsePositiveOpinions().contains(hashtag)) {
                    if (!startsWithNegativeTerm) {
                        tweet.addToSetCategories("012",-2);
                        break;
                    } else {
                        tweet.addToSetCategories("011",-2);
                        break;
                    }
                }
            }
        }
    }

    private void isHashTagContainingAnOpinion() {
        for (String term : ControllerBean.Hloader.getMapH1().keySet()) {
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (ControllerBean.Hloader.getSetNegations().contains(hashtag)) {
                        tweet.addToSetCategories("012",-2);
                        break;

                    }
                    if (ControllerBean.Hloader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToSetCategories("011",-2);
                        tweet.addToSetCategories("022",-2);
                        break;
                    }
                } else {
                    tweet.addToSetCategories("011",-2);
                    break;

                }
            }
        }
        for (String term : ControllerBean.Hloader.getMapH2().keySet()) {
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (ControllerBean.Hloader.getSetNegations().contains(hashtag)) {
                        tweet.addToSetCategories("011",-2);
                        break;
                    }
                    if (ControllerBean.Hloader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToSetCategories("012",-2);
                        tweet.addToSetCategories("022",-2);
                        break;
                    }
                } else {
                    tweet.addToSetCategories("012",-2);
                    break;

                }
            }
        }
    }
}
