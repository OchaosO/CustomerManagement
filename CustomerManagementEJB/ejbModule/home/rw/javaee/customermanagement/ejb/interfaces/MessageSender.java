package home.rw.javaee.customermanagement.ejb.interfaces;

import java.util.concurrent.Future;

import home.rw.javaee.customermanagement.persistence.entities.Customer;

public interface MessageSender {
	
	//Future of boolean dr�ckt aus, das die Methode asynchron laufen kann
	//Damit wird eine Instanz nebenl�ufig gestartet und vom Message Excecutor Service des App Servers verwaltet
	public Future<Boolean> send(String subject, String body, Customer customer);

}
