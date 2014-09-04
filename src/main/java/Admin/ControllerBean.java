/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Classifier.ClassifierMachine;
import Twitter.ExternalSourceTweetLoader;
import Twitter.Tweet;
import Twitter.TwitterAPIController;
import Utils.APIkeys;
import Utils.Clock;
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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
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
    private static String client = "fwefdfwee";
    private String dummy;
    private String userInput;
    private String twitterStreamInput;
    private boolean warning = false;
    private boolean progressBarRendered = true;
    private String stage = "Fetching tweets...";

    private int countTweetsToClassify = 0;
    private int sizeTweetsToClassify = 0;

    @Inject
    ExternalSourceTweetLoader comp;

    @Inject
    ClassifierMachine cm;

    public ControllerBean() {
    }

    public void msg() {
        warning = true;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public String tweetsViaAPI() throws UnknownHostException, FileNotFoundException, IOException, TwitterException {
        progressBarRendered = true;
        reportedClassificationErrors = new ArrayList();
        System.out.println("UMIGON - semantic analyzer for large twitter accounts");
        listTweets = new ArrayList();

        TwitterAPIController twitterAPIFetcher = new TwitterAPIController();
        listTweets = twitterAPIFetcher.getTweetsFromSearchAPI(this.getTwitterStreamInput());
        if (listTweets.isEmpty()) {
            return "error.xhtml?faces-redirect=true";
        }

        stage = "computing sentiment: ";
        listTweets = classify(listTweets);

        return "result.xhtml?faces-redirect=true";

    }

    public String tweetsViaUserInput() throws UnknownHostException, FileNotFoundException, IOException, TwitterException {
        progressBarRendered = true;
        reportedClassificationErrors = new ArrayList();
        listTweets = comp.userInputTweets(userInput);
        if (listTweets.isEmpty()) {
            return "error.xhtml?faces-redirect=true";
        }
        stage = "computing sentiment: ";
        listTweets = classify(listTweets);
        return "result.xhtml?faces-redirect=true";
    }

    public List<Tweet> classify(List<Tweet> tweets) {

        Iterator<Tweet> tweetsIterator = tweets.iterator();
        sizeTweetsToClassify = tweets.size();

        List<Tweet> tweetsClassified = new ArrayList();

        Clock classificationClock = new Clock("starting the analysis of tweets");
        while (tweetsIterator.hasNext()) {
            countTweetsToClassify++;
            tweetsClassified.add(cm.classify(tweetsIterator.next()));
        }
        classificationClock.closeAndPrintClock();
        return tweetsClassified;
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
            data += "&api_key=" + URLEncoder.encode(APIkeys.getElasticMailAPIKey(), "UTF-8");
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

    public int getCountTweetsToClassify() {
        return countTweetsToClassify;
    }

    public void setCountTweetsToClassify(int countTweetsToClassify) {
        this.countTweetsToClassify = countTweetsToClassify;
    }

    public int getSizeTweetsToClassify() {
        return sizeTweetsToClassify;
    }

    public void setSizeTweetsToClassify(int sizeTweetsToClassify) {
        this.sizeTweetsToClassify = sizeTweetsToClassify;
    }

    public boolean isProgressBarRendered() {
        return progressBarRendered;
    }

    public void setProgressBarRendered(boolean progressBarRendered) {
        this.progressBarRendered = progressBarRendered;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
