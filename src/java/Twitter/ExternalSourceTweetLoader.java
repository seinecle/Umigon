/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 *
 * @author C. Levallois
 */
public class ExternalSourceTweetLoader {

    BufferedReader br;
    ArrayList<Tweet> tweets;

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

    public ArrayList<Tweet> sentimentBigSetLoader(int max, String term) throws FileNotFoundException, IOException {
        String fieldDelimiter = ",";
        String textDelimiter = "\"";
        String fileName = "D:\\Docs Pro Clement\\E-humanities\\Sentiment Analysis\\datasets\\training.1600000.processed.noemoticon.csv";
        CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(fileName)), fieldDelimiter.charAt(0));
        csvReader.setTextQualifier(textDelimiter.charAt(0));
        csvReader.setUseTextQualifier(true);
        Tweet tweet;
        String[] values;
        ArrayList<Tweet> setTweets = new ArrayList();
        int counter = 0;

        while (csvReader.readRecord() && counter < max) {
            values = csvReader.getValues();
            if (!values[5].contains(term)) {
                continue;
            }
            tweet = new Tweet();
            tweet.setTrainingSetCat(values[0]);
            tweet.setText(values[5]);
            setTweets.add(tweet);
            counter++;

        }
        return setTweets;
    }

    public ArrayList<Tweet> clementTestTweetsLoader(int max) throws FileNotFoundException, IOException {
        String fieldDelimiter = ",";
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
        String sep = System.getProperty("line.separator");
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
