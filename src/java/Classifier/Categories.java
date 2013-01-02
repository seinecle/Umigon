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

    private static HashMap<Integer, String> mapCategories;

    public static void populate() {
        mapCategories = new HashMap();
        //
        // *** 1 *** tone
        //
        mapCategories.put(10, "neutral tone");
        mapCategories.put(11, "positive tone");
        mapCategories.put(12, "negative tone");
        mapCategories.put(13, "possibly ironic tone");
        mapCategories.put(14, "fun tone");

        //
        // *** 2 *** intensity
        //
        mapCategories.put(20, "neutral intensity");
        mapCategories.put(21, "weak intensity");
        mapCategories.put(22, "strong intensity");

        //
        // *** 3 *** time
        //
        mapCategories.put(30, "neutral time");
        mapCategories.put(31, "past time");
        mapCategories.put(311, "immediate past");
        mapCategories.put(320, "present time");
        mapCategories.put(321, "immediate present: just now");
        mapCategories.put(330, "future time");
        mapCategories.put(331, "immediate future");

        //
        // *** 4 *** question
        //
        mapCategories.put(40, "question");

        //
        // *** 5 *** type of address
        //
        mapCategories.put(50, "neutral address");
        mapCategories.put(51, "subjective address");
        mapCategories.put(52, "direct address");

        //
        // *** 6 *** topic
        //
        mapCategories.put(60, "neutral topic");
        mapCategories.put(61, "commercial");
        mapCategories.put(611, "commercial offer");
        mapCategories.put(62, "factual statement");
        mapCategories.put(621, "factual statement - statistics cited");

    }

    public static String get(int i) {
        return mapCategories.get(i);
    }
}
