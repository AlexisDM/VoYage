package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
						oneflight.put("departure", f.getDeparture().toString());
						oneflight.put("arrival", f.getArrival().toString());
						oneflight.put("seats", String.valueOf(f.getSeats()));
						oneflight.put("price", String.valueOf(f.getPrice()));
						oneflight.put("time",String.valueOf(f.getHours()));
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

			if ("EditFlight".equals(cmd)) {

				String id = req.getParameter("id");

				boolean isOk = false;

				Map<String, String> oneflight = new HashMap<String, String>();

				if (id != null) {
					Flight f = null;
					try {
						f = FlightDao.loadSpecificFlight(KeyFactory.stringToKey(id));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						
						SimpleDateFormat df1=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
						DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);

						Date arrivalDate = null;
						Date departureDate = null;
						String arrivalString = null;
						String departureString = null;
						
						try {
							arrivalDate = df1.parse(f.getArrival().toString());
							departureDate = df1.parse(f.getDeparture().toString());
							arrivalString = df2.format(arrivalDate);
							departureString = df2.format(departureDate);
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						oneflight.put("id", id);
						oneflight.put("from", f.getFrom());
						oneflight.put("to", f.getTo());
						oneflight.put("departure", departureString);
						oneflight.put("arrival", arrivalString);
						oneflight.put("seats", String.valueOf(f.getSeats()));
						oneflight.put("price", String.valueOf(f.getPrice()));
						oneflight.put("time",String.valueOf(f.getHours()));
						isOk = true;
						
					}


				}

				PrintWriter out = resp.getWriter();
				if (isOk) {
					out.write(new Gson().toJson(oneflight));
				} else {
					out.write("Failed");
				}

			}

			if ("UpdateFlight".equals(cmd)) {
				String id = req.getParameter("id");
				boolean isOk = false;
				
				SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				Date departureConverted = null;
				Date arrivalConverted = null;
				
				try {
					departureConverted = format.parse(req.getParameter("departure").toString());
					arrivalConverted = format.parse(req.getParameter("arrival").toString());
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int time = (int) (arrivalConverted.getTime()-departureConverted.getTime())/3600000;

				Flight flight = new Flight(KeyFactory.stringToKey(id), req.getParameter("from"), req.getParameter("to"), 
						Integer.parseInt(req.getParameter("price")), Integer.parseInt(req.getParameter("seats")), 
						arrivalConverted, departureConverted, 
						time);
				try
				{
					FlightDao.UpdateFlight(flight);
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
			
			if ("DeleteFlight".equals(cmd)) {
				String id = req.getParameter("id");
				boolean isOk = false;

				Flight flight = new Flight(KeyFactory.stringToKey(id), new String(""), new String(""), 0, 0, new Date(), new Date(), 0);
				
				
					FlightDao.DeleteFlight(flight);
				
					isOk = true;
				
				
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
			
			if ("CreateFlight".equals(cmd)) {
			
				String from = req.getParameter("from");
				String to = req.getParameter("to");
				String departure = req.getParameter("departure");
				String arrival = req.getParameter("arrival");
				String price = req.getParameter("price");
				String seats = req.getParameter("seats");
				
				SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				Date departureConverted = null;
				Date arrivalConverted = null;
				
				try {
					departureConverted = format.parse(departure);
					arrivalConverted = format.parse(arrival);
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				boolean isOk = false;
				
				if (from != null && to != null && departure != null && arrival != null && price != null && seats != null) {
					
					int time = (int) (arrivalConverted.getTime()-departureConverted.getTime())/3600000;
					
					try {
						
						Flight flightToAdd = new Flight(from, to, Integer.parseInt(price), Integer.parseInt(seats),arrivalConverted, departureConverted, time);
						FlightDao.addFlight(flightToAdd);
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
				
			}
		}
	}
}
