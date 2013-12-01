package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.KeyFactory;

import model.Global;
import model.User;
import dao.UserDao;


@SuppressWarnings("serial")
public class LogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		PrintWriter out = resp.getWriter();
		
		if (cmd != null) {
			if ("PostLogin".equals(cmd)) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				
				if (login != null && !login.equals("") && password != null && !password.equals("")) {
					User user = UserDao.loginUser(login, password, "N");
					
					if(user != null) {
						//Création d'une session stockant le login
						HttpSession session = req.getSession();
						
						session.setAttribute("login", login);
						session.setAttribute("password", password);
						session.setAttribute("lastConnexionDate", user.getLastConnectionDate());
						session.setAttribute("lastConnexionTime", user.getLastConnectionTime());
						session.setAttribute("creationAccount", user.getCreationAccount());
						session.setAttribute("nom", user.getNom());
						session.setAttribute("prenom", user.getPrenom());
						session.setAttribute("email", user.getEmail());
						session.setAttribute("age", user.getAge());
						session.setAttribute("admin", user.getAdmin());
						session.setAttribute("password", user.getPassword());
						session.setAttribute("id", KeyFactory.keyToString(user.getId()));
						
						if(user.getLastConnectionDate().equals(user.getCreationAccount())) {
							out.write(Global.firstConn);
						} else {
							out.write(Global.success);
						}
					} else {
						out.write(Global.fail);
					}
				} else {
					out.write(Global.fail);
				}
			} else if("PostChangePass".equals(cmd)) {
				String oldPassword = req.getParameter("oldPassword");
				String newPassword = req.getParameter("newPassword");
				
				User user = UserDao.loginUser(req.getSession().getAttribute("login").toString(), oldPassword, "N");
				
				if(user != null) {
					user.setPassword(newPassword);
					UserDao.UpdateUser(user);
					
					if(user.getPassword().equals(newPassword)) {
						req.getSession().setAttribute("password", newPassword);
						out.write(Global.success);
					} else {
						out.write(Global.fail);
					}
				} else {
					out.write(Global.fail);
				}
			} else if("PostLogOutUser".equals(cmd)) {
				HttpSession session = req.getSession();
				boolean isOk = false;
				
				SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
				Date convertedDateCreation = null;	
				Date convertedDateConnexion = null;	
				
				try {
					convertedDateCreation = format.parse(session.getAttribute("creationAccount").toString());
					convertedDateConnexion = format.parse(session.getAttribute("lastConnexionDate").toString());
					
					User user = new User(KeyFactory.stringToKey(session.getAttribute("id").toString()), 
							session.getAttribute("email").toString(), session.getAttribute("login").toString(), 
							session.getAttribute("password").toString(), session.getAttribute("prenom").toString(), 
							session.getAttribute("nom").toString(), session.getAttribute("admin").toString(), 
							Integer.parseInt( session.getAttribute("age").toString()), 
							convertedDateCreation, convertedDateConnexion, 
							Double.parseDouble(session.getAttribute("lastConnexionTime").toString()));
					
					UserDao.logoutUser(user);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					isOk = true;
				}
	
				if(isOk) {
					out.write(Global.success);
				} else {
					out.write(Global.fail);
				}
			}
		} else {
			out.write(Global.fail);
		}
	}
}
