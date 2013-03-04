/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextCleaning;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class StatusCleaner {

    public String clean(String status) {
        status = status.replace("...", " ");
        status = status.replace("..", " ");
//            System.out.println(status);
        status = status.replaceAll("http[^ ]*", " ");
        status = status.replaceAll("-", " ");
//        status = status.replaceAll("\".*\"", " ");
        status = status.replaceAll("http.*[\r|\n]*", " ");
        status = status.replaceAll(" +", " ");

        return status;
    }

    public String removePunctuationSigns(String string) {
        string = StringUtils.removeEnd(string, "'s");
        string = StringUtils.removeEnd(string, "’s");
        String punctuation = "!?.'\"-,()#=*";
        char[] chars = punctuation.toCharArray();
        for (char currChar : chars) {
            string = StringUtils.remove(string, currChar);
        }
        return string;
    }
}
