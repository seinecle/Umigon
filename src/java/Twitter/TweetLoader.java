/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import Classifier.Categories;
import Classifier.TweetLooper;
import Heuristics.HeuristicsLoader;
import Heuristics.ExcelToCsv;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author C. Levallois
 */
public class TweetLoader {

    static Datastore ds;
    static Datastore dsLocal;
    private String string;
    public static HeuristicsLoader Hloader;
    static private boolean saveOnDisk = false;
    static private boolean analyzeNewlyArrivedTweets = false;
    static private boolean analyzeAllFromDisk = true;

    public static void main(String args[]) throws UnknownHostException, FileNotFoundException, IOException, LangDetectException, InvalidFormatException {
        System.out.println("OMEGAN - semantic analyzer for large twitter accounts");
        Mongo m;
        Morphia morphia;
        Mongo mLocal;
        Morphia morphiaLocal;
        mLocal = new Mongo();
        morphiaLocal = new Morphia();
        List<Tweet> listTweets;
        Set<Tweet> setTweets = new HashSet();
        String[] newArgs = null;
        
        //converts excel file with heuristics into csv that Java can handle
        ExcelToCsv.main(newArgs);
        
        //loads the heuristics from the csv files just created
        Hloader = new HeuristicsLoader();
        Hloader.load();
        
        //loads Categories
        Categories.populate();
        
        TweetLooper hl1;

        if (saveOnDisk || analyzeNewlyArrivedTweets) {
            m = new Mongo("alex.mongohq.com", 10056);
            morphia = new Morphia();
            String pass = "testpass";
            ds = morphia.createDatastore(m, "0FwGVJmwy8ouyeZ1z6p7xQ", "seinecle", pass.toCharArray());
            if (ds != null) {
                System.out.println("Morphia datastore on CloudBees / MongoHQ created!!!!!!!");
            }
            morphia.map(Tweet.class);
            listTweets = ds.find(Tweet.class).asList();
            setTweets.addAll(listTweets);

        }

        if (saveOnDisk || analyzeAllFromDisk) {
            dsLocal = morphiaLocal.createDatastore(mLocal, "hp");
            morphiaLocal.map(Tweet.class);
        }

        if (saveOnDisk) {
            Iterator<Tweet> setTweetsIterator = setTweets.iterator();
            while (setTweetsIterator.hasNext()) {
                Tweet tweet = setTweetsIterator.next();
                dsLocal.save(tweet);
            }
            ds.delete(ds.createQuery(Tweet.class));
            System.out.println("------------------------------------------------");
            System.out.println("saved " + setTweets.size() + " on disk and deleted them fromm the cloud");

        }
        if (analyzeAllFromDisk) {
            listTweets = dsLocal.find(Tweet.class).asList();
            setTweets.addAll(listTweets);
            System.out.println("------------------------------------------------");
            System.out.println("retrieving all tweets from disk (collected since Dec. 02, 2012): " + setTweets.size());
            hl1 = new TweetLooper(setTweets);
            hl1.applyLevel1();
        }

        if (analyzeNewlyArrivedTweets) {
            listTweets = ds.find(Tweet.class).asList();
            setTweets.addAll(listTweets);
            System.out.println("------------------------------------------------");
            System.out.println("retrieving newly arrived tweets from the cloud: " + setTweets.size());
            hl1 = new TweetLooper(setTweets);
            hl1.applyLevel1();

        }

    }
}
