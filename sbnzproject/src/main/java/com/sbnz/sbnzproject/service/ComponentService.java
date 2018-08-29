package com.sbnz.sbnzproject.service;

import java.awt.Component;
import java.util.Collection;
import java.util.List;

import com.sbnz.sbnzproject.model.MedicineComponent;

public interface ComponentService {

	List<MedicineComponent> getAll();

	MedicineComponent create(MedicineComponent component);

	MedicineComponent delete(Long id);

	MedicineComponent findById(Long id);

}
