package chaosnetworkz.javaee.customermanagement.web.beans;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;

import chaosnetworkz.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import chaosnetworkz.javaee.customermanagement.ejb.interfaces.MessageSender;
import chaosnetworkz.javaee.customermanagement.persistence.entities.Customer;

/**
 * Nachrichtenversand implementieren (Diese Instanz wird dann von einer Managed Bean verwendet / aus JSF angestoßen)
 * @author ralf.wurm
 *
 */
@ApplicationScoped
public class MailSender {
	
	@EJB
	private MessageSender messageSender;
	
	@EJB
	private CustomerDAO customerDAO;
	
	//Der Managed Excecutor Service sorgt für den asynchronen Versand der Mail
	//Startet und verwaltet den dafür erforderlichen Prozess und alles notwendige 
	@Resource(name="DefaultManagedExecutorService")
	private ManagedExecutorService executor;

	public void sendMail(final String message, final String subject, final int recipientId) {
		
		Runnable task = new Runnable() {
			@Override
			public void run() {
				Customer recipient = customerDAO.getCustomer(recipientId);
				Future<Boolean> result = messageSender.send(subject, message, recipient);
				try {
					result.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		
		executor.submit(task);
		
	}
	
}
