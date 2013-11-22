package dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import model.User;

public class UserDao {
	public static void addUser(User user) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity entityUser = new Entity("user");
		
		entityUser.setProperty("nom", user.getNom());
		entityUser.setProperty("prenom", user.getPrenom());
		entityUser.setProperty("age", user.getAge());
		entityUser.setProperty("email", user.getEmail());
		entityUser.setProperty("login", user.getLogin());
		entityUser.setProperty("password", user.getPassword());
		entityUser.setProperty("creationAccount", user.getCreationAccount());
		entityUser.setProperty("lastConnexionDate", user.getLastConnectionDate());
		entityUser.setProperty("lastConnexionTime", user.getLastConnectionTime());
		entityUser.setProperty("admin", user.getAdmin());
		
		datastore.put(entityUser);
	}
	
	public static User loginUser(String reqLogin, String reqPassword, String reqAdmin) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("user");
		q.addFilter("login", Query.FilterOperator.EQUAL, reqLogin);
		q.addFilter("password", Query.FilterOperator.EQUAL, reqPassword);
		if (reqAdmin.equals("Y"))
		{
			q.addFilter("admin", Query.FilterOperator.EQUAL, reqAdmin);	
		}
		
		PreparedQuery pq = datastore.prepare(q);
		
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
		Date convertedDateCreation = null;	
		Date convertedDateConnexion = null;	
		
		User myuser = null;

		for(Entity result : pq.asIterable ()) {
			
			String lastConnexionTime = "";
			double lastConnexionTimedb = 0;
			lastConnexionTime = result.getProperty("lastConnexionTime").toString();
			lastConnexionTimedb = Double.parseDouble(lastConnexionTime);
			lastConnexionTimedb = lastConnexionTimedb/6000;
			lastConnexionTimedb = Math.round( lastConnexionTimedb * 100.0 ) / 100.0;
			
			try {
			convertedDateCreation = format.parse(result.getProperty("creationAccount").toString());
			convertedDateConnexion = format.parse(result.getProperty("lastConnexionDate").toString());
			}
			catch(Exception e)
			{
			}
			myuser = new User(result.getKey(), result.getProperty("email").toString(), 
					result.getProperty("login").toString(), result.getProperty("password").toString(), 
					result.getProperty("prenom").toString(), result.getProperty("nom").toString(), 
					result.getProperty("admin").toString(), Integer.parseInt(result.getProperty("age").toString()), 
					convertedDateCreation, convertedDateConnexion, 
					lastConnexionTimedb);
		}

		if (myuser.getId() != null ) {
			Entity user = null;
			try {
				user = datastore.get(myuser.getId());
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setProperty("lastConnexionDate", new Date());
			user.setProperty("lastConnexionTime", "0");
			datastore.put(user);
		}
		
		return myuser;
	}
	
	public static void logoutUser(User myUser)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
	    Date convertedDate;	
		
		if (myUser.getId() != null ) {
			Entity user = null;
			try {
				user = datastore.get(myUser.getId());
				
				String lastConnexionDate = user.getProperty("lastConnexionDate").toString();
				try {
					convertedDate = format.parse(lastConnexionDate);
					user.setProperty("lastConnexionTime", new Date().getTime() - convertedDate.getTime());
					datastore.put(user);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
