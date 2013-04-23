/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Heuristics.HashtagLevelHeuristics;
import Heuristics.Heuristic;
import Heuristics.SentenceLevelHeuristicsPost;
import Heuristics.SentenceLevelHeuristicsPre;
import Heuristics.StatusEligibleHeuristics;
import LanguageDetection.LanguageDetector;
import TextCleaning.SpellCheckingMethods;
import TextCleaning.StatusCleaner;
import Twitter.Tweet;
import Admin.ControllerBean;
import Utils.Clock;
import Utils.NGramFinder;
import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.collect.HashMultiset;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
public class ClassifierMachine {

    String status;
    String statusStripped;
    HashMultiset<String> nGrams;
    Iterator<String> nGramsIterator;
    String nGramOrig;
    String nGram;
    String nGramStripped;
    int count = 0;
    Heuristic heuristic;
    SentenceLevelHeuristicsPre sentenceHeuristicsPre;
    SentenceLevelHeuristicsPost sentenceHeuristicsPost;
    HashtagLevelHeuristics hashtagHeuristics;
    Tweet tweet;
    ArrayList<Tweet> setTweetsClassified;
    LanguageDetector ld;
    boolean loadFromTrainingFile;

    public ClassifierMachine(boolean loadFromTrainingFile) {
        this.loadFromTrainingFile = loadFromTrainingFile;
    }

