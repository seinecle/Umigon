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

    private List<Tweet> tweets;

    private int count = 0;
    private ConfigurationBuilder cb;
    private String stringQueried;
    private Twitter twitter;

    public TwitterAPIController() {
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0HI78z6uScVkwQkwWWNA")
                .setOAuthConsumerSecret(APIkeys.getTwitterConsumerSecret())
                .setOAuthAccessToken("31805620-1QQsoAH98dSVRHXb21IBtLOrh8igwIov8NT2TvUCg")
                .setOAuthAccessTokenSecret(APIkeys.getTwitterAccessTokenSecret());
    }

    public List<Tweet> getTweetsFromSearchAPI(String string) throws TwitterException {
        tweets = new ArrayList();
        stringQueried = string;

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        Query query = new Query(stringQueried);
        query.lang("en");
        query.count(100);
        QueryResult result;
        int nbTweets = 0;
        int nbPages = 0;
        boolean finished = false;
        Tweet tweet;
        User user;

        Set<Long> ids = new HashSet();
        long lowestStatusId = Long.MAX_VALUE;

        while (!finished) {
            if (nbPages++ > 8) {
                return tweets;
            }
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                return tweets;
            }
            for (Status status : result.getTweets()) {
                if (!ids.contains(status.getId())) {
                    if (("@" + status.getUser().getScreenName()).equals(stringQueried)) {
                        continue;
                    }
                    user = new User();
                    user.setFavoritesCount(status.getUser().getFavouritesCount());
                    user.setFollowersCount(status.getUser().getFollowersCount());
                    user.setFriendsCount(status.getUser().getFriendsCount());
                    user.setListedCount(status.getUser().getListedCount());
                    user.setId(status.getUser().getId());
                    user.setVerified(status.getUser().isVerified());
                    user.setScreenName(status.getUser().getScreenName());

                    tweet = new Tweet(status, user);
                    tweets.add(tweet);
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
        return tweets;
    }

    public Twitter getTwitter() {
        return twitter;
    }

}
