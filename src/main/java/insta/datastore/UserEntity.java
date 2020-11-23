package insta.datastore;

import com.google.appengine.api.datastore.*;
import insta.User;

import java.util.Date;

public class UserEntity  {

    public static Key createUser(String firstName, String lastName, String email, String bio, String avatarUrl, String token ) {
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Entity entity = new Entity("User", email);
        entity.setProperty("firstName", firstName);
        entity.setProperty("lastName", lastName);
        entity.setProperty("avatarUrl", avatarUrl);
        entity.setProperty("email", email);
        entity.setProperty("bio", bio);
        entity.setProperty("googleToken", token);

        entity.setProperty("lastTimelineRetrieval", new Date());

        return (datastore.put(entity));
    }

    /**
     * create a following association between user a and b, user a follows user  b on tiny Insta
     * @param followingUser user who wants to follow b
     * @param followedUser user being followed by a
     */
    public static void follow(Key followingUser, Key followedUser) throws EntityNotFoundException {
        //retrieve the datastore and the entities corresponding to the users
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Entity follow = new Entity("Follow",followingUser.toString() + followedUser.toString());
        follow.setProperty("follower", followingUser);
        follow.setProperty("following", followedUser);

        datastore.put(follow);
    }

    /**
     * remove a following association between user a and b, user a follows user  b on tiny Insta
     * @param follower user who wants to unfollow b
     * @param following user being followed by a
     */
    public static void unFollow(Key follower, Key following) throws EntityNotFoundException {
        //retrieve the datastore and the entities corresponding to the users
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();
        Key followLink = KeyFactory.createKey("Post", follower.toString() + following.toString());

        datastore.delete(followLink);
    }

    public static Date getLastTimelineretrival(Key userKey) throws EntityNotFoundException {
        //retrieve the datastore and the entity corresponding to the user
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();
        Entity user = datastore.get(userKey);

        return (Date) user.getProperty("lastTimelineRetrieval");
    }

    public static void updateLastTimelineRetrieval(Key userKey) throws EntityNotFoundException {
        //retrieve the datastore and the entity corresponding to the user
        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();
        Entity user = datastore.get(userKey);

        Entity newUser = new Entity("User", userKey.toString());
        newUser.setProperty("name", user.getProperty("name"));
        newUser.setProperty("avatarUrl", user.getProperty("avatarUrl"));
        newUser.setProperty("email", user.getProperty("email"));
        newUser.setProperty("bio", user.getProperty("bio"));
        newUser.setProperty("googleToken", user.getProperty("googleToken"));
        newUser.setProperty("lastTimelineRetrieval", new Date());

        datastore.put(newUser);

    }

    public static int getFollowers(Key user){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query existUser = new Query("Follow").setFilter(new Query.FilterPredicate("following",Query.FilterOperator.EQUAL, user));
        PreparedQuery prepareExistUser = datastore.prepare(existUser);
        return prepareExistUser.asList(FetchOptions.Builder.withDefaults()).size();
    }

    public static Key getKey(String email){
        return KeyFactory.createKey("User", email);
    }

    public static Entity googleAuthentification(String googleToken){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query existUser = new Query("User").setFilter(new Query.FilterPredicate("googleToken",Query.FilterOperator.EQUAL, googleToken));
        PreparedQuery prepareExistUser = datastore.prepare(existUser);
        Entity result = prepareExistUser.asSingleEntity();

        if ( result == null ){
            return null;
        } else {
            return result;
        }
    }
}
