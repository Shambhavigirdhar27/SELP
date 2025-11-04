package com.bits.selp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bits.selp.model.EquipmentModel;
import com.bits.selp.repository.EquipmentRepository;

@Service
public class EquipmentService {
	
    @Autowired
    EquipmentRepository equipmentRepository;
    
    public List<EquipmentModel> getEquipmentsList() {
    	return equipmentRepository.getEquipmentsList();
    }
    
    public void saveEquipmentDetails(EquipmentModel equipmentModel) {
    	equipmentRepository.save(equipmentModel);
    }
    
    public EquipmentModel getEquipmentModelByEquipmentId(int equipmentId) {
    	return equipmentRepository.findByEquipmentid(equipmentId);
    }
    
    public void deleteEquipment(EquipmentModel equipmentModel) {
    	equipmentRepository.delete(equipmentModel);
    }
}
