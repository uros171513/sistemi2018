package com.sbnz.sbnzproject.event;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Patient;

@Role(Role.Type.EVENT)
@Expires("12m")
public class OxygenLevelNotGoodEvent {
	private Patient patient;
	private String message;
	
	public OxygenLevelNotGoodEvent() {
	}
	 
	public OxygenLevelNotGoodEvent(Patient p, String message) {
		this.patient=p; this.message=message;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "**Oxygen Level Not Good Event\n\tpatient:\n\t\t" + patient.getName() + 
				"\n\tmessage:\n\t\t" + message;
	}
	
	
}
