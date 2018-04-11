package home.rw.javaee.customermanagement.persistence.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import home.rw.javaee.customermanagement.persistence.enums.Gender;
import home.rw.javaee.customermanagement.persistence.enums.Relationship;

@Entity
@NamedQuery(name=Customer.QUERY_GETALL,
		query= "select c from Customer c"
		)
@NamedNativeQuery(name=Customer.QUERY_BIRTDAYS,
		query= "Select * from customer where DAY(birthday) = ? and MONTH(birthday) = ? ", //Mysql Syntax
		resultClass = Customer.class
		)
//Serialzable implementieren um den Austausch mit einem EJB CLient zu realisieren
//Ansonsten kann der Client die EJB`s nicht nutzen
public class Customer implements Serializable{
	

	private static final long serialVersionUID = 1L;

	//Aufruf der query über diese Konstante (vermeidet Schreibfehler)
	public static final String QUERY_GETALL = "Customer.GetAll";
	
	public static final String QUERY_BIRTDAYS = "Customer.GetByBirthday";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@Size(min=1, max=50)
	private String firstName;
	
	@NotNull
	@Size(min=1, max=100)
	private String lastName;
	
	private Gender gender;
	
	private Relationship relationship;
	
	private Date birthday;
	
	//Speichert automatisch wenn sich etwas an der Hauptentität ändert oder der Liste ändert (cascade all)
	//Löscht abhängige ELemente wenn Hauptentität (also der Kunde) gelöscht wird (orphaned removal).
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="customerid") //damit wird automatisch eine Spalte customerid in der Tabelle Address als Fremdschlüssel angelegt
	private List<Address> adresses = new ArrayList<Address>(); //direkt initialisieren. das erleichter den Aufruf (z.b.: im Remote Testclient -> customer.getAdresses().add(address)) 
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="customerid")
	private Set<Communication> communications = new HashSet<Communication>();

	//Optimistisches Locking. Datensatz kann nur gespeichert / gelöscht werden wenn sich der Wert seid dem laden
	//des Datensatzes nicht geändert hat. (klappt nicht wenn z.b.: Der Datensatz ausserhalb der Applikation 
	//direkt auf der Datenbank geändert wurde. Dann ist ein Speichern möglich da sich das Versionsfeld ja nicht 
	//geändert hat
	//Kein getter und setter da das Feld über JPA gemanaged wird. 
	@Version
	private Timestamp lastChanged;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}


	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public List<Address> getAdresses() {
		return adresses;
	}

	public void setAdresses(List<Address> adresses) {
		this.adresses = adresses;
	}

	public Set<Communication> getCommunications() {
		return communications;
	}

	public void setCommunications(Set<Communication> communications) {
		this.communications = communications;
	}


	

}
