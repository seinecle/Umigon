/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

/**
 *
 * @author C. Levallois
 */
public class Categories {

    private final static String[] categories = {"positive tone", "negative tone", "strong tone", "time related info", "question asked", "first person speaks", "humorous or light", "direct address","commercial offer","dry fact"};

    public static String[] get() {
        return categories;
    }
}
