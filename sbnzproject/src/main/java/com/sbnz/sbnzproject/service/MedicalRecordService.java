package com.sbnz.sbnzproject.service;

import java.util.Collection;

import com.sbnz.sbnzproject.model.MedicalRecord;

public interface MedicalRecordService {


	Collection<MedicalRecord> getAll();

	MedicalRecord findById(Long id);

	MedicalRecord create(MedicalRecord medicalRecord, String username);

}
