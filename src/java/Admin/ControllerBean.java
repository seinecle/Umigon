/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Classifier.Categories;
import Classifier.TweetLooper;
import Heuristics.HeuristicsLoader;
import LanguageDetection.Cyzoku.util.LangDetectException;
import Twitter.ExternalSourceTweetLoader;
import Twitter.Tweet;
import Twitter.TwitterAPIController;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import twitter4j.TwitterException;

/**
 *
 * @author C. Levallois
 */
@ManagedBean
@SessionScoped
public class ControllerBean implements Serializable {

    private List<Tweet> listTweets;
    private List<String> reportedClassificationErrors;
    public static HeuristicsLoader Hloader;
    private static String client = "fwefdfwee";
    private int maxTweets = 1000000000;
    private String termFilter = "";
    private String dummy;
    private String userInput;
    private String twitterStreamInput;
    private TweetLooper hl1;

    @PostConstruct
    public void init() {
        try {
            reportedClassificationErrors = new ArrayList();
            System.out.println("UMIGON - semantic analyzer for large twitter accounts");

            listTweets = new ArrayList();



            //loads the heuristics from the csv files just created
            Hloader = new HeuristicsLoader();
            Hloader.load();

            //loads Categories
            Categories.populate();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String classifyViaAPI() throws LangDetectException, UnknownHostException, FileNotFoundException, IOException, TwitterException {

        TwitterAPIController twitterAPIFetcher = new TwitterAPIController();
        listTweets = twitterAPIFetcher.getTweetsFromSearchAPI(this.getTwitterStreamInput());

        System.out.println("------------------------------------------------");
        System.out.println("tweets from training file: " + listTweets.size());
        hl1 = new TweetLooper(listTweets);
        listTweets = hl1.applyLevel1(false);

        return "result.xhtml?faces-redirect=true";

    }

    public String classifyViaUserInput() throws LangDetectException, UnknownHostException, FileNotFoundException, IOException, TwitterException {

        ExternalSourceTweetLoader comp = new ExternalSourceTweetLoader();
        listTweets = comp.userInputTweets(userInput);
        System.out.println("------------------------------------------------");
        System.out.println("tweets from user input: " + listTweets.size());
        hl1 = new TweetLooper(listTweets);
        listTweets = hl1.applyLevel1(false);

        return "result.xhtml?faces-redirect=true";

    }

    public String signal(String status, String sentiment) {
        reportedClassificationErrors.add(status + " - should not be " + sentiment);
        for (Tweet t : listTweets) {
            if (t.getText().equals(status)) {
                t.setSignaled(true);
            }
        }
        return "";
    }

    @PreDestroy
    public void sendErrorsByEmail() {
        if (reportedClassificationErrors.isEmpty()) {
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (String s : reportedClassificationErrors) {
                sb.append(s);
                sb.append("<br>");
            }

            String data = "userName=" + URLEncoder.encode("clementlevallois@gmail.com", "UTF-8");
            data += "&api_key=" + URLEncoder.encode("558fe3d8-10df-411e-a3fe-4b3f318aa644", "UTF-8");
            data += "&from=" + URLEncoder.encode("info@exploreyourdata.com", "UTF-8");
            data += "&from_name=" + URLEncoder.encode("Exploreyourdata Umigon", "UTF-8");
            data += "&subject=" + URLEncoder.encode("Umigon error reported", "UTF-8");
            data += "&body_html=" + URLEncoder.encode(sb.toString(), "UTF-8");
            data += "&to=" + URLEncoder.encode("clementlevallois@gmail.com", "UTF-8");

            System.out.println("email to elasticmail: " + data);

            //Send data
            URL url = new URL("https://api.elasticemail.com/mailer/send");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = rd.readLine();
            wr.close();
            rd.close();

            reportedClassificationErrors = new ArrayList();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getClient() {
        return client.toLowerCase();
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }


    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public List<Tweet> getListTweets() {
        return listTweets;
    }

    public void setListTweets(List<Tweet> listTweets) {
        this.listTweets = listTweets;
    }

    public String reinit() {
        return "index.xhtml?faces-redirect=true";
    }

    public String[] getTwitterStreamInputAsArray() {
        return twitterStreamInput.split(";");
    }

    public String getTwitterStreamInput() {
        return twitterStreamInput;
    }

    public void setTwitterStreamInput(String twitterStreamInput) {
        this.twitterStreamInput = twitterStreamInput;
    }
}
