package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.*;

import model.User;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import dao.UserDao;


@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	
	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("Disconnect".equals(cmd)) {
				
				HttpSession session = req.getSession();
				
				boolean isOk = false;
				
				
				SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
				Date convertedDateCreation = null;	
				Date convertedDateConnexion = null;	
				try {
				convertedDateCreation = format.parse(session.getAttribute("creationAccount").toString());
				convertedDateConnexion = format.parse(session.getAttribute("lastConnexionDate").toString());
				}
				catch(Exception e)
				{
				}
				
				User user = new User(KeyFactory.stringToKey(session.getAttribute("id").toString()), 
						session.getAttribute("email").toString(), session.getAttribute("login").toString(), 
						session.getAttribute("password").toString(), session.getAttribute("prenom").toString(), 
						session.getAttribute("nom").toString(), session.getAttribute("admin").toString(), 
						Integer.parseInt( session.getAttribute("age").toString()), 
						convertedDateCreation, convertedDateConnexion, 
						Double.parseDouble(session.getAttribute("lastConnexionTime").toString()));
				
				try
				{
					UserDao.logoutUser(user);
				}
				catch(Exception e)
				{
					
				}
				finally
				{
					isOk = true;
				}
	
				PrintWriter out = resp.getWriter();
				if(isOk) {
					out.write("Success");
				} else {
					out.write("Failed");
				}
			
			}
			
			if ("ManageUsers".equals(cmd)) {
				
				HttpSession session = req.getSession();

				PrintWriter out = resp.getWriter();

				out.write(session.getAttribute("login")+";"+session.getAttribute("nom")+";"+session.getAttribute("prenom")+";"+session.getAttribute("lastConnexionDate")+";"+session.getAttribute("lastConnexionTime"));

			
			}
		}
	}
}
