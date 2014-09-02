/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import LanguageDetection.Cyzoku.util.LangDetectException;
import Twitter.Tweet;
import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class TweetLooper {

    List<Tweet> listTweets;
    @Inject ClassifierMachine cm;


    public TweetLooper() {
    }

    public List<Tweet> applyLevel1(List<Tweet> listTweets) throws LangDetectException, IOException {
        this.listTweets = listTweets;

        listTweets = cm.classify(listTweets);
        return listTweets;
    }
}