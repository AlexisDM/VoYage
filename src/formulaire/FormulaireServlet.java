package formulaire;

import java.io.IOException;
import java.util.Properties;
import java.util.Date;

import javax.servlet.http.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class FormulaireServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String cmd = req.getParameter("cmd");
		
		if (cmd != null) {
			if ("PostInfo".equals(cmd)) {
				
				String nom = req.getParameter("nom");
				String prenom = req.getParameter("prenom");
				String age = req.getParameter("age");
				String email = req.getParameter("email");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				
				Date dateCreaAccount = new Date();
				 
				
				
				if (nom != null && prenom != null && age != null && email != null && login != null && password != null) {
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					
					Entity user = new Entity("user");
					
					user.setProperty("nom", nom);
					user.setProperty("prenom", prenom);
					user.setProperty("age", age);
					user.setProperty("email", email);
					user.setProperty("login", login);
					user.setProperty("password", password);
					user.setProperty("creationAccount", dateCreaAccount);
					user.setProperty("lastConnexionDate", dateCreaAccount);
					user.setProperty("lastConnexionTime", "0");
					user.setProperty("admin", "N");
					
					datastore.put(user);
					
					Properties props = new Properties();
			        Session session = Session.getDefaultInstance(props, null);

			        String msgBody = "Welcome to VoYage!!\nYour account has been activated";

			        try {
			            Message msg = new MimeMessage(session);
			            msg.setFrom(new InternetAddress("alexis.demarinis@gmail.com", "admin@cpe.fr Admin"));
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
