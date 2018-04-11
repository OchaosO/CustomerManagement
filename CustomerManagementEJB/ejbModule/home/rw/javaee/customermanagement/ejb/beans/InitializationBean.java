package home.rw.javaee.customermanagement.ejb.beans;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import home.rw.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import home.rw.javaee.customermanagement.persistence.entities.Address;
import home.rw.javaee.customermanagement.persistence.entities.Communication;
import home.rw.javaee.customermanagement.persistence.entities.Customer;
import home.rw.javaee.customermanagement.persistence.enums.Gender;
import home.rw.javaee.customermanagement.persistence.enums.Kind;


//Klasse sorgt dafür, dass die Applikation nach dem Startup immer in einem definierten Zustand steht (hier gefüllte Tabelle)
//per Singleton ist sichergestellt, dass die Bean pro Applikationsserver nur einmal erzeugt wird
//Auf einem Cluster wird die Bean mit dem Singleton Bean der anderen Clustermember synchronisiert
//Eine Singleton Bean hat immer einen Status (zur Synchronisation mit anderen Clustermembern)
//Startup sorgt dafür, dass die Bean beim Start der Applikation vom Applikationserver erzeugt wird
@Singleton
@Startup
public class InitializationBean {
	
	//EJB verwenden um das Interface zu injizieren weil das Ziel ein EJB (hier eine Session Bean die über das Interface angesprochen wird) ist. 
	//@Inject würd denke ich auch gehen
	@EJB
	private CustomerDAO customerDAO;

	
	@PostConstruct
	private void initialize() {
		
		if (customerDAO.getAllCustomers().size() == 0) {
			
			//Default Customer
			Customer customer = new Customer();
			customer.setFirstName("Max");
			customer.setLastName("Mustermann");
			customer.setGender(Gender.Male);
			Calendar birthday = Calendar.getInstance();
			birthday.set(Calendar.DAY_OF_MONTH, 1);
			birthday.set(Calendar.MONTH, 1);
			birthday.set(Calendar.YEAR, 1970);
			customer.setBirthday( birthday.getTime() );
			
			//Default Address
			Address address = new Address();
			address.setCity("Musterhausen");
			address.setCountry("Germany");
			address.setStreet("Musterstrasse 1a");
			address.setZip("12345");
			address.setKind(Kind.Business);
			customer.getAdresses().add(address);
			
			//Default Communication
			Communication communication = new Communication();
			communication.setName("Geschäft");
			communication.setValue("ralf_wurm@web.de");
			communication.setKind(Kind.Business);
			customer.getCommunications().add(communication);
			
			//Customer speichern
			customerDAO.create(customer);
			
			
		}
		
	}
	
	
}
