/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.TweetLoader;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class Heuristic {

    public enum feature1 {

        isNextWordAnOpinion
    }
    private String term;
    private String feature1;
    private int map;
    private String punctuation = "!?.'\",()-";
    private String punctuationRegex = "[\\!\\?\\.'\\\\\",\\(\\)\\-]";

    public Heuristic(String term) {
        this.term = term;
    }

    public Heuristic(String term, String feature1, int map) {
        this.term = term;
        this.feature1 = feature1;
        this.map = map;
    }

    public String getTerm() {
        return term;
    }

    public String getFeature1() {
        return feature1;
    }

    public int getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "Heuristic{" + "term=" + term + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.term != null ? this.term.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Heuristic other = (Heuristic) obj;
        if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
            return false;
        }
        return true;
    }

    public boolean checkFeatures(String status, String term) {
        this.term = term;
//        System.out.println("term: " + term);
//        System.out.println("feature: " + this.feature1);
        boolean res;
        if (feature1 == null || feature1.isEmpty()) {
//            System.out.println("no feature, returning true");
            return true;
        }
        if (feature1.equals("isNextWordAnOpinion")) {
            res = isFollowedByAnOpinion(status);
        } else if (feature1.equals("isFirstTermOfStatus")) {
            res = isFirstTermOfStatus(status);
        } else if (feature1.equals("isContainedInTweet")) {
            res = isContainedInTweet(status);
        } else if (feature1.equals("isPrecededByANegation")) {
            res = !isPrecededByANegation(status);
        } else if (feature1.equals("isFirstLetterCapitalized")) {
            res = isFirstLetterCapitalized();
        } else if (feature1.equals("isAllCaps")) {
            res = isAllCaps();
        } else {
//            System.out.println("returning false here!");
            res = false;
        }
//        System.out.println("end of check feature, res is: " + res);
        return res;
    }

    public boolean isFollowedByAnOpinion(String status) {
        String temp = status.substring(status.indexOf(term)).trim();
        temp = temp.split(" ")[1];
        return (TweetLoader.Hloader.getMapH2().keySet().contains(temp)
                || TweetLoader.Hloader.getMapH1().keySet().contains(temp)
                || TweetLoader.Hloader.getMapH2().keySet().contains(StringUtils.strip(temp, punctuation))
                || TweetLoader.Hloader.getMapH1().keySet().contains(StringUtils.strip(temp, punctuation)))
                ? true : false;
    }

    public boolean isFollowedByVerbPastTense(String status) {
        String temp = status.substring(status.indexOf(term)).trim();
        temp = StringUtils.strip(temp.split(" ")[1], punctuation);
        return (StringUtils.endsWith(temp, "ed")) ? true : false;
    }

    public boolean isFirstLetterCapitalized() {
        return (StringUtils.isAllUpperCase(StringUtils.left(term, 1))) ? true : false;
    }

    public boolean isAllCaps() {
        return (StringUtils.isAllUpperCase(term)) ? true : false;
    }

    public boolean isPrecededByANegation(String status) {
        int indexTerm = StringUtils.indexOf(status, term);
        String[] temp = StringUtils.left(status.replaceAll(punctuationRegex, " ").toLowerCase(), indexTerm).split(" ");
//        if (term.equals("successful.") & status.contains("for any")) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
//        System.out.println("temp[temp.length - 1]: " + temp[temp.length - 1]);
//        if (temp.length > 1) {
//            System.out.println("temp[temp.length - 2]: " + temp[temp.length - 2]);
//        }
//        System.out.println("temp: " + temp.toString());
//        }

        //if the array is empty it means that the term is the first of the status;
        if (temp.length == 0) {
//            System.out.println("returning false");
            return false;
            //in this case the term is the second in the status. If the previous one is a negative word, return true (as in "like" being preceded by "don't") 
        } else if (temp.length == 1) {
//            System.out.println("temp length ==1");
            boolean res = TweetLoader.Hloader.setNegations.contains(temp[0]) ? true : false;
//            System.out.println("res: " + res);
            return res;
            //in this case the term is preceded by many other terms. We just check the three previous ones.    
        } else if (temp.length == 2) {
//            System.out.println("temp length ==2");
            if (TweetLoader.Hloader.setNegations.contains(temp[temp.length - 1])) {
                return true;
            }

            //in the case of "don't really like", return true
            if (TweetLoader.Hloader.getMapH3().containsKey(temp[temp.length - 1]) & TweetLoader.Hloader.setNegations.contains(temp[temp.length - 2])) {
//                System.out.println("returning true in the don't really like case");
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[0] + " " + temp[1];
            boolean res = (TweetLoader.Hloader.setNegations.contains(concat)) ? true : false;
//            System.out.println("returning in the concat version: " + res);
            return res;
        } else if (temp.length > 2) {
//            System.out.println("temp length >2");
            //in the case of "don't really like", return true

            if (TweetLoader.Hloader.setNegations.contains(temp[temp.length - 1])) {
//                System.out.println("returning true here...");
                return true;
            }
//            System.out.println("res: " + res);


            if (TweetLoader.Hloader.getMapH3().containsKey(temp[temp.length - 1]) & TweetLoader.Hloader.setNegations.contains(temp[temp.length - 2])) {
//                System.out.println("returning true here!");
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[temp.length - 2] + " " + temp[temp.length - 1];
//            System.out.println("concat result!: " + concat.trim());
            boolean res = (TweetLoader.Hloader.setNegations.contains(concat)) ? true : false;
//            System.out.println("res at the concat level: " + res);
            return res;
        }
//        System.out.println("returning false here!");
        return false;
    }

    public boolean isFirstTermOfStatus(String status) {
        String termStripped = StringUtils.strip(term, punctuation);
        status = StringUtils.strip(status.toLowerCase().trim(), punctuation);
//        System.out.println("term: " + term);
        return (status.startsWith(term) || status.startsWith(termStripped)) ? true : false;
    }

    public boolean isContainedInTweet(String status) {
        String termStripped = StringUtils.strip(term);
        return (status.toLowerCase().trim().contains(term) || status.toLowerCase().trim().contains(termStripped)) ? true : false;
    }
}
