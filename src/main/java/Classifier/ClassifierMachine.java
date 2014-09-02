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
import Heuristics.TermLevelHeuristics;
import LanguageDetection.Cyzoku.util.LangDetectException;
import Singletons.HeuristicsLoader;
import TextCleaning.DoubleQuotesRemover;
import TextCleaning.SpellCheckingMethods;
import TextCleaning.StatusCleaner;
import Twitter.Tweet;
import Utils.Clock;
import Utils.NGramFinder;
import com.google.common.collect.HashMultiset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author C. Levallois
 */
@Stateless
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
    Tweet tweet;
    ArrayList<Tweet> setTweetsClassified;

    @Inject HeuristicsLoader HLoader;
    @Inject StatusEligibleHeuristics seh;
    @Inject HashtagLevelHeuristics hashtagHeuristics;
    @Inject SentenceLevelHeuristicsPre sentenceHeuristicsPre;
    @Inject SentenceLevelHeuristicsPost sentenceHeuristicsPost;
    @Inject TermLevelHeuristics termLevelHeuristics;
    @Inject StatusCleaner statusCleaner;
    @Inject SpellCheckingMethods spellCheckingMethods;
    
    

    public ClassifierMachine() {
    }


    public ArrayList<Tweet> classify(List<Tweet> listTweets) throws LangDetectException {
        Iterator<Tweet> setTweetsIterator = listTweets.iterator();

        Clock heuristicsClock = new Clock("starting the analysis of tweets");
        setTweetsClassified = new ArrayList();
//        ld = new LanguageDetector();

        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            status = tweet.getText();

            //removing content in double quotes
            boolean cleaningDone = false;
            while (!cleaningDone){
                status = DoubleQuotesRemover.remove(status);
                cleaningDone = DoubleQuotesRemover.isItCleaned(status);
            }

            tweet = seh.applyRules(tweet, status);
            if (tweet.getListCategories().contains("001") | tweet.getListCategories().contains("002")) {
                setTweetsClassified.add(tweet);
                continue;
            }

            tweet.setListCategories(null);
//            System.out.println("curr tweet: " + tweet.toString());
            status = statusCleaner.clean(status);
            statusStripped = statusCleaner.removePunctuationSigns(status);

            tweet = sentenceHeuristicsPre.applyRules(tweet, status);
            
            tweet = sentenceHeuristicsPre.applyRules(tweet, statusStripped);

            tweet = hashtagHeuristics.applyRules(tweet);

            nGrams = new NGramFinder(status).runIt(4, true);
            nGramsIterator = nGrams.iterator();
            String result;
            String nGramLowerCase;
            String nGramLowerCaseStripped;
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
                
                oldNGramStripped = nGramStripped;
                nGramStripped = spellCheckingMethods.repeatedCharacters(nGramStripped);
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

                if (HLoader.getMapH1().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH1().get(nGramLowerCase);
                    result = (termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig));
                    if (result != null) {
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH1().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("index: " + indexTermOrig);
//                    System.out.println("nGramOrig: " + nGramOrig);
//                    System.out.println("nGramStripped: " + nGramStripped);
//                    System.out.println("nGramLowerCaseStripped: " + nGramLowerCaseStripped);
//                    System.out.println("status: " + status);
//                    System.out.println("statusStripped: " + statusStripped);
                    heuristic = HLoader.getMapH1().get(nGramLowerCaseStripped);
                    result = (termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped));
                    if (result != null) {
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH2().keySet().contains(nGramLowerCase)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGramOrig);
                    heuristic = HLoader.getMapH2().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }

                } else if (HLoader.getMapH2().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                    heuristic = HLoader.getMapH2().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH3().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH3().get(nGramLowerCase);

                    result = termLevelHeuristics.checkFeatures(heuristic, status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH3().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH3().get(nGramLowerCaseStripped);

                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH4().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH4().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH4().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH4().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH5().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH5().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH5().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH5().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH6().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH6().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH6().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH6().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH7().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH7().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);
                        tweet.addToListCategories(result, indexTermOrig);
                    }

                } else if (HLoader.getMapH7().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH7().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH8().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH8().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH8().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH8().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

                if (HLoader.getMapH9().keySet().contains(nGramLowerCase)) {
                    heuristic = HLoader.getMapH9().get(nGramLowerCase);
                    result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                } else if (HLoader.getMapH9().keySet().contains(nGramLowerCaseStripped)) {
                    heuristic = HLoader.getMapH9().get(nGramLowerCaseStripped);
                    result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                    if (result != null) {
                        // System.out.println("result: " + result);

                        tweet.addToListCategories(result, indexTermOrig);
                    }
                }

            }
            tweet = sentenceHeuristicsPost.applyRules(tweet, status);

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

        tweet = seh.applyRules(tweet, status);
        if (tweet.getListCategories().contains("001") | tweet.getListCategories().contains("002")) {
            setTweetsClassified.add(tweet);
            return tweet;
        }

        tweet.setListCategories(null);
