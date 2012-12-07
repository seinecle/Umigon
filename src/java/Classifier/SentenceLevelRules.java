/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

/**
 *
 * @author C. Levallois
 */
public class SentenceLevelRules {

    public static boolean containsPercentage(String status) {
        boolean res = (status.matches(".*\\d%.*")| status.matches(".*\\d/\\d.*"));
//        if (res) {
//            System.out.println("status: " + status);
//        }
        return res;
    }
}
