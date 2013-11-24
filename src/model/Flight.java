package model;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class Flight {
	Key id;
	String from;
	String to;
	int price;
	int seats;
	Date arrival;
	Date departure;
	int hours;
	
	public Flight(Key id, String from, String to, int price, int seats,
			Date arrival, Date departure, int hours) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.price = price;
		this.seats = seats;
		this.arrival = arrival;
		this.departure = departure;
		this.hours = hours;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public Date getArrival() {
		return arrival;
	}

	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}
}
