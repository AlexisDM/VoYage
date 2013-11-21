package dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

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
}
