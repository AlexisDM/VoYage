package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
public class LogAdminServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	@SuppressWarnings({ "deprecation" })
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("PostInfo".equals(cmd)) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				if (login != null && password != null) {
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					
					Query q = new Query("user");
					q.addFilter("login", Query.FilterOperator.EQUAL, login);
					q.addFilter("password", Query.FilterOperator.EQUAL, password);
					q.addFilter("admin", Query.FilterOperator.EQUAL, "Y");
					
					PreparedQuery pq = datastore.prepare(q);
					String prenom = "";
					String nom = "";
					String lastConnexionDate = "";
					String lastConnexionTime = "";
					double lastConnexionTimedb = 0;
					Key id = null;

					
					boolean isLogged = false;
					for(Entity result : pq.asIterable ()) {
						isLogged = true;
						prenom = result.getProperty("prenom").toString();
						nom = result.getProperty("nom").toString();
						lastConnexionDate = result.getProperty("lastConnexionDate").toString();
						lastConnexionTime = result.getProperty("lastConnexionTime").toString();
						lastConnexionTimedb = Double.parseDouble(lastConnexionTime);
						lastConnexionTimedb = lastConnexionTimedb/6000;
						lastConnexionTimedb = Math.round( lastConnexionTimedb * 100.0 ) / 100.0;
						DecimalFormat df = new DecimalFormat("#");
						lastConnexionTime = (df.format(lastConnexionTimedb));
						id = result.getKey();

					}
					
					/*initilisation des variables sessions
					 *
					 */
					
					HttpSession session = req.getSession();
					session.setAttribute("login", login);
					session.setAttribute("lastConnexionDate", lastConnexionDate);
					session.setAttribute("lastConnexionTime", lastConnexionTime);
					session.setAttribute("id", id);
					
					
					/* Maj colone lastconenctiontime*/
					
					if (id != null ) {
						Entity user = null;
						try {
							user = datastore.get(id);
						} catch (EntityNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						user.setProperty("lastConnexionDate", new Date());
						datastore.put(user);
					}
					
					
					PrintWriter out = resp.getWriter();
					if(isLogged) {
						out.write(session.getAttribute("login")+";"+nom+";"+prenom+";"+lastConnexionDate+";"+lastConnexionTime);
					} else {
						out.write("Failed");
					}
				}
			}
		}
	}
}
