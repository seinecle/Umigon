/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author C. Levallois
 */
public class Tweet {

    private String id;
    private String username;
    private String date;
    private String time;
    private String status;
    private String lang;
    private String image;
    private String source;
    private String location;
    private String latitude;
    private String longitude;
    private Set<String> hashtags;
    private Set<String> urls;
    private Set<String> user_mentions;
    private String media;

    public Tweet(String entry) {

        String[] hashTagsArray;
        String[] urlsArray;
        String[] user_mentionsArray;
        hashtags = new HashSet();
        urls = new HashSet();
        user_mentions = new HashSet();
        String[] fields = entry.split("\t", -1);
        if (!fields[0].equals("")) {
            this.id = fields[0];
        }
        if (!fields[1].equals("")) {
            this.username = fields[1].trim();
        }
        if (!fields[2].equals("")) {
            this.date = fields[2].trim();
        }
        if (!fields[3].equals("")) {
            this.time = fields[3].trim();
        }
        if (!fields[4].equals("")) {
            this.status = fields[4].trim();
        }
        if (!fields[5].equals("")) {
            this.lang = fields[5].trim();
        }
        if (!fields[6].equals("")) {
            this.image = fields[6].trim();
        }
        if (!fields[7].equals("")) {
            this.source = fields[7].trim();
        }
        if (!fields[8].equals("")) {
            this.location = fields[8].trim();
        }
        if (!fields[9].equals("")) {
            this.latitude = fields[9].trim();
        }
        if (!fields[10].equals("")) {
            this.longitude = fields[10].trim();
        }

        if (!fields[11].equals("")) {
            hashTagsArray = fields[11].trim().split(" ");
            this.hashtags.addAll(Arrays.asList(hashTagsArray));
        }

        if (!fields[12].equals("")) {
            urlsArray = fields[12].trim().split(" ");
            this.urls.addAll(Arrays.asList(urlsArray));
        }
        if (!fields[13].equals("")) {
            user_mentionsArray = fields[13].trim().split(" ");
            this.user_mentions.addAll(Arrays.asList(user_mentionsArray));
        }
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getLang() {
        return lang;
    }

    public String getImage() {
        return image;
    }

    public String getSource() {
        return source;
    }

    public String getLocation() {
        return location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public Set<String> getUser_mentions() {
        return user_mentions;
    }

    public String getMedia() {
        return media;
    }
}
