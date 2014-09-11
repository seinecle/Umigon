/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Classifier.ClassifierMachine;
import LabelFinder.ControllerLabelsFinder;
import Twitter.ExternalSourceTweetLoader;
import Twitter.Tweet;
import Twitter.TwitterAPIController;
import Utils.APIkeys;
import Utils.Clock;
import Utils.Summary;
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

    private List<Tweet> tweets;
    private List<String> reportedClassificationErrors;
    private static String client = "fwefdfwee";
    private String dummy;
    private String userInput;
    private String twitterStreamInput;
    private boolean warning = false;
    private boolean progressBarRendered = true;
    private String stage = "Fetching tweets...";

    private String label;
    
    private int neg = 0;
    private int pos = 0;
    private int neut = 0;
    private int prom = 0;

    private int negPC = 0;
    private int posPC = 0;
    private int neutPC = 0;
    private int promPC = 0;

    private int countTweetsToClassify = 0;
    private int sizeTweetsToClassify = 0;

    @Inject
    ExternalSourceTweetLoader comp;

    @Inject
    ClassifierMachine cm;
    
    @Inject
    ControllerLabelsFinder controllerLabelsFinder;

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
        tweets = new ArrayList();

        TwitterAPIController twitterAPIFetcher = new TwitterAPIController();
        tweets = twitterAPIFetcher.getTweetsFromSearchAPI(twitterStreamInput);
        if (tweets.isEmpty()) {
            return "error.xhtml?faces-redirect=true";
        }

        stage = "computing sentiment: ";
        tweets = classify(tweets);
        label = controllerLabelsFinder.findFromTweets(tweets, twitterAPIFetcher.getTwitter(),twitterStreamInput);
        Summary sum = new Summary();
        sum.init(twitterStreamInput, tweets);

        return "result.xhtml?faces-redirect=true";

    }

    public String tweetsViaUserInput() throws UnknownHostException, FileNotFoundException, IOException, TwitterException {
        progressBarRendered = true;
        reportedClassificationErrors = new ArrayList();
        tweets = comp.userInputTweets(userInput);
        if (tweets.isEmpty()) {
            return "error.xhtml?faces-redirect=true";
        }
        stage = "computing sentiment: ";
        tweets = classify(tweets);
        return "result.xhtml?faces-redirect=true";
    }

    public List<Tweet> classify(List<Tweet> tweets) {

        Iterator<Tweet> tweetsIterator = tweets.iterator();
        sizeTweetsToClassify = tweets.size();

        List<Tweet> tweetsClassified = new ArrayList();

        Clock classificationClock = new Clock("starting the analysis of tweets");
        Tweet tweet;
        while (tweetsIterator.hasNext()) {
            countTweetsToClassify++;
            tweet = cm.classify(tweetsIterator.next());
            tweetsClassified.add(tweet);
            if (tweet.isIsNegative()) {
                neg++;
            } else if (tweet.isIsPositive()) {
                pos++;
            } else {
                neut++;
            }
            if (tweet.getListCategories().contains("061")) {
                prom++;
            }

        }
        classificationClock.closeAndPrintClock();
        return tweetsClassified;
    }

    public String signal(String status, String sentiment) {
        reportedClassificationErrors.add(status + " - should not be " + sentiment);
        for (Tweet t : tweets) {
            if (t.getText().equals(status)) {
                t.setSignaled(true);
            }
        }
        return "";
    }

    @PreDestroy
    public void sendErrorsByEmail() {
        
        if (reportedClassificationErrors == null || reportedClassificationErrors.isEmpty()) {
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

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public String reinit() {
        twitterStreamInput = "";
        sizeTweetsToClassify = 0;
        countTweetsToClassify = 0;
        pos = 0;
        neut = 0;
        neg = 0;
        prom = 0;
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

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getNeut() {
        return neut;
    }

    public void setNeut(int neut) {
        this.neut = neut;
    }

    public int getProm() {
        return prom;
    }

    public void setProm(int prom) {
        this.prom = prom;
    }

    public int getNegPC() {
        return neg * 100 / sizeTweetsToClassify;
    }

    public void setNegPC(int negPC) {
        this.negPC = negPC;
    }

    public int getPosPC() {
        return pos * 100 / sizeTweetsToClassify;
    }

    public void setPosPC(int posPC) {
        this.posPC = posPC;
    }

    public int getNeutPC() {
        return neut * 100 / sizeTweetsToClassify;
    }

    public void setNeutPC(int neutPC) {
        this.neutPC = neutPC;
    }

    public int getPromPC() {
        return prom * 100 / sizeTweetsToClassify;
    }

    public void setPromPC(int promPC) {
        this.promPC = promPC;
    }

}
