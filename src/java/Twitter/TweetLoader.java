/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import Classifier.HeuristicLevel1;
import Classifier.HeuristicsLoader;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class TweetLoader {

    static Datastore ds;
    private String string;
    public static HeuristicsLoader Hloader;

    public static void main(String args[]) throws UnknownHostException, FileNotFoundException, IOException, LangDetectException {


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

        List<Tweet> listTweets = ds.find(Tweet.class).asList();
        Set<Tweet> setTweets = new HashSet();
        setTweets.addAll(listTweets);

        HeuristicLevel1 hl1 = new HeuristicLevel1(setTweets);
        hl1.applyLevel1();


    }
}
