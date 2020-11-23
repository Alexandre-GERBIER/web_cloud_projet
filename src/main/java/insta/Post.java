package insta;

import com.google.appengine.api.datastore.*;

import java.util.Date;

public class Post {
    private Key postKey;
    private String userKey;
    private String image;
    private String description;
    private Date timestamp;

    public Post(Key postKey, String userKey, String image, String description, Date timestamp){
        this.postKey = postKey;
        this.userKey = userKey;
        this.image = image;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Post(Entity post){
        this.postKey = post.getKey();
        this.userKey = (String) post.getProperty("User").toString();
        this.image = (String) post.getProperty("image").toString();
        this.description = (String) post.getProperty("description").toString();
        this.timestamp = (Date) post.getProperty("timestamp");
    }

}
