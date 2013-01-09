/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import RuleInterpreter.Interpreter;
import Twitter.TweetLoader;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
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
    private Multimap<String, Set<String>> mapFeatures;
    private String rule;
    private String punctuation = "!?.'\",()-|=#";
    private String punctuationRegex = "[\\!\\?\\.'\\\\\",\\(\\)\\-\\|=]";

    public Heuristic(String term, Multimap mapFeatures, String rule) {
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

        if (mapFeatures == null || mapFeatures.isEmpty()) {
//            System.out.println("no feature, returning a simple digit");
            return rule;
        }
        int count = 0;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (Entry<String, Set<String>> feature : mapFeatures.entries()) {
//            if (termOrig.equals("Learn")) {
//                System.out.println("feature: " + feature.getKey());
//                System.out.println("status: " + status);
//            }
//            System.out.println("count: " + count);
            if (feature.getKey().contains("isFollowedByAnOpinion")) {
                outcome = isFollowedByAnOpinion(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstTermOfStatus")) {
                outcome = isFirstTermOfStatus(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFollowedByTimeIndication")) {
                outcome = isFollowedByTimeIndication(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFollowedByVerbPastTense")) {
                outcome = isFollowedByVerbPastTense(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFollowedBySpecificTerm")) {
                outcome = isFollowedBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isHashtagStart")) {
                outcome = isHashtagStart(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededBySpecificTerm")) {
                outcome = isPrecededBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isNotPrecededBySpecificTerm")) {
                outcome = isNotPrecededBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isQuestionMarkAtEndOfStatus")) {
                outcome = isQuestionMarkAtEndOfStatus(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isNotAllCaps")) {
                outcome = isNotAllCaps(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededByANegation")) {
                outcome = !isPrecededByANegation(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededByStrongWord")) {
                outcome = isPrecededByStrongWord(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededByPositive")) {
                outcome = isPrecededByPositive(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstLetterCapitalized")) {
                outcome = isFirstLetterCapitalized(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isAllCaps")) {
                outcome = isAllCaps(termOrig);
//                if (termOrig.equals("DO NOT")) {
//                    System.out.println("outcome: " + outcome);
//                }
//
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            }
            count++;

        }


        String result = Interpreter.interprete(rule, conditions);
//        if (termOrig.equals("Learn")) {
//            System.out.println("result: " + result);
//        }

        return result;
//        System.out.println("result returned in the heuristic checker: " + result);
//        System.out.println("features:");
//        for (String feature : features) {
//            System.out.println("feature: \"" + feature + "\"");
//        }

    }

    public boolean isFollowedByAnOpinion(String status, String termOrig) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
        int indexTerm = status.indexOf(termOrig);
//        System.out.println("index of term: " + indexTerm);
        String temp = status.substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = StringUtils.strip(tempArray[1],punctuation);
        }
        return (TweetLoader.Hloader.getMapH2().keySet().contains(temp)
                || TweetLoader.Hloader.getMapH1().keySet().contains(temp))
                ? true : false;
    }

    public boolean isFollowedByTimeIndication(String status, String termOrig) {
//        if (status.contains(" imploded this morning") & termOrig.equals("this")) {
//            System.out.println("status: " + status);
//            System.out.println("term: " + term);
//        }
        int indexTerm = status.indexOf(termOrig);
//        System.out.println("index of term: " + indexTerm);
        String temp = status.substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = StringUtils.strip(tempArray[1], punctuation).trim();
        }
//        System.out.println("next term: " + temp);
        boolean result = (TweetLoader.Hloader.getSetTimeTokens().contains(temp))
                ? true : false;

//        System.out.println("result: " + result);
        return result;
    }

    public boolean isFollowedByVerbPastTense(String status, String termOrig) {
        String temp = status.substring(status.indexOf(termOrig)).trim();
        temp = StringUtils.strip(temp.split(" ")[1], punctuation);
        return (StringUtils.endsWith(temp, "ed")) ? true : false;
    }

    public boolean isFollowedBySpecificTerm(String status, String termOrig, Set<String> parameters) {
        String temp = status.substring(status.indexOf(termOrig)).trim();
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHashtagStart(String termOrig) {
//        System.out.println("termOrig (hashtag contained in the curr tweet): " + termOrig);
//        System.out.println("term (term contained in the sheet 13 of the Excel sheet of heuristics): " + term);
        return (termOrig.startsWith(term)) ? true : false;
    }

    public boolean isFirstLetterCapitalized(String termOrig) {
        return (StringUtils.isAllUpperCase(StringUtils.left(termOrig, 1))) ? true : false;
    }

    public boolean isPrecededBySpecificTerm(String status, String termOrig, Set<String> parameters) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
        String temp = status.substring(0, status.indexOf(termOrig)).trim().toLowerCase();
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNotPrecededBySpecificTerm(String status, String termOrig, Set<String> parameters) {
//        System.out.println("status: " + status);
//        System.out.println("term: " + term);
        String temp = status.substring(0, status.indexOf(termOrig)).trim().toLowerCase();
//        System.out.println("before the term: " + temp);
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
//                System.out.println("term found!");
                return false;
            }
        }
//        System.out.println("no term found!");
        return true;
    }

    public boolean isPrecededByStrongWord(String status, String termOrig) {
        String[] temp = status.substring(0, status.indexOf(termOrig)).trim().split(" ");
        if (TweetLoader.Hloader.getMapH3().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean isPrecededByPositive(String status, String termOrig) {
        String[] temp = status.substring(0, status.indexOf(termOrig)).trim().split(" ");
        if (TweetLoader.Hloader.getMapH1().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
            return true;
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
        return (StringUtils.isAllUpperCase(StringUtils.remove(termOrig, " "))) ? true : false;
    }

    public boolean isNotAllCaps(String termOrig) {
        return (StringUtils.isAllUpperCase(StringUtils.remove(termOrig, " "))) ? false : true;
    }

    public boolean isPrecededByANegation(String status, String termOrig) {
        term = term.toLowerCase();
        int indexTerm = StringUtils.indexOf(status, termOrig);
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

    public boolean isFirstTermOfStatus(String status, String termOrig) {
        status = StringUtils.strip(status.trim(), punctuation);
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
        boolean res = status.startsWith(termOrig) ? true : false;
//        System.out.println("result: " + res);
        return res;
    }
}
