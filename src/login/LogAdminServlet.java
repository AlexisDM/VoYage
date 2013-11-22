package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import javax.servlet.http.*;

import model.User;

import com.google.appengine.api.datastore.KeyFactory;
import dao.UserDao;


@SuppressWarnings("serial")
public class LogAdminServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
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
						session.setAttribute("creationAccount", user.getCreationAccount());
						session.setAttribute("nom", user.getNom());
						session.setAttribute("prenom", user.getPrenom());
						session.setAttribute("email", user.getEmail());
						session.setAttribute("age", user.getAge());
						session.setAttribute("admin", user.getAdmin());
						session.setAttribute("password", user.getPassword());
						session.setAttribute("id", KeyFactory.keyToString(user.getId()));
						
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
