package com.bits.selp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bits.selp.model.EquipmentModel;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentModel, Integer> {
	
	@Query("SELECT e FROM EquipmentModel e")
	public List<EquipmentModel> getEquipmentsList();
	
	public EquipmentModel findByEquipmentid(int equipmentid);
}
