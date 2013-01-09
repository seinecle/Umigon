/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RuleInterpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 *
 * This class interprets conditions written in human-readable format, and
 * returns the outcome
 *
 * example below: human-written rule:
 *
 * A?(B?(321:C?(322:355)):122)
 *
 * => letters are true / false conditions => digits are outcomes => reads like:
 * "is A true? if so, evaluate B. If not, return 122. Is B true? if so, return
 * 321. If not, evaluate C. Etc...
 *
 * in the example below, we imagine that A, C D are true, while B is false.
 *
 * This class evaluates the expression: A is true => evaluate B B is false: skip
 * 321, go and evaluate C C is true: return 322
 *
 *
 * Why is it interesting? In this example, simple digits are returned. These
 * digits can be mapped to complex objects in a map (or in a dictionary as you
 * would say in Python). So that this class provides a bridge between complex
 * rules set in combination and complex outcomes.
 *
 */
public class Interpreter {

    public static void main(String args[]) {
        String rule = "A?(B?(321:C?(322:355)):122)";
        HashMap mapHeuristics = new HashMap();
        mapHeuristics.put("A", true);
        mapHeuristics.put("B", false);
        mapHeuristics.put("C", true);
        mapHeuristics.put("D", true);
        interprete(rule, mapHeuristics);
        System.out.println("rule: " + rule);

    }

    public static String interprete(String rule, Map<String, Boolean> heuristics) {
        StringBuilder sb = null;
        String token = null;
        String punct = "";
        String prepunct = "";
        char chr;
        String sign;
        Boolean currBoolean = true;
        boolean skippingToFalse = false;
        int openParent = 0;
        int closedParent = 0;
        int currParentCount = 0;

        // for debugging purposes
//        for (Entry entry : heuristics.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
        //case when there is only one condition
        if (heuristics.entrySet().size() < 2) {
//            System.out.println("rule: " + rule);
            return simpleInterpretation(rule, heuristics);
        }

        //more complex conditions...
//        System.out.println("rule: " + rule);
        Scanner s = new Scanner(rule);
        s.useDelimiter("");
        while (s.hasNext()) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            chr = s.next().charAt(0);
            sign = CharUtils.toString(chr);

            if (!StringUtils.isAlphanumeric(sign)) {
                token = sb.toString();
                prepunct = punct;
                punct = CharUtils.toString(chr);
                if (punct.equals("(")) {
                    openParent++;
                    currParentCount = openParent - closedParent;
//                    System.out.println("prevParentCount: " + prevParentCount);
//                    System.out.println("currParentCount: " + currParentCount);
                }

                if (punct.equals(")")) {
                    closedParent++;
                    currParentCount = openParent - closedParent;
//                    System.out.println("prevParentCount: " + prevParentCount);
//                    System.out.println("currParentCount: " + currParentCount);
                }
                sb = null;

            } else {
                sb.append(sign);
//                System.out.println("token: " + token);
            }
//            System.out.println("curr prepunct: " + prepunct);
//            System.out.println("curr punct: " + punct);
//            System.out.println("curr sign: " + sign);
//            System.out.println("curr token: " + token);

            //if we are in a true condition, return the current token
            if (currBoolean & StringUtils.isNumeric(token)) {
//                System.out.println("token returned: " + token);
                return token;
            }

            //if we are in a false condition and we are not skipping, return the token just after the semicolon
            if (!skippingToFalse & !currBoolean
                    & prepunct.equals(":")
                    & StringUtils.isNumeric(token)) {
//                System.out.println("curr sign: " + sign);
//                System.out.println("token returned: " + token);
                return token;
            }

            //after a condition is evaluated as false, continue to record the tokens but don't evaluate conditions until you find a closing bracket
            if (!currBoolean & (currParentCount == 1) & !punct.equals(":")) {
//                System.out.println("deciding we ARE in skip mode ");
//                System.out.println("prevParentCount: " + prevParentCount);
//                System.out.println("currParentCount: " + currParentCount);
//                System.out.println("curr Boolean: " + currBoolean);
                skippingToFalse = true;
            }

            if (!currBoolean & (currParentCount == 1) & punct.equals(":")) {
//                System.out.println("deciding we are NOT in skip mode ");
//                System.out.println("prevParentCount: " + prevParentCount);
//                System.out.println("currParentCount: " + currParentCount);
//                System.out.println("curr Boolean: " + currBoolean);
                skippingToFalse = false;
                openParent = 0;
                closedParent = 0;
                currParentCount = 0;

            }

            //evaluation of conditions, which are always preceding a question mark
            if (!skippingToFalse & sign.equals("?")) {
//                System.out.println("? found, condition going to be evaluated: " + token);
                currBoolean = heuristics.get(token);
//                System.out.println("condition " + token + " evaluated, returns: " + currBoolean);
                openParent = 0;
                closedParent = 0;
                currParentCount = 0;

                sb = null;
            }

        }
        if (!skippingToFalse) {
            System.out.println("some mistake must have happened in the interpreter: returning -99");
            System.out.println("rule: " + rule);
            for (Entry entry : heuristics.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            return "-99";
        } else {
            return null;
        }

    }

    private static String simpleInterpretation(String rule, Map<String, Boolean> heuristics) {
        String[] outcomes = rule.split("\\|");
        if (!heuristics.values().isEmpty()) {
            if (heuristics.values().iterator().next()) {
                return outcomes[0];
            } else if (outcomes.length > 1) {
                return outcomes[1];
            } else {
                return null;
            }
        } else {
            return rule;
        }
    }
}
