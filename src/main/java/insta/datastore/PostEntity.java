package insta.datastore;

import com.google.appengine.api.datastore.*;
import insta.Post;

import java.util.Date;

public class PostEntity {

    public static void createPost(Key userKey, String image, String description){
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Date ts = new Date();
        String id =  userKey.toString() + ts;
        Entity entity = new Entity("post", id);

        entity = new Entity("Post",id);
        entity.setProperty("User",userKey);
        entity.setProperty("description", description);
        entity.setProperty("timestamp", ts);
        entity.setProperty("image", image);

        datastore.put(entity);
    }

    /**
     * method to register a like for a post from a given user
     * @param userKey the datastore key of the user
     * @param postKey the datastore key of the post
     * @throws EntityNotFoundException
     */
    public static void like(Key userKey, Key postKey) throws EntityNotFoundException {
        //retrieve the datastore and the entity corresponding to the post
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Entity like = new Entity("like",postKey.toString() + userKey.toString());
        like.setProperty("Post", postKey);
        like.setProperty("User",userKey);

        datastore.put(like);
    }

    /**
     * method to unlike a post for a user
     * @param userKey the datastore key of the user who wants to unlike
     * @param postKey the datastore key of the post to unlike
     * @throws EntityNotFoundException
     */
    public static void unlike(Key userKey, Key postKey) throws EntityNotFoundException {
        //retrieve the datastore and the entity corresponding to the post
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Key likeKey = KeyFactory.createKey("like",userKey.toString() + postKey.toString() );

        datastore.delete(likeKey);
    }

    public static Key getKey(String postId){
        return KeyFactory.createKey("Post", postId);
    }

    /**
     * return the post corresponding to the key from the datastore
     * @param key the post's key
     * @return the post
     * @throws EntityNotFoundException
     */
    public static Post getPost(String key) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        return new Post(datastore.get(PostEntity.getKey(key)));
    }
}
