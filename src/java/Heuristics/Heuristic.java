package Heuristics;

import RuleInterpreter.Interpreter;
import Admin.ControllerBean;
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

    private String term;
    private Multimap<String, Set<String>> mapFeatures;
    private String rule;

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
        String termOrigCasePreserved = termOrig;
        termOrig = termOrig.toLowerCase();
        String statusOrigCasePreserved = status;
        status = status.toLowerCase();
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
            boolean opposite = false;
            if (feature.getKey().startsWith("!")) {
                opposite = true;
            }
            if (feature.getKey().contains("isImmediatelyFollowedByAnOpinion")) {
                outcome = opposite ? !isImmediatelyFollowedByAnOpinion(status, termOrig) : isImmediatelyFollowedByAnOpinion(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstTermOfStatus")) {
                outcome = opposite ? !isFirstTermOfStatus(status, termOrig) : isFirstTermOfStatus(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFollowedByAPositiveOpinion")) {
                outcome = opposite ? !isFollowedByAPositiveOpinion(status, termOrig) : isFollowedByAPositiveOpinion(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyFollowedByAPositiveOpinion")) {
                outcome = opposite ? !isImmediatelyFollowedByAPositiveOpinion(status, termOrig) : isImmediatelyFollowedByAPositiveOpinion(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyFollowedByTimeIndication")) {
                outcome = opposite ? !isImmediatelyFollowedByTimeIndication(status, termOrig) : isImmediatelyFollowedByTimeIndication(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyFollowedByVerbPastTense")) {
                outcome = opposite ? !isImmediatelyFollowedByVerbPastTense(status, termOrig) : !isImmediatelyFollowedByVerbPastTense(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFollowedBySpecificTerm")) {
                outcome = opposite ? !isFollowedBySpecificTerm(status, termOrig, feature.getValue()) : isFollowedBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyFollowedBySpecificTerm")) {
                outcome = opposite ? !isFollowedBySpecificTerm(status, termOrig, feature.getValue()) : isFollowedBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isHashtagStart")) {
                outcome = opposite ? !isHashtagStart(termOrig) : isHashtagStart(termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededBySpecificTerm")) {
                outcome = opposite ? !isPrecededBySpecificTerm(status, termOrig, feature.getValue()) : isPrecededBySpecificTerm(status, termOrig, feature.getValue());
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isQuestionMarkAtEndOfStatus")) {
                outcome = opposite ? !isQuestionMarkAtEndOfStatus(status) : isQuestionMarkAtEndOfStatus(status);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isAllCaps")) {
                outcome = opposite ? !isAllCaps(termOrigCasePreserved) : isAllCaps(termOrigCasePreserved);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyPrecededByANegation")) {
                outcome = opposite ? !isImmediatelyPrecededByANegation(status, termOrig) : isImmediatelyPrecededByANegation(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isPrecededByStrongWord")) {
                outcome = opposite ? isPrecededByStrongWord(status, termOrig) : isPrecededByStrongWord(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isImmediatelyPrecededByPositive")) {
                outcome = opposite ? !isImmediatelyPrecededByPositive(status, termOrig) : isImmediatelyPrecededByPositive(status, termOrig);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            } else if (feature.getKey().contains("isFirstLetterCapitalized")) {
                outcome = opposite ? !isFirstLetterCapitalized(termOrigCasePreserved) : isFirstLetterCapitalized(termOrigCasePreserved);
                conditions.put(StringUtils.substring(alphabet, count, (count + 1)), outcome);
            }
            count++;
        }
        Interpreter interpret = new Interpreter();
        String result = interpret.interprete(rule, conditions);
        return result;
    }

    public boolean isImmediatelyFollowedByAnOpinion(String status, String termOrig) {
        int indexTerm = status.indexOf(termOrig);
        if (indexTerm == -1) {
            System.out.println("status: " + status);
            System.out.println("termOrig: " + termOrig);
        }
        String temp = status.substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = tempArray[1].trim();
        }
        return (ControllerBean.Hloader.getMapH2().keySet().contains(temp)
                || ControllerBean.Hloader.getMapH1().keySet().contains(temp))
                ? true : false;
    }

    public boolean isFollowedByAPositiveOpinion(String status, String termOrig) {
        int indexTerm = status.indexOf(termOrig);
        String temp = status.substring(indexTerm).trim();

        for (String positiveTerm : ControllerBean.Hloader.getMapH1().keySet()) {
            if (temp.contains(positiveTerm)) {
                return true;
            }
        }
        return false;
    }

    public boolean isImmediatelyFollowedByAPositiveOpinion(String status, String termOrig) {
        int indexTerm = status.indexOf(termOrig);
        String temp = status.substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = tempArray[1].trim();
        }
        return (ControllerBean.Hloader.getMapH1().keySet().contains(temp))
                ? true : false;
    }

    public boolean isImmediatelyFollowedByTimeIndication(String status, String termOrig) {
        int indexTerm = status.indexOf(termOrig);
        String temp = status.substring(indexTerm).trim();
        String[] tempArray = temp.split(" ");
        if (tempArray.length < 2) {
            return false;
        } else {
            temp = tempArray[1].trim();
        }
        boolean result = (ControllerBean.Hloader.getSetTimeTokens().contains(temp))
                ? true : false;
        return result;
    }

    public boolean isImmediatelyFollowedByVerbPastTense(String status, String termOrig) {
        String temp = status.substring(status.indexOf(termOrig)).trim();
        boolean pastTense;
        String[] nextTerms = temp.split(" ");
        if (nextTerms.length > 1) {
            temp = nextTerms[1].trim();
            pastTense = StringUtils.endsWith(temp, "ed");
            if (pastTense) {
                return true;
            }
            pastTense = StringUtils.endsWith(temp, "ought") & !StringUtils.startsWith(temp, "ought");
            if (pastTense) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isImmediatelyFollowedBySpecificTerm(String status, String termOrig, Set<String> parameters) {
        String temp = status.substring(status.indexOf(termOrig)).trim();
        String[] nextTerms = temp.split(" ");
        if (nextTerms.length > 1) {
            temp = nextTerms[1].trim();
            for (String candidate : parameters) {
                if (temp.equals(candidate)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean isFollowedBySpecificTerm(String status, String termOrig, Set<String> parameters) {
        int indexTerm = status.indexOf(termOrig);
        if (indexTerm == -1) {
            System.out.println("status: " + status);
            System.out.println("termOrig: " + termOrig);
        }

        String temp = status.substring(indexTerm).trim();
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHashtagStart(String termOrig) {
        return (termOrig.startsWith(term)) ? true : false;
    }

    public boolean isFirstLetterCapitalized(String termOrig) {
        return (StringUtils.isAllUpperCase(StringUtils.left(termOrig, 1))) ? true : false;
    }

    public boolean isPrecededBySpecificTerm(String status, String termOrig, Set<String> parameters) {
        String temp = status.substring(0, status.indexOf(termOrig)).trim().toLowerCase();
        for (String candidate : parameters) {
            if (temp.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPrecededByStrongWord(String status, String termOrig) {
        String[] temp = status.substring(0, status.indexOf(termOrig)).trim().split(" ");
        if (ControllerBean.Hloader.getMapH3().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean isImmediatelyPrecededByPositive(String status, String termOrig) {
        String[] temp = status.substring(0, status.indexOf(termOrig)).trim().split(" ");
        if (ControllerBean.Hloader.getMapH1().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean isQuestionMarkAtEndOfStatus(String status) {
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
        String temp = termOrig.replaceAll(" ", "").trim();
        return (StringUtils.isAllUpperCase(temp)) ? true : false;
    }

    public boolean isImmediatelyPrecededByANegation(String status, String termOrig) {
        term = term.toLowerCase();
        int indexTerm = StringUtils.indexOf(status, term);
        String[] temp = StringUtils.left(status, indexTerm).split(" ");

        //if the array is empty it means that the term is the first of the status;
        if (temp.length == 0) {
            return false;
            //in this case the term is the second in the status. If the previous one is a negative word, return true (as in "like" being preceded by "don't") 
        } else if (temp.length == 1) {
            boolean res = ControllerBean.Hloader.setNegations.contains(temp[0]) ? true : false;
            return res;
            //in this case the term is preceded by many other terms. We just check the three previous ones.    
        } else if (temp.length == 2) {
            if (ControllerBean.Hloader.setNegations.contains(temp[temp.length - 1])) {
                return true;
            }
            //in the case of "don't really like", return true
            if (ControllerBean.Hloader.getMapH3().containsKey(temp[temp.length - 1]) & ControllerBean.Hloader.setNegations.contains(temp[temp.length - 2])) {
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[0] + " " + temp[1];
            boolean res = (ControllerBean.Hloader.setNegations.contains(concat)) ? true : false;
            return res;
        } else if (temp.length > 2) {
            if (ControllerBean.Hloader.setNegations.contains(temp[temp.length - 1])) {
                return true;
            }
            if (ControllerBean.Hloader.getMapH3().containsKey(temp[temp.length - 1]) & ControllerBean.Hloader.setNegations.contains(temp[temp.length - 2])) {
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[temp.length - 2] + " " + temp[temp.length - 1];
            boolean res = (ControllerBean.Hloader.setNegations.contains(concat)) ? true : false;
            return res;
        }
        return false;
    }

    public boolean isFirstTermOfStatus(String status, String termOrig) {
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
        return res;
    }
}
