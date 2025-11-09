package com.bits.selp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bits.selp.model.OrdersModel;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersModel, Integer> {
	
	@Query("SELECT o FROM OrdersModel o")
	public List<OrdersModel> getOrdersList();
	
	@Query("SELECT o FROM OrdersModel o where o.orderid = :orderid")
	public OrdersModel getOrderModelByOrderId(@Param(value = "orderid") int orderid);

	public List<OrdersModel> findByUserid(Integer userid);
}
