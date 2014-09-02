/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import LanguageDetection.Cyzoku.util.LangDetectException;
import Twitter.Tweet;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author C. Levallois
 */
public class TweetLooper {

    List<Tweet> listTweets;


    public TweetLooper(List<Tweet> listTweets) {
        this.listTweets = listTweets;
    }

    public List<Tweet> applyLevel1(boolean loadFromTrainingFile) throws LangDetectException, IOException {

        ClassifierMachine cm = new ClassifierMachine(loadFromTrainingFile);
        listTweets = cm.classify(listTweets);
        return listTweets;
    }
}
