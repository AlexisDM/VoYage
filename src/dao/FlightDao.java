package dao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import model.Flight;
import model.User;

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
}