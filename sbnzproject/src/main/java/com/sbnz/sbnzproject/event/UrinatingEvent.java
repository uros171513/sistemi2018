package com.sbnz.sbnzproject.event;

import java.util.Date;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import com.sbnz.sbnzproject.model.Patient;
@Role(Role.Type.EVENT)
@Timestamp("executionTime")
@Expires("15m")
public class UrinatingEvent {

	private Date executionTime;
	private Integer amount;
	private Patient patient;
	
	public UrinatingEvent() {
		// constructor
		this.executionTime=new Date();
	}
	
	public UrinatingEvent(Patient p, int amount) {
		this.amount=amount;
		this.patient=p;
		this.executionTime=new Date();
	}

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	public String toString() {
		return "**Urinating Event [executionTime=" + executionTime + ", amount=" + amount + ", patient=" + patient + "]";
	}
	
	
}
