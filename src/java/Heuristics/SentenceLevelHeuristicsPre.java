/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Twitter.Tweet;
import Admin.ControllerBean;
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
        this.status = status.toLowerCase();
        this.tweet = tweet;
    }

    public Tweet applyRules() {
        containsPercentage();
        containsPunctuation();
        containsOnomatopaes();
//        isAClientTweet();
//        isARetweetOfAClientTweet();
        return tweet;
    }

    public void containsPercentage() {
        //do we find a percentage?
        boolean res = status.matches(".*\\d%.*");
        if (res) {
            //if so, is it followed by "off"?
            res = (status.toLowerCase().matches(".*\\d% off.*") | status.toLowerCase().matches(".*\\d% cash back.*"));
            if (res) {
                tweet.addToListCategories("0611", -1);
            } else {
                tweet.addToListCategories("0621", -1);

            }
        }
    }

    public void containsOnomatopaes() {
        //awwww
        int index = 0;
        boolean res = status.matches(".*aww+(?s).*");
        if (res) {
            index = status.indexOf("aww");
            tweet.addToListCategories("011", index);
            if (status.endsWith("ww")) {
                tweet.setFinalNote(1);
            }
        }
        //yesssss
        res = status.toLowerCase().matches(".*yess+(?s).*");
        if (res) {
            index = status.indexOf("yess");
            tweet.addToListCategories("011", index);
            if (status.endsWith("sss")) {
                tweet.setFinalNote(1);
            }
        }

        //ewwww
        res = status.toLowerCase().matches(".*[^n]eww+(?s).*");
        if (res) {
            index = status.indexOf("eww");
            tweet.addToListCategories("012", index);
            if (status.endsWith("w")) {
                tweet.setFinalNote(-1);
            }
        }

        //arrrgh
        res = status.toLowerCase().matches(".*arr+g(?s).*");
        if (res) {
            index = status.indexOf("arr");
            tweet.addToListCategories("012", index);
            if (status.endsWith("gh")) {
                tweet.setFinalNote(-1);
            }
        }

        //ouchhh
        res = status.toLowerCase().matches(".*ou+ch+(?s).*");
        if (res) {
            index = status.indexOf("ouch");
            tweet.addToListCategories("012", index);
            if (status.endsWith("h")) {
                tweet.setFinalNote(-1);
            }
        }

        //yaaaay
        res = status.toLowerCase().matches(".*ya+y(?s).*");
        if (res) {
            index = status.indexOf("ya");
            tweet.addToListCategories("011", index);
            if (status.endsWith("ay")) {
                tweet.setFinalNote(1);
            }
        }
        //yeeeey
        res = status.toLowerCase().matches(".*ye+y(?s).*");
        if (res) {
            index = status.indexOf("ye");
            tweet.addToListCategories("011", index);
            if (status.endsWith("ey")) {
                tweet.setFinalNote(1);
            }
        }

        //ahahaha
        res = status.toLowerCase().matches(".*haha(?s).*");
        if (res) {
            index = status.indexOf("haha");
            tweet.addToListCategories("011", index);
            if (status.endsWith("aha")) {
                tweet.setFinalNote(1);
            }

        }

        //LMFAO
        res = status.toLowerCase().matches(".*lmfao+(?s).*");
        if (res) {
            index = status.indexOf("lmfao");
            tweet.addToListCategories("011", index);
            if (status.endsWith("lmfao")) {
                tweet.setFinalNote(1);
            }
        }

        //LMAO
        res = status.toLowerCase().matches(".*lmao+(?s).*");
        if (res) {
            index = status.indexOf("lmao");
            tweet.addToListCategories("011", index);
            if (status.endsWith("lmao")) {
                tweet.setFinalNote(1);
            }
        }

        //yeaaaa
        res = status.toLowerCase().matches(".*yeaa+(?s).*");
        if (res) {
            index = status.indexOf("yeaa");
            tweet.addToListCategories("011", index);
            if (status.endsWith("aa")) {
                tweet.setFinalNote(1);
            }
        }

        //yuumm
        res = status.toLowerCase().matches(".*yu+m+(?s).*");
        if (res) {
            index = status.indexOf("yu");
            tweet.addToListCategories("011", index);
            if (status.endsWith("m")) {
                tweet.setFinalNote(1);
            }
        }

        //yeeeee
        res = status.toLowerCase().matches(".*yeee+(?s).*");
        if (res) {
            index = status.indexOf("yeee");
            tweet.addToListCategories("011", index);
            if (status.endsWith("eee")) {
                tweet.setFinalNote(1);
            }
        }

        //whyyyy
        res = status.toLowerCase().matches(".*whyy+(?s).*");
        if (res) {
            index = status.indexOf("why");
            tweet.addToListCategories("012", index);
            if (status.endsWith("yy")) {
                tweet.setFinalNote(-1);
            }
        }

        //helppp
        res = status.toLowerCase().matches(".*helpp+(?s).*");
        if (res) {
            index = status.indexOf("help");
            tweet.addToListCategories("012", index);
            if (status.endsWith("pp")) {
                tweet.setFinalNote(-1);
            }
        }

        //noooo
        res = status.toLowerCase().matches(".* nooo+(?s).*");
        if (res) {
            index = status.indexOf("nooo");
            tweet.addToListCategories("012", index);
            if (status.endsWith("ooo")) {
                tweet.setFinalNote(-1);
            }
        }

        //wuhuu
        res = status.toLowerCase().matches(".*wu+hu+(?s).*");
        if (res) {
            index = status.indexOf("wu");
            tweet.addToListCategories("011", index);
            if (status.endsWith("uu")) {
                tweet.setFinalNote(1);
            }
        }

        //buhuu
        res = status.toLowerCase().matches(".*bu+hu+(?s).*");
        if (res) {
            index = status.indexOf("bu");
            tweet.addToListCategories("012", index);
            if (status.endsWith("uu")) {
                tweet.setFinalNote(-1);
            }
        }

        //boooo
        res = status.toLowerCase().matches(".* booo+(?s).*");
        if (res) {
            index = status.indexOf("booo");
            tweet.addToListCategories("012", index);
            if (status.endsWith("ooo")) {
                tweet.setFinalNote(-1);
            }
        }

        //uuuugh
        res = status.toLowerCase().matches(".*u+gh+(?s).*");
        if (res) {
            index = status.indexOf("uu");
            tweet.addToListCategories("012", index);
            if (status.endsWith("h")) {
                tweet.setFinalNote(-1);
            }
        }

        //woohoo
        res = status.toLowerCase().matches(".*wo+ho+(?s).*");
        if (res) {
            index = status.indexOf("wo");
            tweet.addToListCategories("011", index);
            if (status.endsWith("oo")) {
                tweet.setFinalNote(1);
            }
        }

        //yaaaaahooooo
        res = status.toLowerCase().matches(".*ya+ho+(?s).*");
        if (res) {
            index = status.indexOf("ya");
            tweet.addToListCategories("011", index);
            if (status.endsWith("oo")) {
                tweet.setFinalNote(1);
            }
        }
    }

    public void containsPunctuation() {
        int index = 0;

        //multiple exclamation marks
        boolean res = status.matches(".*!!+(?s).*");
        if (res) {
            index = status.indexOf("!!");
            tweet.addToListCategories("022", index);
        }

        //heart ☺
        res = status.matches(".*☺+(?s).*");
        if (res) {
            index = status.indexOf("☺");
            tweet.addToListCategories("011", index);
            if (status.endsWith("☺")) {
                tweet.setFinalNote(1);
            }
        }

        //heart &lt;3
        res = status.matches(".*&lt;3(?s).*");
        if (res) {
            index = status.indexOf("&lt;3");
            tweet.addToListCategories("011", index);
            if (status.endsWith("&lt;3")) {
                tweet.setFinalNote(1);
            }
        }

        //heart ♥
        res = status.matches(".*♥+(?s).*");
        if (res) {
            index = status.indexOf("♥");
            tweet.addToListCategories("011", index);
            if (status.endsWith("♥")) {
                tweet.setFinalNote(1);
            }
        }

        //heart <3
        res = status.matches(".*<3+(?s).*");
        if (res) {
            index = status.indexOf("<3");
            tweet.addToListCategories("011", index);
            if (status.endsWith("3")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :)
        res = status.matches(".*:\\)+(?s).*");
        if (res) {
            index = status.indexOf(":)");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":)") || status.endsWith(":))") || status.endsWith(":)))")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :-)
        res = status.matches(".*:-\\)+(?s).*");
        if (res) {
            index = status.indexOf(":-)");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":-)") || status.endsWith(":-))") || status.endsWith(":-)))")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley : )
        res = status.matches(".*: \\)+(?s).*");
        if (res) {
            index = status.indexOf(": )");
            tweet.addToListCategories("011", index);
            if (status.endsWith(": )") || status.endsWith(": ))") || status.endsWith(": )))")) {
                tweet.setFinalNote(1);
            }

        }

        //smiley :]
        res = status.matches(".*:\\]+(?s).*");
        if (res) {
            index = status.indexOf(":]");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":]") || status.endsWith(":]]")) {
                tweet.setFinalNote(1);
            }

        }

        //smiley ^__^
        res = status.matches(".*\\^_*\\^(?s).*");
        if (res) {
            index = status.indexOf("^");
            tweet.addToListCategories("011", index);
            if (status.endsWith("^")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :O
        res = status.matches(".*:o (?s).*");
        if (res) {
            index = status.indexOf(":o");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":o")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley xD
        res = status.matches(".*:d(?s).*");
        if (res) {
            index = status.indexOf("xd");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":d")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley ;p
        res = status.matches(".*;p+(?s).*");
        if (res) {
            index = status.indexOf(";p");
            tweet.addToListCategories("011", index);
            if (status.endsWith(";p")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :-p
        res = status.matches(".*:-p+(?s).*");
        if (res) {
            index = status.indexOf(":-p");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":-p")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :p
        res = status.matches(".*:-p+(?s).*");
        if (res) {
            index = status.indexOf(":-p");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":-p")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley (:
        res = status.matches(".*\\(:+(?s).*");
        if (res) {
            index = status.indexOf("(:");
            tweet.addToListCategories("011", index);
            if (status.endsWith("(:")) {
                tweet.setFinalNote(1);
            }
        }


        //smiley ;)
        res = status.matches(".*;\\)+(?s).*");
        if (res) {
            index = status.indexOf(";)");
            tweet.addToListCategories("011", index);
            if (status.endsWith(";)") || status.endsWith(";))") || status.endsWith(";)))")) {
                tweet.setFinalNote(1);
            }
        }

        //smiley :D
        res = status.matches(".*:d(?s).*");
        if (res) {
            index = status.indexOf(":d");
            tweet.addToListCategories("011", index);
            if (status.endsWith(":d")) {
                tweet.setFinalNote(1);
            }

        }


        //smiley :|
        res = status.matches(".*:\\|+(?s).*");
        if (res) {
            index = status.indexOf(":|");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":|")) {
                tweet.setFinalNote(-1);
            }
        }

        //smiley :S
        res = status.matches(".*:S(?s).*");
        if (res) {
            index = status.indexOf(":S");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":S")) {
                tweet.setFinalNote(-1);
            }
        }


        //smiley =(
        res = status.matches(".*=\\(+(?s).*");
        if (res) {
            index = status.indexOf("=(");
            tweet.addToListCategories("012", index);
            if (status.endsWith("=(") || status.endsWith("=((") || status.endsWith("=(((")) {
                tweet.setFinalNote(-1);
            }

        }


        //smiley :-(
        res = status.matches(".*:-\\(+(?s).*");
        if (res) {
            index = status.indexOf(":-(");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":-(") || status.endsWith(":-((") || status.endsWith(":-(((")) {
                tweet.setFinalNote(-1);
            }
        }

        //smiley :-/
        res = status.matches(".*:-/+(?s).*");
        if (res) {
            index = status.indexOf(":-/");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":-/") || status.endsWith(":-//") || status.endsWith(":-///")) {
                tweet.setFinalNote(-1);
            }
        }

        //smiley :'(
        res = status.matches(".*:'\\(+(?s).*");
        if (res) {
            index = status.indexOf(":'(");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":'(") || status.endsWith(":'((") || status.endsWith(":'(((")) {
                tweet.setFinalNote(-1);
            }

        }

        //smiley :(
        res = status.matches(".*:\\(+(?s).*");
        if (res) {
            index = status.indexOf(":(");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":(") || status.endsWith(":((") || status.endsWith(":(((")) {
                tweet.setFinalNote(-1);
            }
        }

        //smiley :/
        res = status.matches(".*:/+(?s).*");
        if (res) {
            index = status.indexOf(":/");
            tweet.addToListCategories("012", index);
            if (status.endsWith(":/") || status.endsWith("://") || status.endsWith(":///")) {
                tweet.setFinalNote(-1);
            }
        }


        //question mark
        res = status.matches(".*\\?+(?s).*");
        if (res) {
            index = status.indexOf("?");
            tweet.addToListCategories("040", index);
        }



        //kisses xxx
        res = status.matches(".*xx+(?s).*");
        if (res) {
            index = status.indexOf("xx");
            tweet.addToListCategories("011", index);
            if (status.endsWith("xx")) {
                tweet.setFinalNote(1);
            }
        }

        //kisses xoxoxo
        res = status.matches(".*(xo)\\1{1,}x*o*(?s).*");
        if (res) {
            index = status.indexOf("xo");
            tweet.addToListCategories("011", index);
        }
        if (status.endsWith("xo") || status.endsWith("ox")) {
            tweet.setFinalNote(1);
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
                    tweet.addToListCategories(result, index);
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
            tweet.addToListCategories("0612", -1);
        }
    }

    public void isARetweetOfAClientTweet() {
        if (status.toLowerCase().contains("rt @" + ControllerBean.getClient())) {
            tweet.addToListCategories("06121", -1);
        }
    }
}
