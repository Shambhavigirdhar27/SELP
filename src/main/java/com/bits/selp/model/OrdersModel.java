package com.bits.selp.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
public class OrdersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private int orderid;

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "equipmentid")
    private Integer equipmentid;

    @Column(name = "status")
    private String status;
    
    @Column(name = "approveddate")
    private Date approved_date;

    @Column(name = "approvedbyuserid")
    private Integer approvedByUserid;

    @Column(name = "returndate")
    private Date return_date;

    @Column(name = "quantity")
    private Integer quantity;
    
    @CreationTimestamp
    @Column(name = "createts", nullable = false, updatable = false)
    private Timestamp createts;

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getEquipmentid() {
		return equipmentid;
	}

	public void setEquipmentid(Integer equipmentid) {
		this.equipmentid = equipmentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(Date approved_date) {
		this.approved_date = approved_date;
	}

	public Integer getApprovedByUserid() {
		return approvedByUserid;
	}

	public void setApprovedByUserid(Integer approvedByUserid) {
		this.approvedByUserid = approvedByUserid;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Timestamp getCreatets() {
		return createts;
	}

	public void setCreatets(Timestamp createts) {
		this.createts = createts;
	}
    
}
