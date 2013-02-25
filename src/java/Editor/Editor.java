/*
 * To change this template, choose Tools | Templates
 * and open the template in the Editor.
 */
package Editor;

import Twitter.ControllerBean;
import Twitter.Tweet;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C. Levallois
 */
@ManagedBean
@ViewScoped
public class Editor {

    @ManagedProperty("#{controllerBean}")
    private ControllerBean controllerBean;

    public void setControllerBean(ControllerBean controllerBean) {
        this.controllerBean = controllerBean;
    }
    private Set<Tweet> setTweets;
    private List<Tweet> listTweetsDisplayed;
    private Integer optionChosen;
    private Query<Tweet> updateQuery;
    private UpdateOperations<Tweet> ops;
    private Tweet currTweetDisplayed;

    @PostConstruct
    public void init() {
        try {
            System.out.println("new Editor!");
            setTweets = new HashSet();
            listTweetsDisplayed = new ArrayList();

            //RETRIEVE MATCHES FROM THIS UUID
//            setClosematchesOriginal.addAll(controllerBean.ds.find(CloseMatchBean.class).field("uuid").equal(controllerBean.uuid.toString()).asList());
            setTweets.addAll(controllerBean.getSetTweets());

            System.out.println("number of tweets cases: " + setTweets.size());


            Iterator<Tweet> setTweetsIterator = setTweets.iterator();
            if (setTweetsIterator.hasNext()) {
                listTweetsDisplayed.add(setTweetsIterator.next());
                setTweetsIterator.remove();
            }

        } catch (NullPointerException e) {
            System.err.println("There was no more tweet");
            System.err.println("NullPointerException: " + e.getMessage());

        }

    }

    public String next() {

        if (optionChosen == null) {
            return null;
        }
        System.out.println("curr button selected is: " + optionChosen);
        save(listTweetsDisplayed.get(0));
        optionChosen = null;
        listTweetsDisplayed.clear();

        Iterator<Tweet> setTweetsIterator = setTweets.iterator();
        if (setTweetsIterator.hasNext()) {

            listTweetsDisplayed.add(setTweetsIterator.next());
            setTweetsIterator.remove();
            System.out.println("still this number of closeMatch to treat in the set:" + setTweets.size());

            return null;

        } else {
            System.out.println("no more tweets, moving on");
            return "finalcheck?faces-redirect=true";
        }
    }

    public Integer getOptionChosen() {
        return optionChosen;
    }

    public void setOptionChosen(Integer optionChosen) {
        this.optionChosen = optionChosen;
    }

    public Tweet getTweet() {
        return listTweetsDisplayed.get(0);
    }

    public void save(Tweet tweet) {
        Set<String> updatedSetCategories = tweet.getSetCategories();
        updatedSetCategories.add(String.valueOf(optionChosen));
        updateQuery = controllerBean.dsLocal.createQuery(Tweet.class).field("text").equal(tweet.getText());
        ops = controllerBean.dsLocal.createUpdateOperations(Tweet.class).set("setCategories", updatedSetCategories);
        controllerBean.dsLocal.update(updateQuery, ops, true);
        System.out.println("tweet updated with new categories!");
    }

    public Tweet getCurrTweetDisplayed() {
        return listTweetsDisplayed.get(0);
    }

    public void setCurrTweetDisplayed(Tweet currTweetDisplayed) {
        this.currTweetDisplayed = currTweetDisplayed;
    }
}