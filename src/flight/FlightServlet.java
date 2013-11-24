package flight;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import dao.FlightDao;

@SuppressWarnings("serial")
public class FlightServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		PrintWriter out = resp.getWriter();
		
		if (cmd != null) {
			if ("PostHeaderInfo".equals(cmd)) {
				//renvoie le login du user et le nombre d'utilisateurs connectés
				out.write(req.getSession().getAttribute("login")+";"+FlightDao.numUsersConnected());
			}
		}
	}
}