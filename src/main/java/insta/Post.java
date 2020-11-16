package insta;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;

public class Post {
    protected DatastoreService datastore;
    private String id;
    private Entity entity;

    public Post(User usr, String image, String desc){
        this.datastore  = DatastoreServiceFactory.getDatastoreService();
        this.id =  usr.getId() + new Timestamp(new Date().getTime());

        this.entity = new Entity("Post",id);
        this.entity.setProperty("User",usr.getId());
        this.entity.setProperty("description", desc);
        this.entity.setProperty("image", image);
        this.entity.setProperty("likers",new HashSet<String>());
        this.entity.setProperty("likeCount", 0);
    }

    public Post(User usr, String image){
        this(usr, image, null);
    }

    public static void like(User usr, Post p) throws EntityNotFoundException {
        p.like(usr);
    }

    //TODO premier jet, rendre thread safe
    public void like(User usr) throws EntityNotFoundException {
        Entity c = this.datastore.get(this.entity.getKey());
        HashSet<String> likers = (HashSet<String>) c.getProperty("likers");
        likers.add(usr.getId());
        c.setProperty("likers",likers);

        int like = (int) c.getProperty("likeCount");
        c.setProperty("likeCount", like +1);
    }

    public static void unlike(User usr, Post p){

    }

    //TODO premier jet, rendre thread safe
    public void unlike(User usr) throws EntityNotFoundException {
        Entity c = this.datastore.get(this.entity.getKey());
        HashSet<String> likers = (HashSet<String>) c.getProperty("likers");
        likers.remove(usr.getId());
        c.setProperty("likers",likers);

        int like = (int) c.getProperty("likeCount");
        c.setProperty("likeCount", like - 1);
    }

    public String getId(){
        return this.id;
    }

    public static String getPostId(Post p){
        return p.getId();
    }
}