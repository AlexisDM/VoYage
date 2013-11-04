package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;


@SuppressWarnings("serial")
public class LogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "unused" })
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("PostInfo".equals(cmd)) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				
				if (login != null && password != null) {
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					
					Query q = new Query("user");
					q.addFilter("login", Query.FilterOperator.EQUAL, login);
					q.addFilter("password", Query.FilterOperator.EQUAL, password);
					
					PreparedQuery pq = datastore.prepare(q);
					String prenom = "";
					String nom = "";
					String email = "";
					String age = "";
					
					boolean isLogged = false;
					for(Entity result : pq.asIterable ()) {
						isLogged = true;
						prenom = result.getProperty("prenom").toString();
						nom = result.getProperty("nom").toString();
						email = result.getProperty("email").toString();
						age = result.getProperty("age").toString();
					}
					
					PrintWriter out = resp.getWriter();
					if(isLogged) {
						out.write(login+";"+nom+";"+prenom+";"+email+";"+age);
					} else {
						out.write("Failed");
					}
				}
			}
		}
	}
}
