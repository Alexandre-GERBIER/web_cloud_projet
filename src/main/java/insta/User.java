package insta;

import com.google.api.client.util.DateTime;
import com.google.appengine.api.datastore.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private String bio;
    private Key key;

    public User(Entity user){
        this.key = user.getKey();
        this.firstName = (String) user.getProperty("firstName");
        this.lastName = (String) user.getProperty("lastName");
        this.email = (String) user.getProperty("email");
        this.avatarUrl = (String) user.getProperty("avatarUrl");
        this.bio = (String) user.getProperty("bio");
    }





}

