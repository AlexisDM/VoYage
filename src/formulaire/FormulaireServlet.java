package formulaire;

import java.io.IOException;
import java.util.Properties;
import java.util.Date;

import javax.servlet.http.*;

import dao.UserDao;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.User;

@SuppressWarnings("serial")
public class FormulaireServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("PostInfo".equals(cmd)) {
				//Récupération des données saisies
				String nom = req.getParameter("nom");
				String prenom = req.getParameter("prenom");
				String age = req.getParameter("age");
				String email = req.getParameter("email");
				String login = req.getParameter("login");
				//String password = req.getParameter("password");
				
				//Génération du mot de passe
				String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
			    String pass = "";
			    for(int x=0;x<8;x++)
			    {
			       int i = (int)Math.floor(Math.random() * 62);
			       pass += chars.charAt(i);
			    }
				
			    Date dateCreaAccount = new Date();
				
			    
				if (nom != null && prenom != null && age != null && email != null && login != null) {
					//Enregistrement de l'utilisateur dans le DataStore
					//DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					
					User userToAdd = new User(email, login, pass, prenom, prenom, "N", Integer.parseInt(age), dateCreaAccount, dateCreaAccount, -1);
					UserDao.addUser(userToAdd);
					
					/*
					Entity user = new Entity("user");
					user.setProperty("nom", nom);
					user.setProperty("prenom", prenom);
					user.setProperty("age", age);
					user.setProperty("email", email);
					user.setProperty("login", login);
					user.setProperty("password", pass);
					user.setProperty("creationAccount", dateCreaAccount);
					user.setProperty("lastConnexionDate", dateCreaAccount);
					user.setProperty("lastConnexionTime", "0");
					user.setProperty("admin", "Y");
					datastore.put(user);
					*/
					
					//Envoie du mail
					Properties props = new Properties();
			        Session session = Session.getDefaultInstance(props, null);

			        String msgBody = "Bienvenue sur VoYage!!\n"
			        		+ "Votre compte a bien été créé.\n"
			        		+ "Votre mot de passe est : "+pass+"\n"
			        		+ "Vous pourrez le modifier lors de votre prochaine connexion.";

			        try {
			            Message msg = new MimeMessage(session);
			            msg.setFrom(new InternetAddress("alexis.demarinis@gmail.com", "noReply@VoYage.fr Admin"));
			            msg.addRecipient(Message.RecipientType.TO,
			                             new InternetAddress(email, login));
			            msg.setSubject("Your account has been activated");
			            msg.setText(msgBody);
			            Transport.send(msg);
					} catch (AddressException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
