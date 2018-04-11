package home.rw.javaee.customermanagement.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.Test;

import home.rw.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import home.rw.javaee.customermanagement.ejb.interfaces.MessageSender;
import home.rw.javaee.customermanagement.persistence.entities.Address;
import home.rw.javaee.customermanagement.persistence.entities.Communication;
import home.rw.javaee.customermanagement.persistence.entities.Customer;
import home.rw.javaee.customermanagement.persistence.enums.CommunicationType;
import home.rw.javaee.customermanagement.persistence.enums.Gender;
import home.rw.javaee.customermanagement.persistence.enums.Kind;



public class CustomerBeanTester {

	private CustomerDAO customerDAO;
	
	private MessageSender messageSender;
	
	@Before
	//Objekte für Datenzugriff vorbereiten
	public void setUp() throws Exception {
		
		try {
			
			final Hashtable<String, Comparable> jndiProperties =
					new Hashtable<String, Comparable>();
			
			jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
			jndiProperties.put("jboss.naming.client.ejb.context", true);
			jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
			jndiProperties.put(Context.SECURITY_CREDENTIALS, "admin");
			jndiProperties.put(Context.SECURITY_CREDENTIALS, "admin");
			
			final Context context = new InitialContext(jndiProperties);
			
			//Das für den Remote Zugriff erstellte Interface ansprechen, 
			//die EJB ist ja per Remote Annotation mit diesem Interface verknüpft
			final String lookupName = "CustomerManagement/CustomerManagementEJB/CustomerBean!home.rw.javaee.customermanagement.ejb.interfaces.CustomerDAO";
			final String lookupName2 = "CustomerManagement/CustomerManagementEJB/MessageSenderBean!home.rw.javaee.customermanagement.ejb.interfaces.MessageSender";
			
			customerDAO = (CustomerDAO) context.lookup(lookupName);
			
			messageSender = (MessageSender) context.lookup(lookupName2);
			
		} catch (Exception ex){
			
		}
		
	}
	
	//Testfall
	@Test
	public void testCustomer() {
		
		assertNotNull(customerDAO);
		
		//New Customer
		Customer customer = new Customer();
		customer.setFirstName("Peter");
		customer.setLastName("Pan");
		customer.setGender(Gender.Male);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.YEAR, 1976);
		
		customer.setBirthday(calendar.getTime());
		
		//New Address
		Address address = new Address();
		address.setCity("Frankfurt");
		address.setCountry("Germany");
		address.setStreet("Hauptstrasse");
		address.setKind(Kind.Business);
		address.setZip("57439");
		customer.getAdresses().add(address);
		
		//New Communication
		Communication communication = new Communication();
		communication.setName("Geschäft");
		communication.setValue("ralf_wurm@web.de");
		communication.setKind(Kind.Business);
		communication.setCommunicationType(CommunicationType.Email);
		customer.getCommunications().add(communication);
		
		Customer result = customerDAO.create(customer);
		assertNotEquals(result.getId(), 0);
		
		//Gerade angelegten Kunde erneut abrufen
		result = customerDAO.getCustomer(result.getId());
		
		//Adresse prüfen
		assertTrue(result.getAdresses().size() == 1);
		Address adressFromServer = result.getAdresses().get(0);
		assertEquals(address.getStreet(), adressFromServer.getStreet());
		
		//Communication prüfen
		assertTrue(result.getCommunications().size() == 1);
		Communication commFromServer = result.getCommunications().iterator().next(); //Fetch first Element of HashSet
		assertEquals(communication.getValue(), commFromServer.getValue());
		
	
	}


}
