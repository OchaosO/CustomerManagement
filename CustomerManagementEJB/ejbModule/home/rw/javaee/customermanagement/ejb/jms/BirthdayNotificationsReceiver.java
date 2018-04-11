package home.rw.javaee.customermanagement.ejb.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import home.rw.javaee.customermanagement.persistence.entities.Communication;
import home.rw.javaee.customermanagement.persistence.entities.Customer;
import home.rw.javaee.customermanagement.persistence.enums.CommunicationType;

//Klasse repräsentiert den JMS Nachrichtenempfänger (als Message Driven Bean aufgesetzt)
@MessageDriven(
		activationConfig= {
				//Angabe der Message Queue die abgefragt wird
				@ActivationConfigProperty(
						propertyName="destination",
						propertyValue="java:global/jms/birthdayMailQueue"
						)
		}
		) 
public class BirthdayNotificationsReceiver implements MessageListener{

	//Mail Session (Konfiguration im APP Server) über JNDI Namen injizieren
	@Resource(name="java:jboss/mail/TurckMail")
	private Session session;
	
	@Override
	public void onMessage(Message message) {
		
		//Prüfen ob die Nachricht einen Objectgraphen beinhaltet
		//Da das Customer obejct an die Queue gesendet wurde sollte das also passen (es sollte ein Objekt in der Queue sein)
		if (message instanceof ObjectMessage) {
			
			try {
				Customer customer = message.getBody(Customer.class);
				
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
					mail.setRecipients(RecipientType.TO, InternetAddress.parse(email.getValue()));
					mail.setSubject("Happy Birthday");
					mail.setText("Hallo,\n\nHappy Birthday!");
					//Mail senden
					Transport.send(mail);
				}
				
				
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
