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
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author C. Levallois
 */
@ManagedBean
@SessionScoped
public class ControllerBean implements Serializable {

    public Datastore ds;
    public Datastore dsLocal;
    private ArrayList<Tweet> setTweets;
    public static HeuristicsLoader Hloader;
    private static String client = "fwefdfwee";
    private boolean saveOnDisk = false;
    private boolean analyzeNewlyArrivedTweets = false;
    private boolean analyzeAllFromDisk = false;
    private boolean loadTweetsFromLocal = false;
    private boolean loadFromTrainingFile = true;
    private boolean bigTrainingFile = true;
    private boolean clementTests = false;
    private boolean dev = true;
    private int maxTweets = 1000;
    private String dummy;
    private Query<Tweet> updateQuery;
    private UpdateOperations<Tweet> ops;
    private String userInput;
    private TweetLooper hl1;
    private List<Tweet> listTweets;

    @PostConstruct
    public void init() {
        try {
            System.out.println("UMIGON - semantic analyzer for large twitter accounts");
            Mongo m;
            Morphia morphia;
            Mongo mLocal;
            Morphia morphiaLocal;
            mLocal = new Mongo();
            morphiaLocal = new Morphia();
            setTweets = new ArrayList();

            if (dev) {
                ExcelToCsv.load();
            }

            if (!dev) {
                saveOnDisk = false;
                analyzeNewlyArrivedTweets = false;
                analyzeAllFromDisk = false;
                loadTweetsFromLocal = false;
                loadFromTrainingFile = false;
                bigTrainingFile = false;
                clementTests = false;
            }

            //loads the heuristics from the csv files just created
            Hloader = new HeuristicsLoader();
            Hloader.load();

            //loads Categories
            Categories.populate();



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

            if (saveOnDisk || analyzeAllFromDisk || loadTweetsFromLocal) {
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
                setTweets = hl1.applyLevel1(loadFromTrainingFile);
                for (Tweet tweet : setTweets) {
                    updateQuery = dsLocal.createQuery(Tweet.class).field("text").equal(tweet.getText());
                    ops = dsLocal.createUpdateOperations(Tweet.class).set("setCategories", tweet.getSetCategories());
                    dsLocal.update(updateQuery, ops, true);

                }
            }

            if (loadTweetsFromLocal) {
                listTweets = dsLocal.find(Tweet.class).asList();
                setTweets.addAll(listTweets);
                System.out.println("------------------------------------------------");
                System.out.println("retrieved all tweets from disk (collected since Dec. 02, 2012): " + setTweets.size());
            }

            if (analyzeNewlyArrivedTweets) {
                listTweets = ds.find(Tweet.class).asList();
                setTweets.addAll(listTweets);
                System.out.println("------------------------------------------------");
                System.out.println("retrieving newly arrived tweets from the cloud: " + setTweets.size());
                hl1 = new TweetLooper(setTweets);
                hl1.applyLevel1(loadFromTrainingFile);

            }

            if (loadFromTrainingFile) {
                ExternalSourceTweetLoader comp = new ExternalSourceTweetLoader();
                if (bigTrainingFile) {
                    setTweets = comp.sentimentBigSetLoader(maxTweets);
                } else if (clementTests) {
                    setTweets = comp.clementTestTweetsLoader(maxTweets);
                } else {
                    setTweets = comp.sentiment140Loader();
                }
                System.out.println("------------------------------------------------");
                System.out.println("tweets from training file: " + setTweets.size());
                hl1 = new TweetLooper(setTweets);
                hl1.applyLevel1(loadFromTrainingFile);
            }



        } catch (LangDetectException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MongoException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public String classify() throws LangDetectException {

        ExternalSourceTweetLoader comp = new ExternalSourceTweetLoader();

        setTweets = comp.userInputTweets(userInput);
        System.out.println("------------------------------------------------");
        System.out.println("tweets from training file: " + setTweets.size());
        hl1 = new TweetLooper(setTweets);
        setTweets = hl1.applyLevel1(loadFromTrainingFile);

        return "result.xhtml?faces-redirect=true";

    }

    public static String getClient() {
        return client.toLowerCase();
    }

    public ArrayList<Tweet> getSetTweets() {
        return setTweets;
    }

    public void setSetTweets(ArrayList<Tweet> setTweets) {
        this.setTweets = setTweets;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public boolean isClementTests() {
        return clementTests;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public List<Tweet> getListTweets() {
        listTweets = new ArrayList();
        listTweets.addAll(setTweets);
        return listTweets;
    }

    public void setListTweets(List<Tweet> listTweets) {
        this.listTweets = listTweets;
    }

    public String reinit() {
        return "index.xhtml?faces-redirect=true";
    }
    
    
}
