package Twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Utils.APIkeys;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPIController {

    private List<Tweet> listTweets;
    private int count = 0;
    private ConfigurationBuilder cb;

    public TwitterAPIController() {
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0HI78z6uScVkwQkwWWNA")
                .setOAuthConsumerSecret(APIkeys.getTwitterConsumerSecret())
                .setOAuthAccessToken("31805620-1QQsoAH98dSVRHXb21IBtLOrh8igwIov8NT2TvUCg")
                .setOAuthAccessTokenSecret(APIkeys.getTwitterAccessTokenSecret());
    }

    public List<Tweet> getTweetsFromSearchAPI(String string) throws TwitterException {
        listTweets = new ArrayList();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(string);
        query.lang("en");
        query.count(2000);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (Exception e) {
            return listTweets;
        }
        for (Status status : result.getTweets()) {
            listTweets.add(new Tweet(status));
        }
        return listTweets;

    }

}
