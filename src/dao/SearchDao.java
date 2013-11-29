package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import model.Flight;
import model.Search;

public class SearchDao {
	public static void saveQueryInDatastore(Flight flight, String login) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity entityQuery = new Entity("search");

		entityQuery.setProperty("from", flight.getFrom());
		entityQuery.setProperty("to", flight.getTo());
		entityQuery.setProperty("departure", flight.getDeparture());
		entityQuery.setProperty("dateSearched", new Date());
		entityQuery.setProperty("login", login);

		datastore.put(entityQuery);
	}

	public static List<Search> getSearches(String login) {
		List<Search> searches = new ArrayList<Search>();

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Filter filterLogin = new FilterPredicate("login", FilterOperator.EQUAL,
				login);

		// Requete
		Query q = new Query("search");
		q.setFilter(filterLogin);

		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			Search oneSearch = new Search(result.getProperty("from").toString(), result.getProperty("to").toString(), 
					result.getProperty("departure").toString(), result.getProperty("dateSearched").toString(), result.getProperty("login").toString());

			searches.add(oneSearch);
		}

		return searches;
	}
}
