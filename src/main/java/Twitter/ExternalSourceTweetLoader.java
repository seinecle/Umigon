/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import Classifier.ClassifierMachine;
import LanguageDetection.Cyzoku.util.LangDetectException;
import Utils.Clock;
import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class ExternalSourceTweetLoader {

    BufferedReader br;
    ArrayList<Tweet> tweets;
    
    @Inject ClassifierMachine cm;

    public ExternalSourceTweetLoader() {
    }
    
    

    public ArrayList<Tweet> semanticCompetitionLoader() throws FileNotFoundException, IOException {
        br = new BufferedReader(new FileReader("C:\\Python27-64b\\output_file"));
        String line;
        String cat;
        String text;
        Tweet tweet;
        tweets = new ArrayList();
        while ((line = br.readLine()) != null) {
            String[] fields = line.split("\t");
            cat = fields[3];
            text = fields[4];
            tweet = new Tweet();
            tweet.setText(text);
            tweet.setTrainingSetCat(cat.replaceAll("\"", ""));
            tweets.add(tweet);
        }
        return tweets;
    }

    public ArrayList<Tweet> sentiment140Loader() throws FileNotFoundException, IOException {
        br = new BufferedReader(new FileReader("D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\testsettabdelimited.txt"));
        String line;
        String cat;
        String text;
        Tweet tweet;
        tweets = new ArrayList();
        while ((line = br.readLine()) != null) {
            String[] fields = line.split("\t");
            cat = fields[0];
            text = fields[5];
            tweet = new Tweet();
            tweet.setText(text);
            tweet.setTrainingSetCat(cat.replaceAll("\"", ""));
            tweets.add(tweet);
        }
        return tweets;
    }

    public ArrayList<Tweet> sentimentBigSetLoader(int max, String term) throws FileNotFoundException, IOException, LangDetectException {
        String fieldDelimiter = ",";
        String textDelimiter = "\"";
        String fileName = "D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\training.1600000.processed.noemoticon.csv";
//        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(fileName)), fieldDelimiter.charAt(0));
//        csvReader.setTextQualifier(textDelimiter.charAt(0));
//        csvReader.setUseTextQualifier(true);
        Tweet tweet;
        String[] values;
        ArrayList<Tweet> setTweets = new ArrayList();
        int counter = 0;

        br = new BufferedReader(new FileReader(fileName));
        String line;
        int count = 0;
        ArrayList<String> stringTweets = new ArrayList();
        Clock timingImport = new Clock("import");
        
        while ((line = br.readLine()) != null & count < 2000000) {
            if (count % 100000 == 0) {
                System.out.println("count:" + count);
                timingImport.printElapsedTime();
            }
            if (line.split(",", 6).length < 6) {
                continue;
            }
            stringTweets.add(line);
            count++;
        }
        br.close();
        timingImport.closeAndPrintClock();

        Clock timing = new Clock("classifying");
        for (String string : stringTweets) {
            tweet = new Tweet();
            tweet.setText(string.split(",", 6)[5]);
            tweet = cm.classifySingleTweet(tweet);
        }
        timing.closeAndPrintClock();
//        while (csvReader.readRecord() && counter < max) {
//            values = csvReader.getValues();
//            tweet = new Tweet();
//            tweet.setTrainingSetCat(values[0]);
//            tweet.setText(values[5]);
//            setTweets.add(tweet);
//            counter++;
//
//        }
        return setTweets;
    }

    public ArrayList<Tweet> semevalTestSetLoader() throws FileNotFoundException, IOException {
        String fieldDelimiter = "\t";
        String textDelimiter = "\"";
        String fileName = "D:\\Docs Pro Clement\\E-projects\\SEMEVAL 2013\\twitter-test-input-B.tsv";
        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(fileName)), fieldDelimiter.charAt(0));
        csvReader.setTextQualifier(textDelimiter.charAt(0));
        csvReader.setUseTextQualifier(false);
        Tweet tweet;
        String[] values;
        ArrayList<Tweet> setTweets = new ArrayList();
        int counter = 0;
        String text;
        while (csvReader.readRecord()) {
            values = csvReader.getValues();
            tweet = new Tweet();
            text = StringEscapeUtils.unescapeJava(values[3]);
            tweet.setSemevalId(values[1]);
            tweet.setText(text);
            setTweets.add(tweet);
            counter++;

        }
        return setTweets;
    }

    public ArrayList<Tweet> clementTestTweetsLoader(int max) throws FileNotFoundException, IOException {
        String fieldDelimiter = "\t";
        String textDelimiter = "\"";
        String fileName = "D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\myTests.csv";
        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(fileName)), fieldDelimiter.charAt(0));
        csvReader.setTextQualifier(textDelimiter.charAt(0));
        csvReader.setUseTextQualifier(true);
        Tweet tweet;
        String[] values;
        ArrayList<Tweet> setTweets = new ArrayList();
        int counter = 0;

        while (csvReader.readRecord() && counter < max) {
            tweet = new Tweet();
            values = csvReader.getValues();
            tweet.setTrainingSetCat(values[0]);
            tweet.setText(values[1]);
            setTweets.add(tweet);
            counter++;

        }
        return setTweets;
    }

    public ArrayList<Tweet> userInputTweets(String userInput) {
        String sep = "\n";
        System.out.println("user input: " + userInput);
        String[] userTweets = userInput.split(sep);
        Tweet tweet;
        String[] values;
        ArrayList<Tweet> setTweets = new ArrayList();
        int counter = 0;

        for (String string : userTweets) {
            tweet = new Tweet();
            tweet.setText(string);
            setTweets.add(tweet);
            counter++;

        }
        return setTweets;
    }
}
