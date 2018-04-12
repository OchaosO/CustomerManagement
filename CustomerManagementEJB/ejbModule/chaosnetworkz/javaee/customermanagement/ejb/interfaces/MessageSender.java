package chaosnetworkz.javaee.customermanagement.ejb.interfaces;

import java.util.concurrent.Future;

import chaosnetworkz.javaee.customermanagement.persistence.entities.Customer;

public interface MessageSender {
	
	//Future of boolean drückt aus, das die Methode asynchron laufen kann
	//Damit wird eine Instanz nebenläufig gestartet und vom Message Excecutor Service des App Servers verwaltet
	public Future<Boolean> send(String subject, String body, Customer customer);

}
