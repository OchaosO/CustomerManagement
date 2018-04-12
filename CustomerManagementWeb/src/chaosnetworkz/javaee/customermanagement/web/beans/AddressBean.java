package chaosnetworkz.javaee.customermanagement.web.beans;

import java.io.Serializable;

import chaosnetworkz.javaee.customermanagement.persistence.entities.Address;
import chaosnetworkz.javaee.customermanagement.persistence.enums.Kind;


//Lokale Bean in der View Ebene die um das Feld isEditing erweitert wurde
public class AddressBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Address address;
	
	private boolean isEditing = false;
	
	public AddressBean() {
		
	}
	
	public AddressBean (Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	
	/**
	 * Addressart im Klartext zurückgeben (zur Darstellung in der Webseite)
	 */
	public String getAddressKind() {
		if (address.getKind() == null || address.getKind() == Kind.Unknown ) {
			return "Unbekannt";
		}
		if (address.getKind() == Kind.Business) {
			return "Geschäftsadresse";
		}
		return "Privatadresse";
		
		
	}
	
	public void toggleEditing() {
		isEditing = !isEditing;
	}
	
}
