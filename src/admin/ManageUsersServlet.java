package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.*;

import model.User;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

import dao.UserDao;

@SuppressWarnings("serial")
public class ManageUsersServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String cmd = req.getParameter("cmd");

		resp.setContentType("application/json");

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		if (cmd != null) {
			if ("LoadUsers".equals(cmd)) {

				boolean isOk = false;

	
				Map<String, String> oneuser = null;
				List<Map<String, String>> users = new ArrayList<Map<String, String>>();

				List<User> rep = null;
								
				try
				{
					rep =UserDao.loadUsers();
				}
				catch (Exception e)
				{
					
				}
				finally
				{
					for(User u : rep)
					{
						oneuser = new HashMap<String, String>();
						oneuser.put("id", KeyFactory.keyToString(u.getId()));
						oneuser.put("login", u.getLogin());
						oneuser.put("nom", u.getNom());
						oneuser.put("prenom", u.getPrenom());
						oneuser.put("age", String.valueOf(u.getAge()));
						oneuser.put("email", u.getEmail());
						oneuser.put("creationAccount", u.getCreationAccount().toString());
						oneuser.put("lastConnexionDate",u.getLastConnectionDate().toString());
						oneuser.put("lastConnexionTime", String.valueOf(u.getLastConnectionTime()));
						users.add(oneuser);
					}
					
					isOk=true;
				}


				PrintWriter out = resp.getWriter();
				if (isOk) {
					out.write(new Gson().toJson(users));
				} else {
					out.write("Failed");
				}
			}

			if ("EditUser".equals(cmd)) {

				String id = req.getParameter("id");

				boolean isOk = false;

				Key userid = KeyFactory.stringToKey(id);

				Map<String, String> oneuser = new HashMap<String, String>();

				if (id != null) {
					Entity user = null;
					try {
						user = datastore.get(userid);
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String lastConnexionTime = "";
					double lastConnexionTimedb = 0;

					oneuser.put("id", id);
					oneuser.put("login", user.getProperty("login").toString());
					oneuser.put("prenom", user.getProperty("prenom").toString());
					oneuser.put("nom", user.getProperty("nom").toString());
					oneuser.put("age", user.getProperty("age").toString());
					oneuser.put("email", user.getProperty("email").toString());
					oneuser.put("creationAccount",
							user.getProperty("creationAccount").toString());
					oneuser.put("lastConnexionDate",
							user.getProperty("lastConnexionDate").toString());
					lastConnexionTime = user.getProperty("lastConnexionTime")
							.toString();
					lastConnexionTimedb = Double.parseDouble(lastConnexionTime);
					lastConnexionTimedb = lastConnexionTimedb / 6000;
					lastConnexionTimedb = Math
							.round(lastConnexionTimedb * 100.0) / 100.0;
					DecimalFormat df = new DecimalFormat("#");
					oneuser.put("lastConnexionTime",
							(df.format(lastConnexionTimedb)));

					isOk = true;

				}

				PrintWriter out = resp.getWriter();
				if (isOk) {
					out.write(new Gson().toJson(oneuser));
				} else {
					out.write("Failed");
				}

			}

			if ("UpdateUser".equals(cmd)) {
				String id = req.getParameter("id");
				boolean isOk = false;

				KeyFactory.stringToKey(id);
				
				User user = new User(KeyFactory.stringToKey(id), req.getParameter("email"), new String(""), 
						req.getParameter("password"), req.getParameter("prenom"), 
						req.getParameter("nom"), new String(""), Integer.parseInt(req.getParameter("age")), 
						new Date(), new Date(), 0);

				try
				{
					UserDao.UpdateUser(user);
				}
				catch (Exception e)
				{
				}
				finally
				{
					isOk = true;
				}
				
					Map<String, String> oneuser = new HashMap<String, String>();
					
					HttpSession session = req.getSession();
					oneuser.put("login", session.getAttribute("login").toString());
					oneuser.put("nom", session.getAttribute("nom").toString());
					oneuser.put("prenom", session.getAttribute("prenom").toString());
					oneuser.put("lastConnexionDate", session.getAttribute("lastConnexionDate").toString());
					oneuser.put("lastConnexionTime", session.getAttribute("lastConnexionTime").toString());
					

					PrintWriter out = resp.getWriter();
					if (isOk) {
						out.write(new Gson().toJson(oneuser));
					} else {
						out.write("Failed");
					}

				

			}
			
			if ("DeleteUser".equals(cmd)) {
				String id = req.getParameter("id");
				boolean isOk = false;

				KeyFactory.stringToKey(id);
				
				User user = new User(KeyFactory.stringToKey(id), new String(""), new String(""), 
						new String(""), new String(""), 
						new String(""), new String(""), 0, 
						new Date(), new Date(), 0);

				try
				{
					UserDao.DeleteUser(user);
				}
				catch (Exception e)
				{
				}
				finally
				{
					isOk = true;
				}
				
					Map<String, String> oneuser = new HashMap<String, String>();
					
					HttpSession session = req.getSession();
					oneuser.put("login", session.getAttribute("login").toString());
					oneuser.put("nom", session.getAttribute("nom").toString());
					oneuser.put("prenom", session.getAttribute("prenom").toString());
					oneuser.put("lastConnexionDate", session.getAttribute("lastConnexionDate").toString());
					oneuser.put("lastConnexionTime", session.getAttribute("lastConnexionTime").toString());
					

					PrintWriter out = resp.getWriter();
					if (isOk) {
						out.write(new Gson().toJson(oneuser));
					} else {
						out.write("Failed");
					}

				

			}
		}
	}
}
