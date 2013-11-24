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

import model.Flight;
import model.User;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

import dao.FlightDao;
import dao.UserDao;

@SuppressWarnings("serial")
public class ManageFlightsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String cmd = req.getParameter("cmd");

		resp.setContentType("application/json");

		if (cmd != null) {
			if ("LoadFlights".equals(cmd)) {

				boolean isOk = false;

	
				Map<String, String> oneflight = null;
				List<Map<String, String>> flights = new ArrayList<Map<String, String>>();

				List<Flight> rep = null;
								
					rep = FlightDao.loadFlights();
				
					for(Flight f : rep)
					{
						oneflight = new HashMap<String, String>();
						oneflight.put("id", KeyFactory.keyToString(f.getId()));
						oneflight.put("from", f.getFrom());
						oneflight.put("to", f.getTo());
						oneflight.put("dateDeparture", f.getDeparture().toString());
						oneflight.put("dateArrival", f.getArrival().toString());
						oneflight.put("seats", String.valueOf(f.getSeats()));
						oneflight.put("price", String.valueOf(f.getPrice()));
						oneflight.put("hours",String.valueOf(f.getHours()));
						flights.add(oneflight);
					}
					
					isOk=true;
				


				PrintWriter out = resp.getWriter();
				if (isOk) {
					out.write(new Gson().toJson(flights));
				} else {
					out.write("Failed");
				}
			}

			/*if ("EditUser".equals(cmd)) {

				String id = req.getParameter("id");

				boolean isOk = false;

				Map<String, String> oneuser = new HashMap<String, String>();

				if (id != null) {
					User user = null;
					try {
						user = UserDao.loadSpecificUser(KeyFactory.stringToKey(id));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						oneuser.put("id", id);
						oneuser.put("login", user.getLogin());
						oneuser.put("prenom", user.getPrenom());
						oneuser.put("nom", user.getNom());
						oneuser.put("age", String.valueOf(user.getAge()));
						oneuser.put("email", user.getEmail());
						oneuser.put("creationAccount",
								user.getCreationAccount().toString());
						oneuser.put("lastConnexionDate",
								user.getLastConnectionDate().toString());
						DecimalFormat df = new DecimalFormat("#");
						oneuser.put("lastConnexionTime",
								(df.format(user.getLastConnectionTime())));
						isOk = true;
						
					}


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
			
			if ("CreateUser".equals(cmd)) {
			
				String nom = req.getParameter("nom");
				String prenom = req.getParameter("prenom");
				String age = req.getParameter("age");
				String email = req.getParameter("email");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String admin = req.getParameter("admin");
				Date dateCreaAccount = new Date();
				
				boolean isOk = false;
				
				if (nom != null && prenom != null && password != null && email != null && login != null) {
					
					try {
						
						User userToAdd = new User(email, login, password, prenom, nom, admin, Integer.parseInt(age), dateCreaAccount, dateCreaAccount, -1);
						UserDao.addUser(userToAdd);
					}
					catch(Exception e){
						
					}
					finally{
						isOk = true;
					}
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
				
			}*/
		}
	}
}
