package Twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Utils.APIkeys;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPIController {

    private List<Tweet> listTweets;
    private int count = 0;
    private TwitterStream twitterStream;
    private ConfigurationBuilder cb;

    public TwitterAPIController() {
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0HI78z6uScVkwQkwWWNA")
                .setOAuthConsumerSecret(APIkeys.getTwitterConsumerSecret())
                .setOAuthAccessToken("31805620-1QQsoAH98dSVRHXb21IBtLOrh8igwIov8NT2TvUCg")
                .setOAuthAccessTokenSecret(APIkeys.getTwitterAccessTokenSecret())
                .setUseSSL(true);
    }

    public List<Tweet> getTweetsFromSearchAPI(String string) throws TwitterException {
         listTweets = new ArrayList();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(string);
        query.lang("en");
        query.count(2000);
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            listTweets.add(new Tweet(status));
        }
        return listTweets;

    }

    public List<Tweet> getTweetsFromStream(String keywords[]) throws UnknownHostException, FileNotFoundException, IOException, TwitterException {


         listTweets= new ArrayList();

        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        twitterStream = tf.getInstance();


        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                if ("en".equals(status.getUser().getLang())) {
                    Tweet tweet = new Tweet(status);
                    listTweets.add(tweet);
                    System.out.println("new tweet saved, " + count++);

                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                System.out.println("Exception: " + ex);
            }
        };
        long[] users = {17193794}; //this id is @hp's
        twitterStream.addListener(listener);
        FilterQuery fq = new FilterQuery();
        fq.track(keywords);
//        twitterStream.filter(new FilterQuery(0, users, keywords));
        twitterStream.filter(fq);
        return listTweets;
    }
}
