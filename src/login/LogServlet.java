package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


@SuppressWarnings("serial")
public class LogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	@SuppressWarnings({ "deprecation" })
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		PrintWriter out = resp.getWriter();
		
		if (cmd != null) {
			if ("PostLogin".equals(cmd)) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				
				if (login != null && !login.equals("") && password != null && !password.equals("")) {
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					
					Query q = new Query("user");
					q.addFilter("login", Query.FilterOperator.EQUAL, login);
					q.addFilter("password", Query.FilterOperator.EQUAL, password);
					
					PreparedQuery pq = datastore.prepare(q);
					String prenom = "";
					String nom = "";
					String email = "";
					String age = "";
					String accountCrea = "";
					String lastConn = "";
					Key id = null;
					
					boolean isLogged = false;
					for(Entity result : pq.asIterable ()) {
						isLogged = true;
						prenom = result.getProperty("prenom").toString();
						nom = result.getProperty("nom").toString();
						email = result.getProperty("email").toString();
						age = result.getProperty("age").toString();
						lastConn = result.getProperty("lastConnexionDate").toString();
						accountCrea = result.getProperty("creationAccount").toString();
						id = result.getKey();
					}
					
					//Création d'une session stockant le login
					HttpSession session = req.getSession();
					session.setAttribute("login", login);
					session.setAttribute("id", id);
					
					if(!accountCrea.equals("") && accountCrea.equals(lastConn)) {
						out.write("FirstConn");
					} else if(isLogged) {
						out.write(login+";"+nom+";"+prenom+";"+email+";"+age);
					} else {
						out.write("Failed");
					}
				} else {
					out.write("Failed");
				}
			} else if("PostChangePass".equals(cmd)) {
				String oldPassword = req.getParameter("oldPassword");
				String newPassword = req.getParameter("newPassword");
				
				//Récupération du login et de l'id
				String login = req.getSession().getAttribute("login").toString();
				Key id = null;
				Entity user = null;
				
				//Récupération de l'ancien mdp
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				
				Query q = new Query("user");
				q.addFilter("login", Query.FilterOperator.EQUAL, login);
				
				PreparedQuery pq = datastore.prepare(q);
				String password = "";
				
				for(Entity result : pq.asIterable ()) {
					password = result.getProperty("password").toString();
					id = result.getKey();
				}
				
				if (id != null ) {
					try {
						user = datastore.get(id);
					} catch (EntityNotFoundException e) {
						e.printStackTrace();
					}
				}
				
				//Changement du mdp et de la date de dernière connexion
				boolean changedPassword = false;
				if(password.equals(oldPassword)) {
					changedPassword = true;

					user.setProperty("password", newPassword);
					user.setProperty("lastConnexionDate", new Date());
					datastore.put(user);
				}
				
				if(changedPassword == true) {
					out.write("success");
				} else {
					out.write("fail");
				}
			}
		}
	}
}
