package home.rw.javaee.customermanagement.web.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import home.rw.javaee.customermanagement.ejb.interfaces.CustomerDAO;
import home.rw.javaee.customermanagement.persistence.entities.Address;
import home.rw.javaee.customermanagement.persistence.entities.Communication;
import home.rw.javaee.customermanagement.persistence.entities.Customer;

//Managed Beans erweitern die JSF-Anzeigekomponenten um Logik und sind aus dem Frontend ansprechbar
//-----------------------------------------------------------------------------------------------------
//Managed Bean mit dem Namen customers die View Scoped ist deklarieren (ist also das View Model f�r die Webseite)
//Sie repr�sentert hier einen Kundendatensatz und stellt Operation f�r JSF bereit
//Diese Bean liefert Methoden die den kompletten Lebenszyklus eines Kunden im Frontend abbilden
//------------------------------------------------------------------------------------------------
@ViewScoped //G�ltigkeitsbereich der Instanz (hier f�r eine Kombination aus Seiten und Parametern. So lange diese unver�ndert belieb nist die Bean g�ltig
@Named("customers") //M�ssen benannt sein sonst werden Sie von der JSF Seite nicht erkannt
public class CustomerBean implements Serializable{

	//Definition von Navigationszielen 
	//Startseite
	public static final String OUTCOME_INDEX = "/index?faces-redirect=true";
	//Detailseite
	public static final String OUTCOME_UPDATED = "/details?faces-redirect=true&amp;id=%s";
	//Startseite
	public static final String OUTCOME_DELETED = "/index?faces-redirect=true";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private CustomerDAO customerDAO;
	
	private Customer customer;

	private int customerId;
	
	private List<AddressBean> addresses = new ArrayList<AddressBean>();
	
	private List<CommunicationBean> communications = new ArrayList<CommunicationBean>();
	
	/**
	 * Kundendatensatz laden
	 */
	public void initCustomer() {
		
		//Ist bereits ein Kundendatensatz im Fronend geladen dann abbruch
		if (null != customer) {
			return ;
		}
		
		//Kunden mit spezieller id laden wenn im Frontend einer ausgew�hlt ist
		if (customerId > 0) {
			customer = customerDAO.getCustomer(customerId);
		}
		
		//Sollte kein Datensatz geladen worden sein dann eine neue Instanz anlegen
		if (customer == null) {
			customer = new Customer();			
		}
		
		//Copy Data wenn Adressen zum Kunden (in der Datenbank vorhanden) in Frontend Bean 
		addresses.clear();
		for (Address address : customer.getAdresses()) {
			addresses.add( new AddressBean(address) );
		}
		
		////Copy Data wenn Kommunikationskan�le zum Kunden (in der Datenbank vorhanden) in Frontend Bean 
		communications.clear();
		for ( Communication comm : customer.getCommunications() ) {
			communications.add( new CommunicationBean(comm) );
		}
		
	}
	
	/**
	 * Kundendatensatz zum speichern vorbereiten
	 */
	private void prepareCustomer() {
		
		//clear Adressliste
		customer.getAdresses().clear();
		
		//Add Addressen aus Frontend Bean
		for (AddressBean addressBean : addresses) {
			customer.getAdresses().add( addressBean.getAddress()  );
		}
		
		//Clear Kommunikationskan�le
		customer.getCommunications().clear();
		
		//Add Kommunikatinsokan�le aus Frontend Bean
		for (CommunicationBean commBean : communications) {
			customer.getCommunications().add( commBean.getCommunicaton() );
		}
		
	}
	
	/**
	 * Kunde anlegen
	 * @return
	 */
	public String create() {
		
		//Datensatz zum speichen vorbereiten
		prepareCustomer();
		
		//Datensatz in der Datenbank speichern
		customerDAO.create(customer);
		
		//Sprungmarke zur�ckgeben (auf Startseite zur�ckleiten)
		return OUTCOME_INDEX;
		
	}
	
	/**
	 * Kunde aktualisieren
	 * @return
	 */
	public String update() {
		
		//Datensatz zum speichen vorbereiten
		prepareCustomer();
		
		//Datensatz in der Datenbank speichern
		customerDAO.update(customer);
		
		//Sprungmarke zur�ckgeben (auf Detailseite)
		return OUTCOME_UPDATED;
		
	}
	
	/**
	 * Kunde l�schen
	 * @return
	 */
	public String delete() {
		
		customerDAO.remove( customer.getId() );
		
		return OUTCOME_DELETED;
	}
	
	/**
	 * Neue Adresse hinzuf�gen
	 */
	public void addAddress() {
		AddressBean addressBean = new AddressBean( new Address() );
		addressBean.setEditing(true);
		addresses.add( addressBean );
	}
	
	/**
	 * Neue Kommunikation hinzuf�gen
	 */
	public void addCommunication() {
		CommunicationBean commBean = new CommunicationBean( new Communication() );
		commBean.setEditing(true);
		communications.add(commBean);
	}
	
	/**
	 * Adressse l�schen
	 */
	public void removeAddress(AddressBean toBeRemoved) {
		addresses.remove(toBeRemoved);
	}
	
	/**
	 * Kommunikation l�schen
	 */
	public void removeCommunication(CommunicationBean toBeRemoved) {
		communications.remove(toBeRemoved);
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public List<AddressBean> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressBean> addresses) {
		this.addresses = addresses;
	}

	public List<CommunicationBean> getCommunications() {
		return communications;
	}

	public void setCommunications(List<CommunicationBean> communications) {
		this.communications = communications;
	}
	
	/**
	 * Formatiertes Geburtsdatum im Klartext zur�ckgeben (zur Darstellung in der Webseite)
	 */
	public String getBirthdayFormatted() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return dateFormat.format( customer.getBirthday() );
	}
	
	/**
	 * Beziehungstyp im Klartext zur�ckgeben (zur Darstellung in der Webseite)
	 */
	public String getRelationshipTranslated() {
		if (null == customer.getRelationship()) {
			return "Unbekannt";
		}
		
		switch ( customer.getRelationship() ) {
		case Colleague:
			return "Kollege";
		case Friend:
			return "Freund";
		case Family:
			return "Familie";
		case Job:
			return "Arbeitskollege";
		default:
			return "Unbekannt";
		
		}
		
	}
	
}
