/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextCleaning;

import com.google.common.collect.Multiset;
import java.util.Iterator;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class StatusCleaner {

    public StatusCleaner() {
    }

    public String clean(String status) {
        status = status.replace("...", " ");
        status = status.replace(",", " ");
        status = status.replace("..", " ");
//            System.out.println(status);
        status = status.replaceAll("http[^ ]*", " ");
//        status = status.replaceAll("\".*\"", " ");
        status = status.replaceAll("http.*[\r|\n]*", " ");
        status = status.replaceAll(" +", " ");

        return status;
    }

    public String removePunctuationSigns(String string) {
        string = StringUtils.removeEnd(string, "'s");
        string = StringUtils.removeEnd(string, "’s");
        String punctuation = "!?.'’\":-+,$&()#=*";
        char[] chars = punctuation.toCharArray();
        for (char currChar : chars) {
            string = StringUtils.replace(string, String.valueOf(currChar), " ");
        }
        return string.trim();
    }

    public Multiset<String> removeSmallWords(Multiset<String> terms) {

        Iterator<String> it = terms.iterator();
        while (it.hasNext()) {
            String string = it.next();
            if (string.length() < 3 | string.matches(".*\\d.*")) {
                it.remove();
            }
        }
        return terms;

    }

}
