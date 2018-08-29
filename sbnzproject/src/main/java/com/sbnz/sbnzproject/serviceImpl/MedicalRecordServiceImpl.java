package com.sbnz.sbnzproject.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.SbnzprojectApplication;
import com.sbnz.sbnzproject.model.MedicalRecord;
import com.sbnz.sbnzproject.model.Medicine;
import com.sbnz.sbnzproject.repository.MedicalRecordRepository;
import com.sbnz.sbnzproject.service.MedicalRecordService;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

	@Autowired
	MedicalRecordRepository medicalRecordRepository;

	@Override
	public MedicalRecord create(MedicalRecord medicalRecord, String username) {
		ArrayList<Medicine> medicines = new ArrayList();
		for(Medicine m:medicalRecord.getMedicine()) {
			m.setDoctor(SbnzprojectApplication.users.get("currentUser-" + username));
			medicines.add(m);
		}
		medicalRecord.getMedicine().clear();
		for(Medicine m:medicines) {
			System.err.println(m.getName());
			medicalRecord.getMedicine().add(m);
		}
		medicalRecord.setDoctor(SbnzprojectApplication.users.get("currentUser-" + username));
		return medicalRecordRepository.save(medicalRecord);
	}

	@Override
	public Collection<MedicalRecord> getAll() {
		return medicalRecordRepository.findAll();
	}

	@Override
	public MedicalRecord findById(Long id) {
		return medicalRecordRepository.getOne(id);
	}
}
