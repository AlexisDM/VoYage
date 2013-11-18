package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.gson.Gson;


@SuppressWarnings("serial")
public class ManageUsersServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes", "unused" })
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		resp.setContentType( "application/json" );
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		if (cmd != null) {
			if ("LoadUsers".equals(cmd)) {
			
				boolean isOk = false;
				
				Query q = new Query("user");
				
				PreparedQuery pq = datastore.prepare(q);
				String lastConnexionTime = "";
				double lastConnexionTimedb = 0;
				
				Map<String,String> oneuser = null;
				List<Map<String,String>> users = new ArrayList<Map<String,String>>();
				
				for(Entity result : pq.asIterable ()) {
					oneuser = new HashMap<String, String>();
					oneuser.put("id", KeyFactory.keyToString(result.getKey()));
					oneuser.put("login", result.getProperty("login").toString());
					oneuser.put("nom", result.getProperty("nom").toString());
					oneuser.put("prenom", result.getProperty("prenom").toString());
					oneuser.put("age", result.getProperty("age").toString());
					oneuser.put("creationAccount", result.getProperty("creationAccount").toString());
					oneuser.put("lastConnexionDate", result.getProperty("lastConnexionDate").toString());
					lastConnexionTime = result.getProperty("lastConnexionTime").toString();
					lastConnexionTimedb = Double.parseDouble(lastConnexionTime);
					lastConnexionTimedb = lastConnexionTimedb/6000;
					lastConnexionTimedb = Math.round( lastConnexionTimedb * 100.0 ) / 100.0;
					DecimalFormat df = new DecimalFormat("#");
					oneuser.put("lastConnexionTime", (df.format(lastConnexionTimedb)));
					users.add(oneuser);
					
					isOk = true;
				}

				PrintWriter out = resp.getWriter();
				if(isOk) {
					out.write(new Gson().toJson(users));
				} else {
					out.write("Failed");
				}
			}
			
			if ("EditUser".equals(cmd)) {
				
				String id = req.getParameter("id");
				
				boolean isOk = false;
				
				Key userid = KeyFactory.stringToKey(id);
				
				Map<String,String> oneuser = new HashMap<String, String>();
				
				if (id != null ) {
					Entity user = null;
					try {
						user = datastore.get(userid);
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String prenom = "";
					String nom = "";
					String age = "";
					String creationAccount = "";
					String lastConnexionDate = "";
					String lastConnexionTime = "";
					double lastConnexionTimedb = 0;

					
					oneuser.put("login", user.getProperty("login").toString());
					oneuser.put("prenom", user.getProperty("prenom").toString());
					oneuser.put("nom", user.getProperty("nom").toString());
					oneuser.put("age", user.getProperty("age").toString());
					oneuser.put("creationAccount", user.getProperty("creationAccount").toString());
					oneuser.put("lastConnexionDate", user.getProperty("lastConnexionDate").toString());
					lastConnexionTime = user.getProperty("lastConnexionTime").toString();
					lastConnexionTimedb = Double.parseDouble(lastConnexionTime);
					lastConnexionTimedb = lastConnexionTimedb/6000;
					lastConnexionTimedb = Math.round( lastConnexionTimedb * 100.0 ) / 100.0;
					DecimalFormat df = new DecimalFormat("#");
					oneuser.put("lastConnexionTime", (df.format(lastConnexionTimedb)));
					
					isOk = true;
					
				}
				
				PrintWriter out = resp.getWriter();
				if(isOk) {
					out.write(new Gson().toJson(oneuser));
				} else {
					out.write("Failed");
				}
				
				
			}
			
		}
	}
}
