package com.bits.selp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bits.selp.model.OrdersModel;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersModel, Integer> {
	
	@Query("SELECT o FROM OrdersModel o where o.status = :status")
	public List<OrdersModel> getOrdersListByStatus(@Param(value = "status") String status);
	
	@Query("SELECT o FROM OrdersModel o where o.orderid = :orderid")
	public OrdersModel getOrderModelByOrderId(@Param(value = "orderid") int orderid);
}
