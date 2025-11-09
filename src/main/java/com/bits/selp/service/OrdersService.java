package com.bits.selp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bits.selp.model.OrdersModel;
import com.bits.selp.repository.OrdersRepository;

@Service
public class OrdersService {
	
    @Autowired
    OrdersRepository ordersRepository;
    
    public List<OrdersModel> getOrdersList() {
    	return ordersRepository.getOrdersList();
    }
    
    public OrdersModel getOrderModelByOrderId(int orderid) {
    	return ordersRepository.getOrderModelByOrderId(orderid);
    }
    
    public void saveOrder(OrdersModel orderModel) {
    	ordersRepository.save(orderModel);
    }

	public List<OrdersModel> getOrdersListByUserId(Integer userid) {
		// TODO Auto-generated method stub
		return ordersRepository.findByUserid(userid);
	}
}
