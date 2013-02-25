/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Heuristics.Heuristic;
import Twitter.Tweet;
import Twitter.ControllerBean;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelHeuristicsPre {

    private String status;
    private Tweet tweet;
    private Heuristic heuristic;

    public SentenceLevelHeuristicsPre(Tweet tweet, String status) {
        this.status = status;
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsPercentage();
        containsPunctuation();
        containsOnomatopaes();
        isAClientTweet();
        isARetweetOfAClientTweet();
        return tweet;
    }

    public void containsPercentage() {
        //do we find a percentage?
        boolean res = status.matches(".*\\d%.*");
        if (res) {
            //if so, is it followed by "off"?
            res = (status.toLowerCase().matches(".*\\d% off.*") | status.toLowerCase().matches(".*\\d% cash back.*"));
            if (res) {
                tweet.addToSetCategories("0611");
            } else {
                tweet.addToSetCategories("0621");

            }
        }
    }

    public void containsOnomatopaes() {
        //awwww
        boolean res = status.matches(".*aww+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //yesssss
        res = status.toLowerCase().matches(".*yess+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //ewwww
        res = status.toLowerCase().matches(".*[^n]eww+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }
        //arrrgh
        res = status.toLowerCase().matches(".*arr+g.*");
        if (res) {
            tweet.addToSetCategories("012");
        }
        //ahahaha
        res = status.toLowerCase().matches(".*haha.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //LMFAO
        res = status.toLowerCase().matches(".*lmfao+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //LMAO
        res = status.toLowerCase().matches(".*lmao+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //yeaaaa
        res = status.toLowerCase().matches(".*yeaa+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }
        //noooooo
        res = status.toLowerCase().matches(".*nooo+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }
    }

    public void containsPunctuation() {
        //multiple exclamation marks
        boolean res = status.matches(".*!!+.*");
        if (res) {
            tweet.addToSetCategories("022");
        }

        //smiley :)
        res = status.matches(".*:\\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :]
        res = status.matches(".*:\\]+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley ^__^
        res = status.matches(".*\\^_+\\^.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :O
        res = status.matches(".*:O .*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley ;p
        res = status.matches(".*;p+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :-p
        res = status.matches(".*:-p+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :|
        res = status.matches(".*:\\|+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley :/
        res = status.matches(".*:/+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley =(
        res = status.matches(".*=\\(+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley : )
        res = status.matches(".*: \\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :-)
        res = status.matches(".*:-\\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :-(
        res = status.matches(".*:-\\(+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley :-/
        res = status.matches(".*:-/+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley :'(
        res = status.matches(".*:'\\(+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley :(
        res = status.matches(".*:\\(+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //smiley (:
        res = status.matches(".*\\(:+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley ;)
        res = status.matches(".*;\\)+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //smiley :D
        res = status.matches(".*:D.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

        //question mark
        res = status.matches(".*\\?+.*");
        if (res) {
            tweet.addToSetCategories("040");
        }

        //question mark with exclamation: ?!
        res = status.matches(".*\\?+\\!+.*");
        if (res) {
            tweet.addToSetCategories("012");
        }

        //kisses
        res = status.toLowerCase().matches(".*xx+.*");
        if (res) {
            tweet.addToSetCategories("011");
        }

    }

    public void containsTimeIndication() {
        for (String term : ControllerBean.Hloader.mapH4.keySet()) {
            if (status.contains(term)) {
                heuristic = ControllerBean.Hloader.getMapH12().get(term);
                String result = heuristic.checkFeatures(status, term);
                if (result != null) {
                    tweet.addToSetCategories(result);
                }
            }
        }
    }

    public void containsMoreThan2Mentions() {
        int countArobase = StringUtils.countMatches(status, "@");
        if (countArobase > 2) {
        }
    }

    public void isAClientTweet() {
        if (tweet.getUser().toLowerCase().equals(ControllerBean.getClient())) {
            tweet.addToSetCategories("0612");
        }
    }

    public void isARetweetOfAClientTweet() {
        if (status.toLowerCase().contains("rt @" + ControllerBean.getClient())) {
            tweet.addToSetCategories("06121");
        }
    }
}