    public ArrayList<Tweet> classify(ArrayList<Tweet> setTweets) throws LangDetectException {
        Iterator<Tweet> setTweetsIterator = setTweets.iterator();

        Clock heuristicsClock = new Clock("starting the analysis of tweets");
        setTweetsClassified = new ArrayList();
//        ld = new LanguageDetector();
        StatusCleaner statusCleaner = new StatusCleaner();


        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            status = tweet.getText();

            if (status.equals("Not Available")) {
                continue;
            }

//            if (!loadFromTrainingFile) {
//                if (!ld.detectEnglish(status)) {
//                    tweet.addToListCategories("001");
//                    setTweetsClassified.add(tweet);
//                    continue;
//                }
//            }

            StatusEligibleHeuristics seh = new StatusEligibleHeuristics(tweet, status);
            tweet = seh.applyRules();
            if (tweet.getListCategories().contains("001") | tweet.getListCategories().contains("002")) {
                setTweetsClassified.add(tweet);
                continue;
            }

            tweet.setListCategories(null);
//            System.out.println("curr tweet: " + tweet.toString());
            status = statusCleaner.clean(status);
            statusStripped = statusCleaner.removePunctuationSigns(status);

            sentenceHeuristicsPre = new SentenceLevelHeuristicsPre(tweet, status);
            tweet = sentenceHeuristicsPre.applyRules();
            sentenceHeuristicsPre = new SentenceLevelHeuristicsPre(tweet, statusStripped);
            tweet = sentenceHeuristicsPre.applyRules();

            hashtagHeuristics = new HashtagLevelHeuristics(tweet);
            tweet = hashtagHeuristics.applyRules();

            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            String result;
            String nGramLowerCase;
            String nGramLowerCaseStripped;
            SpellCheckingMethods spellChecker;
            int indexTermOrig = 0;
            String oldNGramStripped;
            while (nGramsIterator.hasNext()) {
                nGramOrig = nGramsIterator.next().trim();

                indexTermOrig = status.indexOf(nGramOrig);
//                System.out.println("index: " + indexTermOrig);
//                System.out.println("nGramOrig: " + nGramOrig);
//                System.out.println("status: " + status);

                nGramLowerCase = nGramOrig.toLowerCase();
                //this condition puts the ngram in lower case, except if it is all in upper case (this is a valuable info)
                //                System.out.println("status: " + status);
                nGramStripped = statusCleaner.removePunctuationSigns(nGramOrig);
                spellChecker = new SpellCheckingMethods();
                oldNGramStripped = nGramStripped;
                nGramStripped = spellChecker.repeatedCharacters(nGramStripped);
                statusStripped = StringUtils.replace(statusStripped, oldNGramStripped, nGramStripped);
                nGramLowerCaseStripped = nGramStripped.toLowerCase();

//                  if (nGramLowerCaseStripped.equals("fun")) {
//                    System.out.println("stop here!");
//                }

                //this is for the case where happy" is detected in I'm so "happy" today - probable marker of irony here.
                //In doubt, at least avoid a misclassification by leaving the term out.
                if (StringUtils.endsWith(nGramLowerCase, "\"") || StringUtils.endsWith(nGramLowerCase, "&quot;")) {
                    continue;
                }

                if (ControllerBean.Hloader.getMapH1().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH1().get(nGramLowerCase);
                    result = (heuristic.checkFeatures(status, nGramOrig));
                    if (result != null) {
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH1().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("index: " + indexTermOrig);
//                    System.out.println("nGramOrig: " + nGramOrig);
//                    System.out.println("nGramStripped: " + nGramStripped);
//                    System.out.println("nGramLowerCaseStripped: " + nGramLowerCaseStripped);
//                    System.out.println("status: " + status);
//                    System.out.println("statusStripped: " + statusStripped);
                    heuristic = ControllerBean.Hloader.getMapH1().get(nGramLowerCaseStripped);
                    result = (heuristic.checkFeatures(statusStripped, nGramStripped));
                    if (result != null) {
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH2().keySet().contains(nGramLowerCase)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGramOrig);
                    heuristic = ControllerBean.Hloader.getMapH2().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }

                } else if (ControllerBean.Hloader.getMapH2().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                    heuristic = ControllerBean.Hloader.getMapH2().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH3().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH3().get(nGramLowerCase);

                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH3().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH3().get(nGramLowerCaseStripped);

                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH4().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH4().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH4().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH4().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH5().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH5().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH5().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH5().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH6().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH6().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH6().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH6().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH7().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH7().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }

                } else if (ControllerBean.Hloader.getMapH7().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH7().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH8().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH8().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH8().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH8().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (ControllerBean.Hloader.getMapH9().keySet().contains(nGramLowerCase)) {
                    heuristic = ControllerBean.Hloader.getMapH9().get(nGramLowerCase);
                    result = heuristic.checkFeatures(status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (ControllerBean.Hloader.getMapH9().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = ControllerBean.Hloader.getMapH9().get(nGramLowerCaseStripped);
                    result = heuristic.checkFeatures(statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }


            }
            sentenceHeuristicsPost = new SentenceLevelHeuristicsPost(tweet, status);
            tweet = sentenceHeuristicsPost.applyRules();

            setTweetsClassified.add(tweet);
//            if (tweet.getSetCategories().contains("011") & !tweet.getUser().toLowerCase().contains("hp")& !status.toLowerCase().contains("rt @hp")&
//                !tweet.getSetCategories().contains("012") & !tweet.getSetCategoriesToString().contains("061")) {
//                System.out.println("positive tweet, not promoted: " + status);
//                System.out.println("categories: " + tweet.getSetCategoriesToString());
//            }

        }
        heuristicsClock.closeAndPrintClock();
        return setTweetsClassified;


    }

    public Tweet classifySingleTweet(Tweet tweet) throws LangDetectException {
        StatusCleaner statusCleaner = new StatusCleaner();


        status = tweet.getText();

        if (status.equals("Not Available")) {
            return tweet;
        }

//            if (!loadFromTrainingFile) {
//                if (!ld.detectEnglish(status)) {
//                    tweet.addToListCategories("001");
//                    setTweetsClassified.add(tweet);
//                    continue;
//                }
//            }

        StatusEligibleHeuristics seh = new StatusEligibleHeuristics(tweet, status);
        tweet = seh.applyRules();
        if (tweet.getListCategories().contains("001") | tweet.getListCategories().contains("002")) {
            setTweetsClassified.add(tweet);
            return tweet;
        }

        tweet.setListCategories(null);
//            System.out.println("curr tweet: " + tweet.toString());
        status = statusCleaner.clean(status);
        statusStripped = statusCleaner.removePunctuationSigns(status);

        sentenceHeuristicsPre = new SentenceLevelHeuristicsPre(tweet, status);
        tweet = sentenceHeuristicsPre.applyRules();
        sentenceHeuristicsPre = new SentenceLevelHeuristicsPre(tweet, statusStripped);
        tweet = sentenceHeuristicsPre.applyRules();

        hashtagHeuristics = new HashtagLevelHeuristics(tweet);
        tweet = hashtagHeuristics.applyRules();

        nGrams = new NGramFinder(status).runIt(4, true);
        nGramsIterator = nGrams.iterator();
        String result;
        String nGramLowerCase;
        String nGramLowerCaseStripped;
        SpellCheckingMethods spellChecker;
        int indexTermOrig = 0;
        String oldNGramStripped;
        while (nGramsIterator.hasNext()) {
            nGramOrig = nGramsIterator.next().trim();
//            if (nGramOrig.equals("toooooo!")) {
//                System.out.println("stop");
//            }

            indexTermOrig = status.indexOf(nGramOrig);
//                System.out.println("index: " + indexTermOrig);
//                System.out.println("nGramOrig: " + nGramOrig);
//                System.out.println("status: " + status);

            nGramLowerCase = nGramOrig.toLowerCase();
            //this condition puts the ngram in lower case, except if it is all in upper case (this is a valuable info)
            //                System.out.println("status: " + status);
            nGramStripped = statusCleaner.removePunctuationSigns(nGramOrig);
            spellChecker = new SpellCheckingMethods();
            oldNGramStripped = nGramStripped;
            nGramStripped = spellChecker.repeatedCharacters(nGramStripped);
            statusStripped = StringUtils.replace(statusStripped, oldNGramStripped, nGramStripped);
            nGramLowerCaseStripped = nGramStripped.toLowerCase();

//                  if (nGramLowerCaseStripped.equals("fun")) {
//                    System.out.println("stop here!");
//                }

            //this is for the case where happy" is detected in I'm so "happy" today - probable marker of irony here.
            //In doubt, at least avoid a misclassification by leaving the term out.
            if (StringUtils.endsWith(nGramLowerCase, "\"") || StringUtils.endsWith(nGramLowerCase, "&quot;")) {
                continue;
            }

            if (ControllerBean.Hloader.getMapH1().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH1().get(nGramLowerCase);
                result = (heuristic.checkFeatures(status, nGramOrig));
                if (result != null) {
                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH1().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("index: " + indexTermOrig);
//                    System.out.println("nGramOrig: " + nGramOrig);
//                    System.out.println("nGramStripped: " + nGramStripped);
//                    System.out.println("nGramLowerCaseStripped: " + nGramLowerCaseStripped);
//                    System.out.println("status: " + status);
//                    System.out.println("statusStripped: " + statusStripped);
                heuristic = ControllerBean.Hloader.getMapH1().get(nGramLowerCaseStripped);
                result = (heuristic.checkFeatures(statusStripped, nGramStripped));
                if (result != null) {
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH2().keySet().contains(nGramLowerCase)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGramOrig);
                heuristic = ControllerBean.Hloader.getMapH2().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }

            } else if (ControllerBean.Hloader.getMapH2().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                heuristic = ControllerBean.Hloader.getMapH2().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH3().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH3().get(nGramLowerCase);

                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH3().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH3().get(nGramLowerCaseStripped);

                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH4().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH4().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH4().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH4().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH5().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH5().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH5().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH5().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH6().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH6().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH6().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH6().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH7().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH7().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }

            } else if (ControllerBean.Hloader.getMapH7().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH7().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH8().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH8().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH8().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH8().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (ControllerBean.Hloader.getMapH9().keySet().contains(nGramLowerCase)) {
                heuristic = ControllerBean.Hloader.getMapH9().get(nGramLowerCase);
                result = heuristic.checkFeatures(status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (ControllerBean.Hloader.getMapH9().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = ControllerBean.Hloader.getMapH9().get(nGramLowerCaseStripped);
                result = heuristic.checkFeatures(statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }


        }
        sentenceHeuristicsPost = new SentenceLevelHeuristicsPost(tweet, status);
        return tweet = sentenceHeuristicsPost.applyRules();

//            if (tweet.getSetCategories().contains("011") & !tweet.getUser().toLowerCase().contains("hp")& !status.toLowerCase().contains("rt @hp")&
//                !tweet.getSetCategories().contains("012") & !tweet.getSetCategoriesToString().contains("061")) {
//                System.out.println("positive tweet, not promoted: " + status);
//                System.out.println("categories: " + tweet.getSetCategoriesToString());
//            }




    }
}