package com.sbnz.sbnzproject.event;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import com.sbnz.sbnzproject.model.Patient;

@Role(Role.Type.EVENT)
@Expires("12m")
public class HeartbeatNotGoodEvent {
	private Patient patient;
	private String message;
	
	public HeartbeatNotGoodEvent() {
		//constructor
	}
	
	public HeartbeatNotGoodEvent(Patient p, String m) {
		this.message=m;
		this.patient=p;
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
		return "**Heartbeat Noot Good Event\n\tpatient:\n\t\t" + patient.getName() + "\n\tmessage:\n\t\t" + message;
	}
	
	
}
