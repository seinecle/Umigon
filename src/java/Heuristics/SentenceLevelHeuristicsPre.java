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
                tweet.addToSetCategories("0611", -1);
            } else {
                tweet.addToSetCategories("0621", -1);

            }
        }
    }

    public void containsOnomatopaes() {
        //awwww
        int index = 0;
        boolean res = status.matches(".*aww+.*");
        if (res) {
            index = status.indexOf("aww");
            tweet.addToSetCategories("011", index);
        }
        //yesssss
        res = status.toLowerCase().matches(".*yess+.*");
        if (res) {
            index = status.indexOf("yess");
            tweet.addToSetCategories("011", index);
        }
        //ewwww
        res = status.toLowerCase().matches(".*[^n]eww+.*");
        if (res) {
            index = status.indexOf("eww");
            tweet.addToSetCategories("012", index);
        }
        //arrrgh
        res = status.toLowerCase().matches(".*arr+g.*");
        if (res) {
            index = status.indexOf("arr");
            tweet.addToSetCategories("012", index);
        }
        //ahahaha
        res = status.toLowerCase().matches(".*haha.*");
        if (res) {
            index = status.indexOf("haha");
            tweet.addToSetCategories("011", index);
        }
        //LMFAO
        res = status.toLowerCase().matches(".*lmfao+.*");
        if (res) {
            index = status.indexOf("lmfao");
            tweet.addToSetCategories("011", index);
        }
        //LMAO
        res = status.toLowerCase().matches(".*lmao+.*");
        if (res) {
            index = status.indexOf("lmao");
            tweet.addToSetCategories("011", index);
        }
        //yeaaaa
        res = status.toLowerCase().matches(".*yeaa+.*");
        if (res) {
            index = status.indexOf("yeaa");
            tweet.addToSetCategories("011", index);
        }
        //noooooo
        res = status.toLowerCase().matches(".*nooo+.*");
        if (res) {
            index = status.indexOf("noo");
            tweet.addToSetCategories("012", index);
        }
    }

    public void containsPunctuation() {
        int index = 0;

        //multiple exclamation marks
        boolean res = status.matches(".*!!+.*");
        if (res) {
            index = status.indexOf("!!");
            tweet.addToSetCategories("022", index);
        }

        //smiley :)
        res = status.matches(".*:\\)+.*");
        if (res) {
            index = status.indexOf(":)");
            tweet.addToSetCategories("011", index);
        }

        //smiley :]
        res = status.matches(".*:\\]+.*");
        if (res) {
            index = status.indexOf(":]");
            tweet.addToSetCategories("011", index);
        }

        //smiley ^__^
        res = status.matches(".*\\^_+\\^.*");
        if (res) {
            index = status.indexOf("^_");
            tweet.addToSetCategories("011", index);
        }

        //smiley :O
        res = status.matches(".*:O .*");
        if (res) {
            index = status.indexOf(":O");
            tweet.addToSetCategories("011", index);
        }

        //smiley ;p
        res = status.matches(".*;p+.*");
        if (res) {
            index = status.indexOf(";p");
            tweet.addToSetCategories("011", index);
        }

        //smiley :-p
        res = status.matches(".*:-p+.*");
        if (res) {
            index = status.indexOf(":-p");
            tweet.addToSetCategories("011", index);
        }

        //smiley :|
        res = status.matches(".*:\\|+.*");
        if (res) {
            index = status.indexOf(":|");
            tweet.addToSetCategories("012", index);
        }

        //smiley :/
        res = status.matches(".*:/+.*");
        if (res) {
            index = status.indexOf(":/");
            tweet.addToSetCategories("012", index);
        }

        //smiley =(
        res = status.matches(".*=\\(+.*");
        if (res) {
            index = status.indexOf("=(");
            tweet.addToSetCategories("012", index);
        }

        //smiley : )
        res = status.matches(".*: \\)+.*");
        if (res) {
            index = status.indexOf(": )");
            tweet.addToSetCategories("011", index);
        }

        //smiley :-)
        res = status.matches(".*:-\\)+.*");
        if (res) {
            index = status.indexOf(":-)");
            tweet.addToSetCategories("011", index);
        }

        //smiley :-(
        res = status.matches(".*:-\\(+.*");
        if (res) {
            index = status.indexOf(":-(");
            tweet.addToSetCategories("012", index);
        }

        //smiley :-/
        res = status.matches(".*:-/+.*");
        if (res) {
            index = status.indexOf(":-/");
            tweet.addToSetCategories("012", index);
        }

        //smiley :'(
        res = status.matches(".*:'\\(+.*");
        if (res) {
            index = status.indexOf(":'(");
            tweet.addToSetCategories("012", index);
        }

        //smiley :(
        res = status.matches(".*:\\(+.*");
        if (res) {
            index = status.indexOf(":(");
            tweet.addToSetCategories("012", index);
        }

        //smiley (:
        res = status.matches(".*\\(:+.*");
        if (res) {
            index = status.indexOf("(:");
            tweet.addToSetCategories("011", index);
        }

        //smiley ;)
        res = status.matches(".*;\\)+.*");
        if (res) {
            index = status.indexOf(";)");
            tweet.addToSetCategories("011", index);
        }

        //smiley :D
        res = status.matches(".*:D.*");
        if (res) {
            index = status.indexOf(":D");
            tweet.addToSetCategories("011", index);
        }

        //question mark
        res = status.matches(".*\\?+.*");
        if (res) {
            index = status.indexOf("?");
            tweet.addToSetCategories("040", index);
        }

        //question mark with exclamation: ?!
        res = status.matches(".*\\?+\\!+.*");
        if (res) {
            index = status.indexOf("?!");
            tweet.addToSetCategories("012", index);
        }

        //kisses
        res = status.toLowerCase().matches(".*xx+.*");
        if (res) {
            index = status.indexOf("xx");
            tweet.addToSetCategories("011", index);
        }

    }

    public void containsTimeIndication() {
        int index = 0;
        for (String term : ControllerBean.Hloader.mapH4.keySet()) {
            if (status.contains(term)) {
                heuristic = ControllerBean.Hloader.getMapH12().get(term);
                String result = heuristic.checkFeatures(status, term);
                if (result != null) {
                    index = status.indexOf(term);
                    tweet.addToSetCategories(result,index);
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
            tweet.addToSetCategories("0612",-1);
        }
    }

    public void isARetweetOfAClientTweet() {
        if (status.toLowerCase().contains("rt @" + ControllerBean.getClient())) {
            tweet.addToSetCategories("06121",-1);
        }
    }
}
