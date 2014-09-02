/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import Singletons.HeuristicsLoader;
import Twitter.Tweet;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class SentenceLevelHeuristicsPre {

    private String status;
    private String lc;
    private Tweet tweet;
    private Heuristic heuristic;

    @Inject
    HeuristicsLoader HLoader;

    @Inject
    TermLevelHeuristics termLevelHeuristics;

    public SentenceLevelHeuristicsPre() {
    }

    public Tweet applyRules(Tweet tweet, String status) {
        this.status = status;
        this.lc = status.toLowerCase();
        this.tweet = tweet;

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
        boolean res = lc.matches(".*aww+(?s).*");
        if (res) {
            index = lc.indexOf("aww");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("ww")) {
                tweet.setFinalNote(1);
            }
        }
        //yesssss
        res = lc.toLowerCase().matches(".*yess+(?s).*");
        if (res) {
            index = lc.indexOf("yess");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("sss")) {
                tweet.setFinalNote(1);
            }
        }

        //ewwww
        res = lc.matches(".*[^n]eww+(?s).*");
        if (res) {
            index = lc.indexOf("eww");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("ww")) {
                tweet.setFinalNote(-1);
            }
        }

        //arrrgh
        res = lc.matches(".*arr+g(?s).*");
        if (res) {
            index = lc.indexOf("arr");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("gh")) {
                tweet.setFinalNote(-1);
            }
        }

        //ouchhh
        res = lc.matches(".*ou+ch+(?s).*");
        if (res) {
            index = lc.indexOf("ouch");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("h")) {
                tweet.setFinalNote(-1);
            }
        }

        //yaaaay
        res = lc.matches(".*ya+y(?s).*");
        if (res) {
            index = status.indexOf("ya");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("ay")) {
                tweet.setFinalNote(1);
            }
        }
        //yeeeey
        res = lc.matches(".*ye+y(?s).*");
        if (res) {
            index = lc.indexOf("ye");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("ey")) {
                tweet.setFinalNote(1);
            }
        }

        //ahahaha
        res = lc.matches(".*haha(?s).*");
        if (res) {
            index = lc.indexOf("haha");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("aha")) {
                tweet.setFinalNote(1);
            }

        }

        //LMFAO
        res = lc.matches(".*lmfao+(?s).*");
        if (res) {
            index = lc.indexOf("lmfao");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("lmfao")) {
                tweet.setFinalNote(1);
            }
        }

        //LMAO
        res = lc.matches(".*lmao+(?s).*");
        if (res) {
            index = lc.indexOf("lmao");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("lmao")) {
                tweet.setFinalNote(1);
            }
        }

        //yeaaaa
        res = lc.matches(".*yeaa+(?s).*");
        if (res) {
            index = lc.indexOf("yeaa");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("aa")) {
                tweet.setFinalNote(1);
            }
        }

        //yuumm
        res = lc.matches(".*yu+m+(?s).*");
        if (res) {
            index = lc.indexOf("yu");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("m")) {
                tweet.setFinalNote(1);
            }
        }

        //yeeeee
        res = lc.matches(".*yeee+(?s).*");
        if (res) {
            index = lc.indexOf("yeee");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("eee")) {
                tweet.setFinalNote(1);
            }
        }

        //whyyyy
        res = lc.matches(".*whyy+(?s).*");
        if (res) {
            index = lc.indexOf("why");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("yy")) {
                tweet.setFinalNote(-1);
            }
        }

        //helppp
        res = lc.matches(".*helpp+(?s).*");
        if (res) {
            index = lc.indexOf("help");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("pp")) {
                tweet.setFinalNote(-1);
            }
        }

        //noooo
        res = lc.matches(".* nooo+(?s).*");
        if (res) {
            index = lc.indexOf("nooo");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("ooo")) {
                tweet.setFinalNote(-1);
            }
        }

        //wuhuu
        res = lc.matches(".*wu+hu+(?s).*");
        if (res) {
            index = lc.indexOf("wu");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("uu")) {
                tweet.setFinalNote(1);
            }
        }

        //buhuu
        res = lc.matches(".*bu+hu+(?s).*");
        if (res) {
            index = lc.indexOf("bu");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("uu")) {
                tweet.setFinalNote(-1);
            }
        }

        //boooo
        res = lc.matches(".* booo+(?s).*");
        if (res) {
            index = lc.indexOf("booo");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("ooo")) {
                tweet.setFinalNote(-1);
            }
        }

        //uuuugh
        res = lc.matches(".*u+gh+(?s).*");
        if (res) {
            index = lc.indexOf("uu");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("h")) {
                tweet.setFinalNote(-1);
            }
        }

        //woohoo
        res = lc.matches(".*wo+ho+(?s).*");
        if (res) {
            index = lc.indexOf("wo");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("oo")) {
                tweet.setFinalNote(1);
            }
        }

        //yaaaaahooooo
        res = lc.matches(".*ya+ho+(?s).*");
        if (res) {
            index = lc.indexOf("ya");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("oo")) {
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
        res = lc.matches(".*&lt;3(?s).*");
        if (res) {
            index = lc.indexOf("&lt;3");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("&lt;3")) {
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

        //smiley T_T
        res = lc.matches("t_t");
        if (res) {
            index = lc.indexOf("t_t");
            tweet.addToListCategories("012", index);
            if (lc.endsWith("t_t") || lc.endsWith("tt")) {
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
        res = lc.matches(".*xx+(?s).*");
        if (res) {
            index = lc.indexOf("xx");
            tweet.addToListCategories("011", index);
            if (lc.endsWith("xx")) {
                tweet.setFinalNote(1);
            }
        }

        //kisses xoxoxo
        res = lc.matches(".*(xo)\\1{1,}x*o*(?s).*");
        if (res) {
            index = lc.indexOf("xo");
            tweet.addToListCategories("011", index);
        }
        if (lc.endsWith("xo") || status.endsWith("ox")) {
            tweet.setFinalNote(1);
        }
    }

    public void containsTimeIndication() {
        int index = 0;
        for (String term : HLoader.getMapH4().keySet()) {
            if (status.contains(term)) {
                heuristic = HLoader.getMapH12().get(term);
                String result = termLevelHeuristics.checkFeatures(heuristic,status, term);
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
//        if (tweet.getUser().toLowerCase().equals(ControllerBean.getClient())) {
//            tweet.addToListCategories("0612", -1);
//        }
    }

    public void isARetweetOfAClientTweet() {
//        if (status.toLowerCase().contains("rt @" + ControllerBean.getClient())) {
//            tweet.addToListCategories("06121", -1);
//        }
    }
}
