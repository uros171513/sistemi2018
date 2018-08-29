package com.sbnz.sbnzproject.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.model.MedicineComponent;
import com.sbnz.sbnzproject.repository.ComponentRepository;
import com.sbnz.sbnzproject.service.ComponentService;

@Service
public class ComponentServiceImpl implements ComponentService {

	@Autowired
	ComponentRepository componentRepository;

	@Override
	public List<MedicineComponent> getAll() {
		return componentRepository.findAll();
	}

	@Override
	public MedicineComponent create(MedicineComponent component) {
		return componentRepository.save(component);
	}

	@Override
	public MedicineComponent delete(Long id) {
		MedicineComponent component = componentRepository.findOne(id);
		componentRepository.deleteComponentById(id);
		return component;
	}

	@Override
	public MedicineComponent findById(Long id) {
		return componentRepository.findOne(id);
	}

}
