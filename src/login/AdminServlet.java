package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;


@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "unused" })
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("Disconnect".equals(cmd)) {
				
				HttpSession session = req.getSession();
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				
				boolean isOk = false;
				
				
				Key id = (Key) session.getAttribute("id");
				

				if (id != null ) {
					Entity user = null;
					try {
						user = datastore.get(id);
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String lastConnexionDate = user.getProperty("lastConnexionDate").toString();
					
					datastore.put(user);
					isOk = true;
				}

				
				
				PrintWriter out = resp.getWriter();
				if(isOk) {
					out.write("Success");
				} else {
					out.write("Failed");
				}
			
			}
		}
	}
}
