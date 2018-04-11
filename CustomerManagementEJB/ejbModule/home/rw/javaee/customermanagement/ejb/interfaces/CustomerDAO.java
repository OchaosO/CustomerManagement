package home.rw.javaee.customermanagement.ejb.interfaces;

import java.util.List;

import home.rw.javaee.customermanagement.persistence.entities.Customer;

public interface CustomerDAO {

	public Customer create(Customer customer);
	
	public Customer update(Customer customer);
	
	public void remove(int id);
	
	public Customer getCustomer(int id);
	
	public List<Customer> getAllCustomers();

	public List<Customer> getCustomersHavingBirthday();
	

}
