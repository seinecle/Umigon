/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import Twitter.TweetLoader;
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

    public HashtagLevelHeuristics(Tweet tweet) {
        this.tweet = tweet;
        this.listHashtags = tweet.getHashtags();

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
        for (String term : TweetLoader.Hloader.getMapH13().keySet()) {
            if (hashtag.startsWith(term)) {
                heuristic = TweetLoader.Hloader.getMapH13().get(term);
                result = heuristic.checkFeatures(tweet.getText(), hashtag);
                // System.out.println("result: " + result);
                if (result != null) {
                    tweet.addToSetCategories(result);
                    break;
                }

            }
        }
    }

    private void isHashTagStartingWithAnOpinion() {

        for (String term : TweetLoader.Hloader.getMapH1().keySet()) {
            if (hashtag.startsWith(term)) {
                tweet.addToSetCategories("011");
                break;
            }
        }
        for (String term : TweetLoader.Hloader.getMapH2().keySet()) {
            if (hashtag.startsWith(term)) {
                tweet.addToSetCategories("012");
                break;
            }
        }
    }

    private void isHashTagContainingAnOpinion() {
        for (String term : TweetLoader.Hloader.getMapH1().keySet()) {
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (TweetLoader.Hloader.getSetNegations().contains(hashtag)) {
                        tweet.addToSetCategories("012");
                        break;
                    }
                    if (TweetLoader.Hloader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToSetCategories("011");
                        tweet.addToSetCategories("022");
                        break;
                    }
                } else {
                    tweet.addToSetCategories("011");
                    break;

                }
            }
        }
        for (String term : TweetLoader.Hloader.getMapH2().keySet()) {
            if (hashtag.contains(term)) {
                hashtag = StringUtils.removeEnd(hashtag, term);
                if (hashtag.length() > 1) {
                    if (TweetLoader.Hloader.getSetNegations().contains(hashtag)) {
                        tweet.addToSetCategories("011");
                        break;
                    }
                    if (TweetLoader.Hloader.getMapH3().keySet().contains(hashtag)) {
                        tweet.addToSetCategories("012");
                        tweet.addToSetCategories("022");
                        break;
                    }
                } else {
                    tweet.addToSetCategories("012");
                    break;

                }
            }
        }
    }
}
