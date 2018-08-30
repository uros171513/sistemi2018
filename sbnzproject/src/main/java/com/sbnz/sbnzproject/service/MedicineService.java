package com.sbnz.sbnzproject.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.sbnz.sbnzproject.model.Medicine;

public interface MedicineService {

	Medicine create(Medicine medicine);

	Medicine delete(Long id);

	Collection<Medicine> getAll();

	Medicine findById(Long id);

	boolean checkAllergies(Long id, ArrayList<Medicine> medicines, String username);

}
