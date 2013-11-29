package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import model.Flight;

public class FlightDao {
	public static void addFlight(Flight flight) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity entityFlight = new Entity("flight");
		
		entityFlight.setProperty("departure", flight.getDeparture());
		entityFlight.setProperty("arrival", flight.getArrival());
		entityFlight.setProperty("from", flight.getFrom());
		entityFlight.setProperty("to", flight.getTo());
		entityFlight.setProperty("price", flight.getPrice());
		entityFlight.setProperty("seats", flight.getSeats());
		entityFlight.setProperty("time", flight.getHours());
		
		datastore.put(entityFlight);
	}
	
	
	public static Flight loadSpecificFlight(Key id)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity flight = null;
		try {
			flight = datastore.get(id);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Flight(id, flight.getProperty("from").toString(), flight.getProperty("to").toString(), 
				Integer.parseInt(flight.getProperty("price").toString()), Integer.parseInt(flight.getProperty("seats").toString()), 
				stringToDate(flight.getProperty("arrival").toString()), stringToDate(flight.getProperty("departure").toString()), 
				Integer.parseInt(flight.getProperty("time").toString()));
	}
	
	public static List<Flight> loadFlights()
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		List<Flight> flights = new ArrayList<Flight>();
		
		Query q = new Query("flight");

		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {
			
			Flight oneflight = new Flight(result.getKey(), result.getProperty("from").toString(), result.getProperty("to").toString(), 
					Integer.parseInt(result.getProperty("price").toString()), Integer.parseInt(result.getProperty("seats").toString()), 
					stringToDate(result.getProperty("arrival").toString()), stringToDate(result.getProperty("departure").toString()), 
					Integer.parseInt(result.getProperty("time").toString()));
			
			flights.add(oneflight);
			
			}
		
		return flights;
	
	}
	
	public static void UpdateFlight(Flight flight)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity newflight = null;
		try {
			newflight = datastore.get(flight.getId());

		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (!flight.getDeparture().equals("")) {
				newflight.setProperty("departure", flight.getDeparture());
			}
			
			if (!flight.getArrival().equals("")) {
				newflight.setProperty("arrival", flight.getArrival());
			}
			
			if (!flight.getFrom().equals("")) {
				newflight.setProperty("from", flight.getFrom());
			}
			
			if (!flight.getTo().equals("")) {
				newflight.setProperty("to", flight.getTo());
			}
			
			newflight.setProperty("price", flight.getPrice());
			
			newflight.setProperty("seats", flight.getSeats());
			
			newflight.setProperty("time", flight.getHours());
			
			datastore.put(newflight);
		}
		
		
	}
	
	public static void DeleteFlight(Flight flight)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		datastore.delete(flight.getId());

		
	}
	
	public static Date stringToDate(String origine)
	{
		Date out = null;
		
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy",Locale.US);
		
		try {
			out = format.parse(origine);
			}
			catch(Exception e)
			{
			}
		
		return out;
	}
	
	public static double stringToDouble(String origine)
	{
		double out = 0;
		
		out = Double.parseDouble(origine);
		out = out/3600000;
		out = Math.round( out * 100.0 ) / 100.0;
		
		return out;
	}
	
	@SuppressWarnings("unused")
	public static int numUsersConnected() {
		int usersCo = 0;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Filter usersConnected = new FilterPredicate("lastConnexionTime", FilterOperator.EQUAL, "0");
		Query q = new Query("user");
		q.setFilter(usersConnected);
		
		PreparedQuery pq = datastore.prepare(q);

		for(Entity result : pq.asIterable ()) {
			usersCo++;
		}

		return usersCo;
	}
	
	public static List<Flight> loadSimilarFlights(Flight flight, String login) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		List<Flight> flights = new ArrayList<Flight>();
		
		//Filtres simples from et to
		Filter fromCity = new FilterPredicate("from", FilterOperator.EQUAL, flight.getFrom());
		Filter toCity = new FilterPredicate("to", FilterOperator.EQUAL, flight.getTo());
		
		//Filtre composé pour les deux filtres précédents
		Filter rangeFromTo = CompositeFilterOperator.and(fromCity, toCity);
		
		//Initialisation d'une seconde date pour récupérer les vols qui sont au maximum 2 jours après la date spécifiée
		Calendar cal = Calendar.getInstance();
		cal.setTime(flight.getDeparture());
		cal.add(Calendar.DAY_OF_YEAR, 2);
		Date maxDepTime = cal.getTime();
	
		//Requete
		Query q = new Query("flight");
		q.setFilter(rangeFromTo);

		//On boucle en regardant si le vol trouvé est dans l'interval date demandée <= date trouvée <= date demandée + 2 jours
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			
			System.out.println("datastore = "+result.getProperty("time").toString());
			
			Flight oneFlight = new Flight(result.getKey(), result.getProperty("from").toString(), result.getProperty("to").toString(), 
					Integer.parseInt(result.getProperty("price").toString()), Integer.parseInt(result.getProperty("seats").toString()), 
					stringToDate(result.getProperty("arrival").toString()), stringToDate(result.getProperty("departure").toString()), 
					Integer.parseInt(result.getProperty("time").toString()));

			if(oneFlight.getDeparture().after(flight.getDeparture()) && oneFlight.getDeparture().before(maxDepTime)) {
				flights.add(oneFlight);
			}
			
			System.out.println("flight = "+oneFlight.getHours());
		}
		
		SearchDao.saveQueryInDatastore(flight, login);
		
		return flights;
	}

	public static String getCities(String fromOrTo) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		List<String> listCities = new ArrayList<String>();
			
		Query q = new Query("flight");

		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {
			if(!listCities.contains(result.getProperty(fromOrTo))) {
				listCities.add(result.getProperty(fromOrTo).toString());
			}
		}
		
		return listCities.toString();
	}
}
