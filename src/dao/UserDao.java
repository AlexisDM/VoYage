package dao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
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
	
	@SuppressWarnings("deprecation")
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
		
		User myuser = null;

		for(Entity result : pq.asIterable ()) {
			
			myuser = new User(result.getKey(), result.getProperty("email").toString(), 
					result.getProperty("login").toString(), result.getProperty("password").toString(), 
					result.getProperty("prenom").toString(), result.getProperty("nom").toString(), 
					result.getProperty("admin").toString(), Integer.parseInt(result.getProperty("age").toString()), 
					stringToDate(result.getProperty("creationAccount").toString()), stringToDate(result.getProperty("lastConnexionDate").toString()), 
					stringToDouble(result.getProperty("lastConnexionTime").toString()));
		}

		if (myuser.getId() != null ) {
			Entity user = null;
			try {
				user = datastore.get(myuser.getId());
			} catch (EntityNotFoundException e) {
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
	
	public static User loadSpecificUser(Key id)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity user = null;
		try {
			user = datastore.get(id);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new User(id, user.getProperty("email").toString(), user.getProperty("login").toString(), 
				user.getProperty("password").toString(), user.getProperty("prenom").toString(), user.getProperty("nom").toString(), 
				user.getProperty("admin").toString(), Integer.parseInt(user.getProperty("age").toString()), 
				stringToDate(user.getProperty("creationAccount").toString()), stringToDate(user.getProperty("lastConnexionDate").toString()),
				stringToDouble(user.getProperty("lastConnexionTime").toString()));
	}
	
	public static List<User> loadUsers()
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		List<User> users = new ArrayList<User>();
		
		Query q = new Query("user");

		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {
			
			User oneuser = new User(result.getKey(), result.getProperty("email").toString(),
					result.getProperty("login").toString(), result.getProperty("password").toString(), 
					result.getProperty("prenom").toString(), result.getProperty("nom").toString(),
					result.getProperty("admin").toString(), Integer.parseInt(result.getProperty("age").toString()), 
					stringToDate(result.getProperty("creationAccount").toString()), stringToDate(result.getProperty("lastConnexionDate").toString()),
					stringToDouble(result.getProperty("lastConnexionTime").toString()));
			
			users.add(oneuser);
			}
		
		return users;
	
	}
	
	public static void UpdateUser(User user)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity newuser = null;
		try {
			newuser = datastore.get(user.getId());

		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (!user.getNom().equals("")) {
				newuser.setProperty("nom", user.getNom());
			}

			if (!user.getPrenom().equals("")) {
				newuser.setProperty("prenom", user.getPrenom());
			}
			
			newuser.setProperty("age", String.valueOf(user.getAge()));
			
			if (!user.getEmail().equals("")) {
				newuser.setProperty("email",user.getEmail());
			}
			
			if (!user.getPassword().equals("")) {
				newuser.setProperty("password",user.getPassword());
			}
			datastore.put(newuser);
		}
		
		
	}
	
	public static void DeleteUser(User user)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		datastore.delete(user.getId());

		
	}
	
	private static Date stringToDate(String origine)
	{
		Date out = null;
		
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
		
		try {
			out = format.parse(origine);
			}
			catch(Exception e)
			{
			}
		
		return out;
	}
	
	private static double stringToDouble(String origine)
	{
		double out = 0;
		
		out = Double.parseDouble(origine);
		out = out/6000;
		out = Math.round( out * 100.0 ) / 100.0;
		
		return out;
	}
}
