package insta.api;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import insta.Post;
import insta.Profile;
import insta.User;
import insta.datastore.UserEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet(
        name= "ProfileAPI",
        description = "ProfileAPI: get a user profile",
        urlPatterns = "/api/profile"
)
public class ProfileServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String googleToken = req.getHeader("googleToken");
        String profileToGet = req.getHeader("profileToGet");

        Entity userIdentityVerified = UserEntity.googleAuthentification(googleToken);

        if (userIdentityVerified == null ){
            response.setStatus(401);
        } else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

             try {
                 Key profileToGetKey = UserEntity.getKey(profileToGet);
                 User user = new User(datastore.get(profileToGetKey));

                 Query userPost = new Query("Post").setFilter(new Query.FilterPredicate("User", Query.FilterOperator.EQUAL,profileToGetKey ));
                 PreparedQuery prepareUserPost = datastore.prepare(userPost);

                 List<Entity> tmpPosts =prepareUserPost.asList(FetchOptions.Builder.withDefaults());
                 LinkedList<Post> posts = new LinkedList<Post>();

                 for( Entity e : tmpPosts){
                     posts.add( new Post(e));
                 }

                 Profile profile = new Profile(user, posts );

                 response.getWriter().print(new Gson().toJson(profile));


            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                response.setStatus(500);
            }


        }

    }
}
