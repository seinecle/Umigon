/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RuleInterpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class Interpreter {

    public static void main(String args[]) {
        String rule = "A?(B?(321:C?(322:355)):122)";
        HashMap mapHeuristics = new HashMap();
        mapHeuristics.put("A", true);
        mapHeuristics.put("B", false);
        mapHeuristics.put("C", false);
        interprete(rule, mapHeuristics);
        System.out.println("rule: " + rule);

    }

    public static Integer interprete(String rule, Map<String, Boolean> heuristics) {
        Integer res = null;
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


        Scanner s = new Scanner(rule);
        s.useDelimiter("");
        while (s.hasNext()) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            chr = s.next().charAt(0);
            sign = CharUtils.toString(chr);

            if (!StringUtils.isAlphanumeric(sign)) {
                prepunct = punct;
                punct = CharUtils.toString(chr);
                if (punct.equals("(")) {
                    openParent++;
//                System.out.println("openParent = " + openParent);
                    System.out.println("openParent - closedParent = " + (openParent - closedParent));
                }

                if (punct.equals(")")) {
                    closedParent++;
//                System.out.println("closedParent = " + closedParent);
                    System.out.println("openParent - closedParent = " + (openParent - closedParent));
                }
                sb = null;
            } else {
                sb.append(sign);
                token = sb.toString();
                System.out.println("token: " + token);
            }
            System.out.println("curr prepunct: " + prepunct);
            System.out.println("curr punct: " + punct);
            System.out.println("curr sign: " + sign);


            //after a condition is evaluated as false, continue to record the tokens but don't evaluate conditions until you find a closing bracket
            if (!currBoolean & (openParent - closedParent != 0)) {
                System.out.println("deciding we ARE in skip mode ");
                System.out.println("openParent - closedParent = " + (openParent - closedParent));
                skippingToFalse = true;
            } else {
                System.out.println("deciding we are NOT in skip mode ");
                System.out.println("openParent - closedParent = " + (openParent - closedParent));
                System.out.println("curr Boolean: " + currBoolean);

                skippingToFalse = false;
            }

            //evaluation of conditions, which are always preceding a question mark
            if (!skippingToFalse & sign.equals("?")) {
                System.out.println("? found, condition going to be evaluated: " + token);
                currBoolean = heuristics.get(token);
                System.out.println("condition " + token + " evaluated, returns: " + currBoolean);
                openParent = 0;
                closedParent = 0;
                sb = null;
            }


            //if we are in a true condition, return the current token
            if (currBoolean & StringUtils.isNumeric(token)) {
                System.out.println("token returned: " + token);
                return Integer.parseInt(token);
            }

            //if we are in a false condition, return the token just after the semicolon
            if (!skippingToFalse & !currBoolean
                    & prepunct.equals(":")
                    & StringUtils.isNumeric(token)) {
                System.out.println("curr sign: " + sign);
                System.out.println("token returned after a semi-colon: " + token);
                return Integer.parseInt(token);
            }
//            return Integer.parseInt(token);
        }
        return -99;

    }
}
