package insta.api;

import com.google.appengine.api.datastore.EntityNotFoundException;
import insta.Misc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name= "fill database",
        description= "create users",
        urlPatterns = "/api/fill"
)
public class FillDatabaseServlet  extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            Misc.fillDatabase();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        response.setStatus(201);
    }
}
