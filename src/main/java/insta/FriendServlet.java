package foo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

@WebServlet(name = "FriendServlet", urlPatterns = { "/friends" })
public class FriendServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		Random r = new Random();

		// Create users
		for (int i = 0; i < 500; i++) {
			Entity e = new Entity("Friend", "f" + i);
			e.setProperty("firstName", "first" + i);
			e.setProperty("lastName", "last" + i);
			e.setProperty("age",r.nextInt(100) + 1);

			// Create user friends
			HashSet<String> fset = new HashSet<String>();
			while (fset.size() < 200) {
				fset.add("f" + r.nextInt(500));
			}
			e.setProperty("friends", fset);

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(e);

			response.getWriter().print("<li> created friend:" + e.getKey() + "<br>" + fset + "<br>");

		}
	}
}