//            System.out.println("curr tweet: " + tweet.toString());
        status = statusCleaner.clean(status);
        statusStripped = statusCleaner.removePunctuationSigns(status);

        tweet = sentenceHeuristicsPre.applyRules(tweet, status);
        tweet = sentenceHeuristicsPre.applyRules(tweet, statusStripped);

        tweet = hashtagHeuristics.applyRules(tweet);

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

            if (HLoader.getMapH1().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH1().get(nGramLowerCase);
                result = (termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig));
                if (result != null) {
                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH1().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("index: " + indexTermOrig);
//                    System.out.println("nGramOrig: " + nGramOrig);
//                    System.out.println("nGramStripped: " + nGramStripped);
//                    System.out.println("nGramLowerCaseStripped: " + nGramLowerCaseStripped);
//                    System.out.println("status: " + status);
//                    System.out.println("statusStripped: " + statusStripped);
                heuristic = HLoader.getMapH1().get(nGramLowerCaseStripped);
                result = (termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped));
                if (result != null) {
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH2().keySet().contains(nGramLowerCase)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGram: " + nGramOrig);
                heuristic = HLoader.getMapH2().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }

            } else if (HLoader.getMapH2().keySet().contains(nGramLowerCaseStripped)) {
//                    System.out.println("negative detected!");
//                    System.out.println("nGramStripped: " + nGramStripped);
                heuristic = HLoader.getMapH2().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH3().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH3().get(nGramLowerCase);

                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH3().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH3().get(nGramLowerCaseStripped);

                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH4().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH4().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH4().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH4().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH5().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH5().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH5().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH5().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH6().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH6().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH6().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH6().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH7().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH7().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);
                    tweet.addToListCategories(result, indexTermOrig);
                }

            } else if (HLoader.getMapH7().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH7().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH8().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH8().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH8().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH8().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

            if (HLoader.getMapH9().keySet().contains(nGramLowerCase)) {
                heuristic = HLoader.getMapH9().get(nGramLowerCase);
                result = termLevelHeuristics.checkFeatures(heuristic,status, nGramOrig);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            } else if (HLoader.getMapH9().keySet().contains(nGramLowerCaseStripped)) {
                heuristic = HLoader.getMapH9().get(nGramLowerCaseStripped);
                result = termLevelHeuristics.checkFeatures(heuristic,statusStripped, nGramStripped);
                if (result != null) {
                    // System.out.println("result: " + result);

                    tweet.addToListCategories(result, indexTermOrig);
                }
            }

        }
        return tweet = sentenceHeuristicsPost.applyRules(tweet, status);

//            if (tweet.getSetCategories().contains("011") & !tweet.getUser().toLowerCase().contains("hp")& !status.toLowerCase().contains("rt @hp")&
//                !tweet.getSetCategories().contains("012") & !tweet.getSetCategoriesToString().contains("061")) {
//                System.out.println("positive tweet, not promoted: " + status);
//                System.out.println("categories: " + tweet.getSetCategoriesToString());
//            }
    }
}
