package chaosnetworkz.javaee.customermanagement.ejb.beans;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import chaosnetworkz.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import chaosnetworkz.javaee.customermanagement.persistence.entities.Customer;

@Stateless //Data Layer in der Applikation
@Remote(CustomerDAO.class) 
//Remote Clients verwenden das Interface und nicht mehr die Bean (Remote view)
public class CustomerBean implements CustomerDAO{

	@PersistenceContext
	private EntityManager em; //Zum speichern, ändern usw. der Daten auf der Datenbank
	
	@Override
	public Customer create(Customer customer) {
		em.persist(customer);
		return customer;
	}

	@Override
	public Customer update(Customer customer) {
		return em.merge(customer);
	}

	@Override
	public void remove(int id) {
		Customer toBeDeleted = getCustomer(id);
		em.remove(toBeDeleted);
	}

	@Override
	public Customer getCustomer(int id) {
		return em.find(Customer.class, id);
	}

	@Override
	public List<Customer> getAllCustomers() {
		 
		return em.createNamedQuery(Customer.QUERY_GETALL , Customer.class)
				.getResultList();
		
	}

	@Override
	public List<Customer> getCustomersHavingBirthday() {
		
		//Aktuelles Datum
		Calendar todaysCalendar = Calendar.getInstance();
		
		return em.createNamedQuery(
						Customer.QUERY_BIRTDAYS, Customer.class)
						.setParameter(1, todaysCalendar.get( Calendar.DAY_OF_MONTH ))
						.setParameter(2, todaysCalendar.get( Calendar.MONTH) )
						.getResultList();
		
	}

}
