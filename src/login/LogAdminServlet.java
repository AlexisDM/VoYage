package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import javax.servlet.http.*;

import model.User;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import dao.UserDao;


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
					
					boolean isLogged = false;
					
					HttpSession session = req.getSession();
					User user = null;
					
					try
					{
						user = UserDao.loginUser(login, password, "Y");

						session.setAttribute("login", login);
						session.setAttribute("lastConnexionDate", user.getLastConnectionDate());
						session.setAttribute("lastConnexionTime", user.getLastConnectionTime());
						session.setAttribute("nom", user.getNom());
						session.setAttribute("prenom", user.getPrenom());
						
						
						session.setAttribute("id", user.getId().getId());
						
						System.out.println(user.getId().getId());
					}
					catch(Exception e)
					{
					}
					finally
					{
						isLogged = true;
					}

					DecimalFormat df = new DecimalFormat("#");
					
					
					PrintWriter out = resp.getWriter();
					if(isLogged) {
						out.write(session.getAttribute("login")+";"+user.getNom()+";"+user.getPrenom()+";"+user.getLastConnectionDate().toString()+";"+(df.format(user.getLastConnectionTime())));
					} else {
						out.write("Failed");
					}
				}
			}
		}
	}
}
