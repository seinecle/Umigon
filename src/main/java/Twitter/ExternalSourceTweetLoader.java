/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class ExternalSourceTweetLoader {

    List<Tweet> tweets;
    

    public ExternalSourceTweetLoader() {
    }
    
    

    public List<Tweet> userInputTweets(String userInput) {
        String sep = "\n";
        String[] userTweets = userInput.split(sep);
        Tweet tweet;
        List<Tweet> tweets = new ArrayList();

        for (String string : userTweets) {
            tweet = new Tweet();
            tweet.setText(string);
            tweets.add(tweet);

        }
        return tweets;
    }
}