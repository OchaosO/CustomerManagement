package chaosnetworkz.javaee.customermanagement.ejb.beans;

import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import chaosnetworkz.javaee.customermanagement.ejb.interfaces.MessageSender;
import chaosnetworkz.javaee.customermanagement.persistence.entities.Communication;
import chaosnetworkz.javaee.customermanagement.persistence.entities.Customer;
import chaosnetworkz.javaee.customermanagement.persistence.enums.CommunicationType;

@Stateless
@Remote(MessageSender.class)
//Mailversand asynchron mit einer Stateless Bean, die als Remote das MessageSender Interface bereitstellt 
public class MessageSenderBean implements MessageSender{

	//Mail Session (Konfiguration im APP Server) über JNDI Namen injizieren
	@Resource(name="java:jboss/mail/TurckMail")
	private Session session;

	//Implentieren der asynchronen Methode für den Mailsversand
	@Override
	public Future<Boolean> send(String subject, String body, Customer customer) {
		
		boolean result = false;
		//Prüfen Communication vom Typ Eail angelegt
		Communication email = null;
		for (Communication communication : customer.getCommunications()) {
			if (communication.getCommunicationType() == CommunicationType.Email) {
				email = communication;
				break;
			}
		}

		if (email != null) {
			//Neue Message vom Typ Mime Message erstellen (Ziel ist die Destination im App Server) 
			javax.mail.Message mail = new MimeMessage(session);
			try {
				mail.setRecipients(RecipientType.TO, InternetAddress.parse(email.getValue()));
				mail.setSubject("Happy Birthday");
				mail.setText("Hallo,\n\nHappy Birthday!");
				//Mail senden
				Transport.send(mail);
				
				result = true;
				
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}

		return new AsyncResult<Boolean>(result);

	}
	
}
