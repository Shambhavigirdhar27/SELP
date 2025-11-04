package com.bits.selp.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.selp.model.EquipmentModel;
import com.bits.selp.model.OrdersModel;
import com.bits.selp.service.EquipmentService;
import com.bits.selp.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import utilities.ResponseConstants;

@RestController
@RequestMapping("/order")
@Tag(name = "Orders", description = "Manage equipment lending orders")
public class OrderController {

	private final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
    OrdersService orderService;
	
	@Autowired
	EquipmentService equipmentService;


	/** 
	 * Get list of all orders by status
	 * 
	 * @param status
	 * @return
	 */
	@GetMapping("/list")
	@Operation(
		summary = "Get orders by status",
		description = "Retrieve a list of all orders filtered by their current status (e.g., Pending, Approved, Returned)."
	)
	public List<OrdersModel> getOrdersListByStatus(@RequestParam(value = "status") String status) {
		logger.info("getOrdersListByStatus(): begin");
	    List<OrdersModel> ordersList = new ArrayList<>();
	    try {
	    	ordersList = orderService.getOrdersListByStatus(status);
	    } catch (Exception e) {
	    	logger.error("Exception while fetching orders by status.", e);
	    }
	    return ordersList;
	}
	
	/** 
	 * Place order for equipment
	 * 
	 * @param userId
	 * @param equipmentid
	 * @param quantity
	 * @param returnDate
	 * @return
	 */
	@PostMapping("/place")
	public String placeOrder(@RequestParam(value = "userid") int userId, @RequestParam(value = "equipmentid") int equipmentid, @RequestParam(value = "quantity") int quantity, @RequestParam(value = "returnDate") Date returnDate) {
		logger.info("placeOrder(): begin");
		String response = "";
		try {
			EquipmentModel equipment = equipmentService.getEquipmentModelByEquipmentId(equipmentid);
			if (equipment == null) {
				return ResponseConstants.RESPONSE_EQUIP_NOT_FOUND;
			}
			
			// Check availability
			if (equipment.getAvailablequantity() < quantity) {
				return "Not enough quantity available for " + equipment.getName();
			}
			
	        // Create new order
			OrdersModel orderModel = new OrdersModel();
			orderModel.setUserid(userId);
			orderModel.setQuantity(quantity);
			orderModel.setEquipmentid(equipmentid);
			orderModel.setStatus(ResponseConstants.STATUS_PENDING_FOR_APPROVAL);	// Pending Approval
			orderModel.setReturn_date(returnDate);
			
			orderService.saveOrder(orderModel);
			response = ResponseConstants.RESPONSE_SUCCESS;
		} catch (Exception e) {	
			logger.error("Exception while placing order.", e);
			response = ResponseConstants.RESPONSE_ERROR;
		}
		return response;
	}
	
	/** 
	 * Admins to update the order status
	 * 
	 * @param orderid
	 * @param status
	 * @return
	 */
	@PostMapping("/change/status")
	public String updateOrderStatus(@RequestParam(value = "orderid") int orderid, @RequestParam(value = "status") String status) {
		logger.info("updateOrderStatus(): begin");
		String response = "";
		try {
			OrdersModel orderModel = orderService.getOrderModelByOrderId(orderid);
			
			orderModel.setStatus(status);
			
		} catch (Exception e) {
			logger.error("Exception while updating order status.", e);
		}
		return response;
	}
}
