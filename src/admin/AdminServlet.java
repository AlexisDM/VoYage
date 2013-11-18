package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;


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
					
					DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
					SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
				    Date convertedDate;			    
				    try {
						convertedDate = format.parse(lastConnexionDate);
						user.setProperty("lastConnexionTime", new Date().getTime() - convertedDate.getTime());
						datastore.put(user);
						isOk = true;
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
