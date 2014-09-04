package Twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Utils.APIkeys;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        query.count(100);
        QueryResult result;
        int nbTweets = 0;
        int nbPages = 0;
        boolean finished = false;

        Set<Long> ids = new HashSet();
        long lowestStatusId = Long.MAX_VALUE;

        while (!finished) {
            if (nbPages++ > 5) {
                return listTweets;
            }
            try {
                result = twitter.search(query);
            } catch (Exception e) {
                return listTweets;
            }
            for (Status status : result.getTweets()) {
                if (!ids.contains(status.getId())) {
                    listTweets.add(new Tweet(status));
                    ids.add(status.getId());
                }
                // Capture the lowest (earliest) Status id
                lowestStatusId = Math.min(status.getId(), lowestStatusId);
                if (nbTweets++ > 5000) {
                    finished = true;
                }
            }
            // Subtracting one here because 'max_id' is inclusive
            query.setMaxId(lowestStatusId - 1);

        }
        return listTweets;
    }
}
