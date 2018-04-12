package chaosnetworkz.javaee.customermanagement.web.beans;

import java.io.Serializable;

import chaosnetworkz.javaee.customermanagement.persistence.entities.Communication;
import chaosnetworkz.javaee.customermanagement.persistence.enums.Kind;

public class CommunicationBean implements Serializable{


	private static final long serialVersionUID = 1L;

	private Communication communicaton;

	private boolean isEditing = false;
	
	public CommunicationBean() {
		
	}
	
	public CommunicationBean (Communication communication) {
		this.communicaton = communication;
	}

	public Communication getCommunicaton() {
		return communicaton;
	}

	public void setCommunicaton(Communication communicaton) {
		this.communicaton = communicaton;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	
	/**
	 * Kommunikationart im Klartext zurückgeben (zur Darstellung in der Webseite)
	 */
	public String getCommunicationKind() {
		if ( communicaton.getKind() == null || communicaton.getKind() == Kind.Unknown ) {
			return "Unbekannt";
		}
		
		if (communicaton.getKind() == Kind.Business) {
			return "Geschäftlich";
		}
		return "Privat";
	}
	
	/**
	 * Kommunikationstyp im Klartext zurückgeben (zur Darstellung in der Webseite)
	 */
	public String getCommunikationType() {
		if (communicaton.getCommunicationType() == null) {
			return "Nicht angegeben";
		}
		
		switch (communicaton.getCommunicationType()) {
			case Telephone:
				return "Festnetz";
			case Fax:
				return "Fax";
			case Email:
				return "Email";
			case Web:
				return "Internetadresse";
			case Mobile:
				return "Mobil";
			default:
				return "Unbekannt";
		}
		
	}
	
	public void toggleEditing() {
		isEditing = !isEditing;
	}
	
	
	
}
