package com.bits.selp.model;

import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "equipment")
public class EquipmentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipmentid")
    private int equipmentid;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "equipmentcondition")
    private String condition;

    @Column(name = "totalquantity")
    private Integer totalquantity;
    
    @Column(name = "availablequantity")
    private Integer availablequantity;

    @CreationTimestamp
    @Column(name = "createts", nullable = false, updatable = false)
    private Timestamp createts;

    @UpdateTimestamp
    @Column(name = "updatets", nullable = false)
    private Timestamp updatets;

	public int getEquipmentid() {
		return equipmentid;
	}

	public void setEquipmentid(int equipmentid) {
		this.equipmentid = equipmentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer getTotalquantity() {
		return totalquantity;
	}

	public void setTotalquantity(Integer totalquantity) {
		this.totalquantity = totalquantity;
	}

	public Integer getAvailablequantity() {
		return availablequantity;
	}

	public void setAvailablequantity(Integer availablequantity) {
		this.availablequantity = availablequantity;
	}

	public Timestamp getCreatets() {
		return createts;
	}

	public void setCreatets(Timestamp createts) {
		this.createts = createts;
	}

	public Timestamp getUpdatets() {
		return updatets;
	}

	public void setUpdatets(Timestamp updatets) {
		this.updatets = updatets;
	}
    
}
