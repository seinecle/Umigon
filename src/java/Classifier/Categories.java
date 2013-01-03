/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import java.util.HashMap;

/**
 *
 * @author C. Levallois
 */
public class Categories {

    private static HashMap<String, String> mapCategories;

    public static void populate() {
        mapCategories = new HashMap();
        //
        // *** 1 *** tone
        //
        mapCategories.put("010", "neutral tone");
        mapCategories.put("011", "positive tone");
        mapCategories.put("012", "negative tone");
        mapCategories.put("013", "possibly ironic tone");
        mapCategories.put("014", "fun tone");

        //
        // *** 2 *** intensity
        //
        mapCategories.put("020", "neutral intensity");
        mapCategories.put("021", "weak intensity");
        mapCategories.put("022", "strong intensity");

        //
        // *** 3 *** time
        //
        mapCategories.put("030", "neutral time");
        mapCategories.put("031", "past time");
        mapCategories.put("0311", "immediate past");
        mapCategories.put("0320", "present time");
        mapCategories.put("0321", "immediate present: just now");
        mapCategories.put("033", "future time");
        mapCategories.put("0331", "immediate future");

        //
        // *** 4 *** question
        //
        mapCategories.put("040", "question");

        //
        // *** 5 *** type of address
        //
        mapCategories.put("050", "neutral address");
        mapCategories.put("051", "subjective address");
        mapCategories.put("052", "direct address");

        //
        // *** 6 *** topic
        //
        mapCategories.put("060", "neutral topic");
        mapCategories.put("061", "commercial");
        mapCategories.put("0611", "commercial offer");
        mapCategories.put("062", "factual statement");
        mapCategories.put("0621", "factual statement - statistics cited");

    }

    public static String get(String i) {
//        System.out.println("code of category returned: "+ i);
        return mapCategories.get(i);
    }
}
