package com.bits.selp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
	    description = "Retrieve a list of all orders filtered by their current status (e.g., Pending, Approved, Returned).",
	    parameters = {
	    	@Parameter(
	    		name = "status",
	            description = "Status of the orders to fetch (e.g., Pending, Approved, Returned).",
	            required = true,
	            example = "Pending"
	    	)
		},
	    responses = {
	    	@ApiResponse(
	    		responseCode = "200",
	            description = "Orders retrieved successfully.",
	            content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = OrdersModel.class))
	    	),
	        @ApiResponse(responseCode = "400", description = "Invalid status parameter."),
	        @ApiResponse(responseCode = "500", description = "Server error while fetching orders.")
		}
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
	@Operation(
		summary = "Place a new order",
	    description = "Allows a student to place an order for a specific equipment item.",
	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	    	description = "Order details including user ID, equipment ID, quantity, and return date.",
	        required = true,
	        content = @Content(schema = @Schema(implementation = OrdersModel.class))
	    ),
	    responses = {
	    	@ApiResponse(responseCode = "201", description = "Order placed successfully."),
	        @ApiResponse(responseCode = "400", description = "Invalid input or insufficient equipment quantity."),
	        @ApiResponse(responseCode = "404", description = "Equipment not found."),
	        @ApiResponse(responseCode = "500", description = "Server error while placing order.")
		}
	)
	public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody OrdersModel orderRequest) {
	    logger.info("placeOrder(): begin");
	    Map<String, Object> response = new HashMap<>();

	    try {
	        EquipmentModel equipment = equipmentService.getEquipmentModelByEquipmentId(orderRequest.getEquipmentid());
	        if (equipment == null) {
	            response.put("status", "error");
	            response.put("message", "Equipment not found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        // Check availability
	        if (equipment.getAvailablequantity() < orderRequest.getQuantity()) {
	            response.put("status", "error");
	            response.put("message", "Not enough quantity available for " + equipment.getName());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        // Create new order
	        OrdersModel orderModel = new OrdersModel();
	        orderModel.setUserid(orderRequest.getUserid());
	        orderModel.setQuantity(orderRequest.getQuantity());
	        orderModel.setEquipmentid(orderRequest.getEquipmentid());
	        orderModel.setStatus(ResponseConstants.STATUS_PENDING_FOR_APPROVAL);
	        orderModel.setReturn_date(orderRequest.getReturn_date());

	        orderService.saveOrder(orderModel);

	        response.put("status", "success");
	        response.put("message", "Order placed successfully");
	        response.put("order", orderModel);

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (Exception e) {
	        logger.error("Exception while placing order.", e);
	        response.put("status", "error");
	        response.put("message", "Failed to place order: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	/** 
	 * Admins to update the order status
	 * 
	 * @param orderid
	 * @param status
	 * @return
	 */
	@PostMapping("/change/status")
	@Operation(
		summary = "Update order status",
	    description = "Allows an admin to update the status of an order (e.g., from Pending to Approved or Returned).",
	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	    	description = "JSON object containing order ID and new status.",
	        required = true,
	        content = @Content(
	        	schema = @Schema(
	        		example = "{\"orderid\": 1, \"status\": \"Approved\"}"
	        	)
	        )
	    ),
	    responses = {
	    	@ApiResponse(responseCode = "200", description = "Order status updated successfully."),
	        @ApiResponse(responseCode = "404", description = "Order not found."),
	        @ApiResponse(responseCode = "500", description = "Error while updating order status.")
		}
	)
	public Map<String, Object> updateOrderStatus(@RequestBody Map<String, Object> requestBody) {
	    logger.info("updateOrderStatus(): begin");
	    Map<String, Object> response = new HashMap<>();
	    try {
	        int orderid = (int) requestBody.get("orderid");
	        String status = (String) requestBody.get("status");

	        OrdersModel orderModel = orderService.getOrderModelByOrderId(orderid);
	        if (orderModel == null) {
	            response.put("message", "Order not found");
	            response.put("status", "error");
	            return response;
	        }

	        orderModel.setStatus(status);
	        orderService.saveOrder(orderModel);
	        response.put("message", "Order status updated successfully");
	        response.put("status", "success");

	    } catch (Exception e) {
	        logger.error("Exception while updating order status.", e);
	        response.put("message", "Failed to update order status: " + e.getMessage());
	        response.put("status", "error");
	    }
	    return response;
	}

}
