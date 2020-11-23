package insta.api;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.repackaged.com.google.gson.Gson;
import insta.Post;
import insta.User;
import insta.datastore.PostEntity;
import insta.datastore.UserEntity;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@WebServlet(
        name= "PostAPI",
        description= "PostAPI: Post a post",
        urlPatterns = "/api/post"
)
@MultipartConfig
public class PostServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String description = req.getHeader("description");
        String image = req.getHeader("image");
        String googleToken = req.getHeader("googleToken");

        //To use with blob to store actual image, but don't work
        //String imageUrl = getUploadedFileUrl(req,"image");

        Entity userIdentityVerified = UserEntity.googleAuthentification(googleToken);

        if(userIdentityVerified == null){
            resp.setStatus(401);
        } else {
            Key userKey = userIdentityVerified.getKey();
            PostEntity.createPost(userKey, image, description);
            resp.setStatus(201);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String googleToken = req.getHeader("googleToken");
        String posts = req.getHeader("posts");

        String[] postsList = posts.split("/");
        Entity userIdentityVerified = UserEntity.googleAuthentification(googleToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LinkedList<Post> postsInfo = new LinkedList<Post>();

        if(userIdentityVerified == null){
            response.setStatus(401);
        } else {
            for (String post : postsList){
                try {
                    postsInfo.add(PostEntity.getPost(post));
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        response.getWriter().print(new Gson().toJson(postsInfo));

    }

    private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName){
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get(formInputElementName);

        // User submitted form without selecting a file, so we can't get a URL. (devserver)
        if(blobKeys == null || blobKeys.isEmpty()) {
            return "erreur blob not saved";
        }

        // Our form only contains a single file input, so get the first index.
        BlobKey blobKey = blobKeys.get(0);

        // User submitted form without selecting a file, so we can't get a URL. (live server)
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return null;
        }

        // We could check the validity of the file here, e.g. to make sure it's an image file
        // https://stackoverflow.com/q/10779564/873165

        // Use ImagesService to get a URL that points to the uploaded file.
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
        return imagesService.getServingUrl(options);
    }
}
