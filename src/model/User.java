package model;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class User {
	Key id;
	String email;
	String login;
	String password;
	String prenom;
	String nom;
	String admin;
	int age;
	Date creationAccount;
	Date lastConnectionDate;
	int lastConnectionTime;

	public User(Key id, String email, String login, String password, String prenom,
			String nom, String admin, int age, Date creationAccount, Date lastConnectionDate,
			int lastConnectionTime) {
		this.id = id;
		this.email = email;
		this.login = login;
		this.password = password;
		this.prenom = prenom;
		this.nom = nom;
		this.admin = admin;
		this.age = age;
		this.creationAccount = creationAccount;
		this.lastConnectionDate = lastConnectionDate;
		this.lastConnectionTime = lastConnectionTime;
	}
	
	public User(String email, String login, String password, String prenom,
			String nom, String admin, int age, Date creationAccount, Date lastConnectionDate,
			int lastConnectionTime) {
		this.email = email;
		this.login = login;
		this.password = password;
		this.prenom = prenom;
		this.nom = nom;
		this.admin = admin;
		this.age = age;
		this.creationAccount = creationAccount;
		this.lastConnectionDate = lastConnectionDate;
		this.lastConnectionTime = lastConnectionTime;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getCreationAccount() {
		return creationAccount;
	}

	public void setCreationAccount(Date creationAccount) {
		this.creationAccount = creationAccount;
	}

	public Date getLastConnectionDate() {
		return lastConnectionDate;
	}

	public void setLastConnectionDate(Date lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
	}

	public int getLastConnectionTime() {
		return lastConnectionTime;
	}

	public void setLastConnectionTime(int lastConnectionTime) {
		this.lastConnectionTime = lastConnectionTime;
	}
}
