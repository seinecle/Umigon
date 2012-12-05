/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author C. Levallois
 */
public class NetworkExtractor {

    Set<Tweet> setTweets;

    public NetworkExtractor(Set<Tweet> setTweets) {
        this.setTweets = setTweets;
    }

    public NonComparablePair<Set> getNetworkOfMentions(int minimumTimesMentioned, int minimumTimesASourceShouldBeMentioned) {
        Iterator<Tweet> setTweetsIterator;
        Tweet tweet;
        Set<String> setUserMentions;
        Set<String> setUserName;
        Map<Integer, NonComparablePair> mapCooc;
        mapCooc = new TreeMap();
        Integer count = 0;
        String string;


        //Stores in a multiset the times each name is mentioned
        Multiset<String> multisetMentions = HashMultiset.create();
        setTweetsIterator = setTweets.iterator();
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            multisetMentions.addAll(tweet.getUser_mentions());
        }




        setTweetsIterator = setTweets.iterator();
        Iterator<String> setIterator;
        while (setTweetsIterator.hasNext()) {
            tweet = setTweetsIterator.next();
            setUserMentions = new TreeSet();
            setUserName = new TreeSet();
            
            //a tweet should mention a least a name to be included in the network
            if (tweet.getUser_mentions().isEmpty()) {
                continue;
            }
            
            //the author of a tweet should be mentioned at least n times by other tweets for its tweet to included
            if (multisetMentions.count(tweet.getUsername())<minimumTimesASourceShouldBeMentioned) {
                continue;
            }

            setIterator = tweet.getUser_mentions().iterator();
            while (setIterator.hasNext()) {
                string = setIterator.next();
                //a person mentioned in one tweet should appear at least a number of times to be included in the final network
                if (multisetMentions.count(string) >= minimumTimesMentioned) {
                    setUserMentions.add(string);
                }
            }
            setUserName.add(tweet.getUsername());
            count++;
            mapCooc.put(count, new NonComparablePair(setUserName, setUserMentions));
        }

        CooccurrencesAnalyzer analyzer = new CooccurrencesAnalyzer();
        analyzer.setInputDirected(mapCooc);


        Clock extractingGephiNodes = new Clock("extracting nodes");
        Set<Node> setNodes= analyzer.returnSetNodes();
        extractingGephiNodes.closeAndPrintClock();


        Clock extractingGephiEdges = new Clock("extracting edges");
        Set<Edge> setEdges = analyzer.returnSetEdgesDirected();
        extractingGephiEdges.closeAndPrintClock();

        NonComparablePair<Set> nodesAndEdges = new NonComparablePair(setNodes, setEdges);

        return nodesAndEdges;

    }
}
