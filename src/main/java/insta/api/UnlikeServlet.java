package insta.api;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import insta.Post;
import insta.User;
import insta.datastore.PostEntity;
import insta.datastore.UserEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (
        name = "unLikeAPI",
        description = "unLikeAPI: unlike a post",
        urlPatterns = "/api/unlike"
)
public class UnlikeServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userEmail = req.getHeader("email");
        String postId = req.getHeader("postId");

        String googleToken = req.getHeader("connectionToken");

        Entity userIdentityVerified = UserEntity.googleAuthentification(googleToken);

        if(userIdentityVerified != null){
            resp.setStatus(401);
        } else {

            Key userKey = userIdentityVerified.getKey();
            Key postKey = PostEntity.getKey(postId);

            try {
                PostEntity.unlike(userKey, postKey);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();

                resp.setStatus(500);
            }

            resp.setStatus(201);
        }
    }
}
