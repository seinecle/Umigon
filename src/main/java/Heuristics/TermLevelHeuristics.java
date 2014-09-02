/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Heuristics;

import RuleInterpreter.Interpreter;
import Singletons.HeuristicsLoader;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
@Stateless
public class TermLevelHeuristics {

    private String termHeuristic;
    private Multimap<String, Set<String>> mapFeatures;
    private String rule;
    
    @Inject HeuristicsLoader HLoader;
    @Inject Interpreter interpreter;

    public TermLevelHeuristics() {
    }

    public String checkFeatures(Heuristic heuristic, String status, String termOrig) {
        this.termHeuristic = heuristic.getTerm();
        this.mapFeatures = heuristic.getMapFeatures();
        this.rule = heuristic.getRule();

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

        for (Map.Entry<String, Set<String>> feature : mapFeatures.entries()) {
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
        String result = interpreter.interprete(rule, conditions);
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
        return (HLoader.getMapH2().keySet().contains(temp)
                || HLoader.getMapH1().keySet().contains(temp))
                ? true : false;
    }

    public boolean isFollowedByAPositiveOpinion(String status, String termOrig) {
        int indexTerm = status.indexOf(termOrig);
        String temp = status.substring(indexTerm).trim();

        for (String positiveTerm : HLoader.getMapH1().keySet()) {
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
        return (HLoader.getMapH1().keySet().contains(temp))
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
        boolean result = (HLoader.getSetTimeTokens().contains(temp))
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
        return (termOrig.startsWith(termHeuristic)) ? true : false;
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
        if (HLoader.getMapH3().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean isImmediatelyPrecededByPositive(String status, String termOrig) {
        String[] temp = status.substring(0, status.indexOf(termOrig)).trim().split(" ");
        if (HLoader.getMapH1().containsKey(temp[temp.length - 1].trim().toLowerCase())) {
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
        termHeuristic = termHeuristic.toLowerCase();
        int indexTerm = StringUtils.indexOf(status, termHeuristic);
        String[] temp = StringUtils.left(status, indexTerm).split(" ");

        //if the array is empty it means that the term is the first of the status;
        if (temp.length == 0) {
            return false;
            //in this case the term is the second in the status. If the previous one is a negative word, return true (as in "like" being preceded by "don't") 
        } else if (temp.length == 1) {
            boolean res = HLoader.getSetNegations().contains(temp[0]) ? true : false;
            return res;
            //in this case the term is preceded by many other terms. We just check the three previous ones.    
        } else if (temp.length == 2) {
            if (HLoader.getSetNegations().contains(temp[temp.length - 1])) {
                return true;
            }
            //in the case of "don't really like", return true
            if (HLoader.getMapH3().containsKey(temp[temp.length - 1]) & HLoader.getSetNegations().contains(temp[temp.length - 2])) {
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[0] + " " + temp[1];
            boolean res = (HLoader.getSetNegations().contains(concat)) ? true : false;
            return res;
        } else if (temp.length > 2) {
            if (HLoader.getSetNegations().contains(temp[temp.length - 1])) {
                return true;
            }
            if (HLoader.getMapH3().containsKey(temp[temp.length - 1]) & HLoader.getSetNegations().contains(temp[temp.length - 2])) {
                return true;
            }
            //in the case of "not the hottest", return true
            String concat = temp[temp.length - 2] + " " + temp[temp.length - 1];
            boolean res = (HLoader.getSetNegations().contains(concat)) ? true : false;
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
