package flight;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.*;

import com.google.gson.Gson;

import model.Flight;
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
			} else if ("LoadFromCities".equals(cmd)) {
				//Renvoie les différentes villes possible pour un départ
				out.write(FlightDao.getCities("from"));
			} else if ("LoadToCities".equals(cmd)) {
				//Renvoie les différentes villes possible d'arrivée
				out.write(FlightDao.getCities("to"));
			} else if ("LoadFlights".equals(cmd)) {
				resp.setContentType("application/json");
				Flight flight = new Flight();
				
				flight.setFrom(req.getParameter("from"));
				flight.setTo(req.getParameter("to"));
				
				try {
					String date = req.getParameter("dateDep")+" "+req.getParameter("timeDep");
					
					SimpleDateFormat format = new SimpleDateFormat();
					flight.setDeparture(format.parse(date));
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				List<Flight> flights = FlightDao.loadSimilarFlights(flight, req.getSession().getAttribute("login").toString());
				
				out.write(new Gson().toJson(flights));
			}
		}
	}
}