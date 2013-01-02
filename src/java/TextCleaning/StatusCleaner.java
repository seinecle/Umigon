/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextCleaning;

/**
 *
 * @author C. Levallois
 */
public class StatusCleaner {

    public static String clean(String status) {
        status = status.replace("...", " ");
//            System.out.println(status);
        status = status.replaceAll("http[^ ]*", " ");
        status = status.replaceAll("\".*\"", " ");
        status = status.replaceAll("http.*[\r|\n]*", " ");
        status = status.replaceAll(" +", " ");

        return status;
    }
}
