package model;

public class Search {
	private String from;
	private String to;
	private String dateFlight;
	private String dateSearch;
	private String login;
	
	public Search() { }
	
	public Search(String from, String to, String dateFlight, String dateSearch, String login) {
		this.from = from;
		this.to = to;
		this.dateFlight = dateFlight;
		this.dateSearch = dateSearch;
		this.login = login;
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

	public String dateFlight() {
		return dateFlight;
	}

	public void setDateSearched(String dateFlight) {
		this.dateFlight = dateFlight;
	}

	public String dateSearch() {
		return dateSearch;
	}

	public void setDateQueried(String dateSearch) {
		this.dateSearch = dateSearch;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
