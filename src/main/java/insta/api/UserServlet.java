package insta.api;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import insta.Misc;
import insta.User;
import insta.datastore.UserEntity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@WebServlet(
        name= "UserServlet",
        description= "manage users",
        urlPatterns = "/api/user"
)
public class UserServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String googleToken = req.getHeader("googleToken");
        String userName = req.getHeader("userName");

        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Entity userVerified = UserEntity.googleAuthentification(googleToken);

        response.setStatus(200);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        LinkedList<String> search = new LinkedList<>();
        if(userName.contains(" ")) {
            Collections.addAll(search, userName.split(" "));
        } else {
            search.add(userName);
        }

        List<Entity> usersEntity = new LinkedList();

        //for(String s1 : search) {
            Query q1 = new Query("User").setFilter(new FilterPredicate("firstName",FilterOperator.IN,search));
            PreparedQuery pq1 = datastore.prepare(q1);
            usersEntity.addAll(pq1.asList(FetchOptions.Builder.withDefaults()));

            Query q2 = new Query("User").setFilter(new FilterPredicate("lastName",FilterOperator.IN,search));
            PreparedQuery pq2 = datastore.prepare(q2);
            usersEntity.addAll(pq2.asList(FetchOptions.Builder.withDefaults()));
       // }

        List<User> user = new LinkedList<User>();

        for(Entity e : usersEntity){
            user.add(new User(e));
        }

        response.getWriter().print(new Gson().toJson(user));
    }
}
