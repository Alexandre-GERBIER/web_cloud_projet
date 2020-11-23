package insta.api;

import com.google.api.client.util.DateTime;
import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import endpoints.repackaged.com.google.api.Http;
import insta.Misc;
import insta.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.google.appengine.api.datastore.Query.*;
import insta.datastore.UserEntity;

@WebServlet(
        name= "timeline",
        description= "retrieve the timeline of the user",
        urlPatterns = "/api/timeline"
)
public class TimelineServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String googleToken = req.getHeader("googleToken");
        String all = req.getHeader("all");

        DatastoreService datastore  = DatastoreServiceFactory.getDatastoreService();

        Entity userIdentityVerified = UserEntity.googleAuthentification(googleToken);

        if(userIdentityVerified == null){
            response.setStatus(401);
        } else {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                Key userKey = userIdentityVerified.getKey();
                Date oldTs;

                if(all.equals("false") || all == null) {
                    oldTs = UserEntity.getLastTimelineretrival(userKey);
                    UserEntity.updateLastTimelineRetrieval(userKey);
                } else {
                    oldTs = new Date(0);
                    System.out.println("timestamp" + oldTs);
                }

                Query queryFollowed = new Query("Follow")
                        .setFilter(new FilterPredicate("follower",Query.FilterOperator.EQUAL, userKey));
                PreparedQuery preparedFollowedQuery = datastore.prepare(queryFollowed);
                List<Entity> followedUsers = preparedFollowedQuery.asList(FetchOptions.Builder.withDefaults());

                List<Entity> result = new ArrayList<Entity>();

                for(Entity usr : followedUsers) {
                    Query queryPost = new Query("Post").
                            setFilter(CompositeFilterOperator.and(
                                    new FilterPredicate("timestamp", Query.FilterOperator.GREATER_THAN_OR_EQUAL, oldTs),
                                    new FilterPredicate("User", FilterOperator.EQUAL, usr.getKey())));
                    PreparedQuery preparedQuery = datastore.prepare(queryPost);
                    result.addAll(preparedQuery.asList(FetchOptions.Builder.withDefaults()));
                }

                result = Misc.parseQuery(result);

                response.getWriter().print(new Gson().toJson(result));

            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                response.setStatus(500);
            }

            response.setStatus(202);
        }

    }
}
