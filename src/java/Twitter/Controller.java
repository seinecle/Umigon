package Twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Heuristics.HeuristicsLoader;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Controller {

    static Datastore ds;
    private String string;
    public static HeuristicsLoader Hloader;

    public static void test(String args[]) throws UnknownHostException, FileNotFoundException, IOException {


        Hloader = new HeuristicsLoader();
        Hloader.load();
        

        Mongo m;
        Morphia morphia;
        System.out.println("beginning morphia init");
        m = new Mongo("alex.mongohq.com", 10056);
        morphia = new Morphia();
        String pass = "testpass";
        ds = morphia.createDatastore(m, "0FwGVJmwy8ouyeZ1z6p7xQ", "seinecle", pass.toCharArray());
        if (ds != null) {
            System.out.println("Morphia datastore on CloudBees / MongoHQ created!!!!!!!");
        }
        morphia.map(Tweet.class);


        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("0HI78z6uScVkwQkwWWNA")
                .setOAuthConsumerSecret("VVsf4qT0DCBeLODRDnBlhwxrRG6KLm0TT2wiK2Q")
                .setOAuthAccessToken("31805620-1QQsoAH98dSVRHXb21IBtLOrh8igwIov8NT2TvUCg")
                .setOAuthAccessTokenSecret("P7l7SmHiZyE11tNfsQ17fdSzJ7sUpMJAliundncpaA");

        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        TwitterStream twitterStream = tf.getInstance();


        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                if ("en".equals(status.getUser().getLang())) {
                    Tweet tweet = new Tweet(status);
                    ds.save(tweet);
                    System.out.println("new tweet saved");
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
        String[] keywords = {"@hp"};
        long[] users = {17193794}; //this id is @hp's
        twitterStream.addListener(listener);
        twitterStream.filter(new FilterQuery(0, users, keywords));
    }
}
