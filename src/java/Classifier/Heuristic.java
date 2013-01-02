/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Twitter.TweetLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
    private Set<String> features;
    private String parametersFeature1;
    private int map;
    private int codeCategories[];
    private String punctuation = "!?.'\",()-|=";
    1
    private String punctuationRegex = "[\\!\\?\\.'\\\\\",\\(\\)\\-\\|=]";

    public Heuristic(String term) {
        this.term = term;
        this.features = new HashSet();
    }

    public Heuristic(String term, int map) {
        this.term = term;
        this.map = map;
        this.features = new HashSet();
    }

    public String getTerm() {
        return term;
    }

    public void addFeature(String feature) {
        features.add(feature);
//        if (this.getTerm().equals("hateful")) {
//            System.out.println("feature added to hateful: \"" + feature + "\"");
//        }
    }

    public Set<String> getFeature1() {
        return features;
    }

    public int getMap() {
        return map;
    }

    public String getParametersFeature1() {
        return parametersFeature1;
    }

    public void setParametersFeature1(String parametersFeature1) {
        this.parametersFeature1 = parametersFeature1;
    }

    public int[] getCodeCategories() {
        return codeCategories;
    }

    public void setCodeCategories(int[] codeCategories) {
        this.codeCategories = codeCategories;
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

    public int checkFeatures(String status, String termOrig) {
        int codeCategory;
//        if (termOrig.equals("GOLD")) {
//            System.out.println("term: " + term);
//        }
        if (features == null || features.isEmpty()) {
//            System.out.println("no feature, returning true");
            return this.codeCategories[0];
        }
//        System.out.println("features:");
//        for (String feature : features) {
//            System.out.println("feature: \"" + feature + "\"");
//        }


        if (features.contains("isNextWordAnOpinion")) {
            codeCategory = isFollowedByAnOpinion(status);
        } else if (features.contains("isFirstTermOfStatus")) {
            codeCategory = isFirstTermOfStatus(status);
        } else if (features.contains("isPrecededBySpecificTerm")) {
            codeCategory = isPrecededBySpecificTerm(status);
        } else if (features.contains("isContainedInTweet")) {
            codeCategory = isContainedInTweet(status);
        } else if (features.contains("isQuestionMarkAtEndOfStatus")) {
            codeCategory = isQuestionMarkAtEndOfStatus(status);
        } else if (features.contains("isNotAllCaps")) {
            codeCategory = isNotAllCaps(termOrig);
        } else if (features.contains("isPrecededByANegation")) {
            codeCategory = !isPrecededByANegation(status);
        } else if (features.contains("isFirstLetterCapitalized")) {
            codeCategory = isFirstLetterCapitalized();
        } else if (features.contains("isAllCaps")) {
            codeCategory = isAllCaps(termOrig);
        } else {
//            System.out.println("returning false here!");
            codeCategory = Integer.parseInt(this.codeCategories[0]);
        }
//        System.out.println("end of check feature, res is: " + res);
        return res;
    }

    public int isFollowedByAnOpinion(String status) {
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

    public boolean isPrecededBySpecificTerm(String status) {
        String temp = status.substring(0, status.indexOf(term)).trim();
        String[] terms = this.parametersFeature1.split("|");
        for (String candidate : terms) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isQuestionMarkAtEndOfStatus(String status) {
//        if (term.equals("Is") & status.contains("service model")) {
//            System.out.println("here!");
//        }
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
