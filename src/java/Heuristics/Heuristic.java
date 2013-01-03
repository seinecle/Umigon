/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import RuleInterpreter.Interpreter;
import Twitter.TweetLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.CharUtils;
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
    private TreeMap<String, Set<String>> mapFeatures;
    private String rule;
    private String punctuation = "!?.'\",()-|=";
    private String punctuationRegex = "[\\!\\?\\.'\\\\\",\\(\\)\\-\\|=]";

    public Heuristic(String term, TreeMap<String, Set<String>> mapFeatures, String rule) {
        this.term = term;
        this.mapFeatures = mapFeatures;
        this.rule = rule;
    }

    public String getTerm() {
        return term;
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

    public String checkFeatures(String status, String termOrig) {

        HashMap<String, Boolean> conditions = new HashMap();
        boolean outcome;

//        if (termOrig.equals("GOLD")) {
//            System.out.println("term: " + term);
//        }
        if (mapFeatures == null || mapFeatures.isEmpty()) {
//            System.out.println("no feature, returning a simple digit");
            return rule;
        }
        int count = 0;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (Entry<String, Set<String>> feature : mapFeatures.entrySet()) {
//            System.out.println("count: " + count);
            if (feature.getKey().contains("isFollowedByAnOpinion")) {
                outcome = isFollowedByAnOpinion(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstTermOfStatus")) {
                outcome = isFirstTermOfStatus(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededBySpecificTerm")) {
                outcome = isPrecededBySpecificTerm(status, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isContainedInTweet")) {
                outcome = isContainedInTweet(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isQuestionMarkAtEndOfStatus")) {
                outcome = isQuestionMarkAtEndOfStatus(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isNotAllCaps")) {
                outcome = isNotAllCaps(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededByANegation")) {
                outcome = !isPrecededByANegation(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstLetterCapitalized")) {
                outcome = isFirstLetterCapitalized();
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isAllCaps")) {
                outcome = isAllCaps(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            }
            count++;

        }
        String result = Interpreter.interprete(rule, conditions);

        if (result == null) {
//            System.out.println("null result detected in the heuristic, termOrig is: " + termOrig);
            return result;
        } else if (result.contains("0330")) {
//            System.out.println("0330 detected in result in the heuristic, termOrig is: " + termOrig);
            return result;
        }
        return result;
//        System.out.println("result returned in the heuristic checker: " + result);
//        System.out.println("features:");
//        for (String feature : features) {
//            System.out.println("feature: \"" + feature + "\"");
//        }

    }

    public boolean isFollowedByAnOpinion(String status) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
        int indexTerm = status.toLowerCase().indexOf(term);
//        System.out.println("index of term: " + indexTerm);
        String temp = status.toLowerCase().substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = tempArray[1];
        }
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

    public boolean isPrecededBySpecificTerm(String status, Set<String> parameters) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
        String temp = status.substring(0, status.toLowerCase().indexOf(term)).trim();
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isQuestionMarkAtEndOfStatus(String status) {
//        if (term.equals("Is") & status.contains("service model")) {
//            System.out.println("here!");
//        } F
        List<String> terms = new ArrayList();
        Collections.addAll(terms, status.split(" "));
        StringBuilder sb = new StringBuilder();
        boolean cleanEnd = false;
        ListIterator<String> termsIterator = terms.listIterator(terms.size());
        while (termsIterator.hasPrevious() & !cleanEnd) {
            String string = termsIterator.previous();
            if (!cleanEnd && (string.contains("/") || string.startsWith("#") || string.startsWith("@") || string.equals("\\|") || string.equals("") || string.contains("via") || string.equals("..."))) {
                continue;
            } else {
                cleanEnd = true;
            }
            sb.insert(0, string);
        }
        status = sb.toString().trim();
        if (status.length() == 0) {
            return false;
        } else {
            return ("?".equals(String.valueOf(status.charAt(status.length() - 1)))) ? true : false;
        }
    }

    public boolean isAllCaps(String termOrig) {
        return (StringUtils.isAllUpperCase(termOrig)) ? true : false;
    }

    public boolean isNotAllCaps(String termOrig) {
        return (StringUtils.isAllUpperCase(termOrig)) ? false : true;
    }

    public boolean isPrecededByANegation(String status) {
        term = term.toLowerCase();
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
        String termStripped = StringUtils.strip(term, punctuation).toLowerCase();
        term = term.toLowerCase();
        status = StringUtils.strip(status.toLowerCase().trim(), punctuation);
        String[] terms = status.split(" ");
        StringBuilder sb = new StringBuilder();
        boolean cleanStart = false;
        for (String currTerm : terms) {
            if (!cleanStart & (currTerm.startsWith("RT") || currTerm.startsWith("@"))) {
                continue;
            } else {
                cleanStart = true;
            }
            sb.append(currTerm).append(" ");
            if (cleanStart) {
                break;
            }
        }
        status = sb.toString().trim();
        return (status.startsWith(term) || status.startsWith(termStripped)) ? true : false;
    }

    public boolean isContainedInTweet(String status) {
        String termStripped = StringUtils.strip(term);
        term = term.toLowerCase();
        return (status.toLowerCase().trim().contains(term) || status.toLowerCase().trim().contains(termStripped)) ? true : false;
    }
}
