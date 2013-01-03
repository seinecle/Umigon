package Twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Classifier.Categories;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bson.types.ObjectId;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 *
 * @author C. Levallois
 */
@Entity
public class Tweet {

    @Id
    private ObjectId id;
    private String text;
    private String user;
    private String language;
    private long retweetCount;
    private List<String> hashtags;
    private List<String> mentions;
    private Set<String> setCategories;

    public Tweet() {
        setCategories = new TreeSet();

    }

    public Tweet(Status status) {
        this.text = status.getText();
        this.user = status.getUser().getScreenName();
        this.language = status.getUser().getLang();
        this.retweetCount = status.getRetweetCount();
        this.hashtags = new ArrayList();
        for (HashtagEntity h : status.getHashtagEntities()) {
            this.hashtags.add(h.getText());
        }
        this.mentions = new ArrayList();
        for (UserMentionEntity u : status.getUserMentionEntities()) {
            this.mentions.add(u.getScreenName());
        }
        setCategories = new TreeSet();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public Set<String> getSetCategories() {
        if (setCategories == null) {
            setCategories = new HashSet();
        }

        return setCategories;
    }

    public String getSetCategoriesToString() {
        if (setCategories == null) {
            setCategories = new HashSet();
        }
        if (setCategories.isEmpty()) {
            return "NO CATEGORY";
        }
        Iterator<String> setCategoriesIterator = setCategories.iterator();
        StringBuilder sb = new StringBuilder();
        String cat;
        while (setCategoriesIterator.hasNext()) {
            cat = setCategoriesIterator.next();
//            System.out.println("cat: " + cat);
            sb.append(Categories.get(cat));
            sb.append(" -- ");
        }

        return sb.toString();
    }

    public void setSetCategories(Set<String> setCategories) {
        this.setCategories = setCategories;
    }

    public boolean addToSetCategories(String category) {
        if (category == null) {
            return true;
        }
        if (setCategories == null) {
            setCategories = new HashSet();
        }

        return this.setCategories.add(category);
    }

    @Override
    public String toString() {
        return "Tweet{" + "text=" + text + ", user=" + user + ", hashtags=" + hashtags + ", mentions=" + mentions + ", setCategories=" + getSetCategoriesToString() + '}';
    }
}
