package home.rw.javaee.customermanagement.web.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import home.rw.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import home.rw.javaee.customermanagement.persistence.entities.Customer;

@Path("/customers")
@Stateless
public class CustomersEndpoint {

	@EJB
	private CustomerDAO customerDAO;
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> getAll() {
		
		return customerDAO.getAllCustomers();
		
	}
	
	
}
