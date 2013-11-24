package login;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

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
					User user = null;
					user = UserDao.loginUser(login, password, "N");
					
					if(user != null) {
						//Création d'une session stockant le login
						HttpSession session = req.getSession();
						session.setAttribute("login", user.getLogin());
						session.setAttribute("id", user.getId());
						
						if(user.getLastConnectionTime() == -1) {
							out.write("FirstConn");
						} else {
							out.write("Success");
						}
					} else {
						out.write("Failed");
					}
				} else {
					out.write("Failed");
				}
			} else {
				out.write("Failed");
			}
		} else if("PostChangePass".equals(cmd)) {
			String oldPassword = req.getParameter("oldPassword");
			String newPassword = req.getParameter("newPassword");
			
			User user = UserDao.loginUser(req.getSession().getAttribute("login").toString(), oldPassword, "N");
			
			if(user != null) {
				user.setPassword(newPassword);
				UserDao.UpdateUser(user);
				
				if(user.getPassword().equals(newPassword)) {
					out.write("success");
				} else {
					out.write("fail");
				}
			} else {
				out.write("fail");
			}
		}
	}
}
