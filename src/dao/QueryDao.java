package dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import model.Flight;

public class QueryDao {
	public static void saveQueryInDatastore(Flight flight) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Entity entityQuery = new Entity("query");
		
		entityQuery.setProperty("from", flight.getFrom());
		entityQuery.setProperty("to", flight.getTo());
		entityQuery.setProperty("departure", flight.getDeparture());
		
		datastore.put(entityQuery);
	}
}
