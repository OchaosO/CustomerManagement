package chaosnetworkz.javaee.customermanagement.ejb.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

import chaosnetworkz.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import chaosnetworkz.javaee.customermanagement.persistence.entities.Customer;

@Singleton
@Startup
//Definition einer JMS Queue
//Hier auf Ebene der Bean (in einer Produktivumgebung wird das ganze auf Ebene des App Server aufgesetzt)
@JMSDestinationDefinitions(
	{
		@JMSDestinationDefinition(
			name="java:global/jms/birthdayMailQueue", //JNDI Name
			interfaceName = "javax.jms.Queue",
			description = "Versenden von Geburtstagsmails")
	})
public class BirthdayNotificationBean {

	//EJB verwenden um das Interface zu injizieren weil das Ziel ein EJB (hier eine Session Bean die über das Interface angesprochen wird) ist. 
	//@Inject würd denke ich auch gehen
	@EJB
	private CustomerDAO customerDAO;
	
	//Zur Benutzung / zum Anprechen der Queue
	@Inject
	private JMSContext jmsContext;
	
	//Ziel in das geschrieben werden soll (Hier die im Klassenkopf angelegte Message Queue)
	@Resource(mappedName="java:global/jms/birthdayMailQueue")
	private Destination birthdayDestination; 
	
	@PostConstruct
	private void init() {
		System.out.println("Bean initialisiert." + this.getClass().getName());
	}
	
	@Schedule(hour="13", minute="30")
	private void checkBirtday() {
		
		List<Customer> birthdays = customerDAO.getCustomersHavingBirthday();
		
		for (Customer customer : birthdays) {
			
			//Producer erzeugen mit dem eine Nachricht versendet werden kann und versenden
			//Das Ziel ist die MailQueue, der Inhalt ist der Kunde
			jmsContext.createProducer().send(birthdayDestination, customer);
			
		}
		
	}
	
}
