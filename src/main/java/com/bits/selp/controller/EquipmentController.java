package com.bits.selp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.selp.model.EquipmentModel;
import com.bits.selp.service.EquipmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import utilities.ResponseConstants;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Manage school equipment records")
public class EquipmentController {

	private final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
	

	@Autowired
    EquipmentService equipService;

	/** 
	 * Get equipments list 
	 * 
	 * @return
	 */
    @GetMapping("/list")
    @Operation(
    	summary = "Get all equipments",
        description = "Fetch a list of all equipment available in the system."
    )
    public List<EquipmentModel> getEquipmentsList() {
    	logger.info("getEquipmentsList(): begin");
    	List<EquipmentModel> equipmentsList = new ArrayList<>();
    	try {
    		equipmentsList = equipService.getEquipmentsList();
    	} catch (Exception e) {
    		logger.error("Exception while fetching list of equipments.", e);
    	}
        return equipmentsList;
    }
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 
     * Save new equipment details
     * 
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addNewEquipment(@RequestBody(required = true) String payload) {
        Map<String, String> response = new HashMap<>();
        try {
        	System.out.println("PAYLOAD RECEIVED: " + payload);
            if (payload == null || payload.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Empty request body");
                return ResponseEntity.badRequest().body(response);
            }

            ObjectMapper mapper = new ObjectMapper();
            EquipmentModel equipmentModel = mapper.readValue(payload, EquipmentModel.class);

            equipService.saveEquipmentDetails(equipmentModel);

            response.put("status", "success");
            response.put("message", "Equipment added successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }



    /** 
     * Delete Equipment
     * 
     * @param equipmentId
     * @return
     */
    @PostMapping("/delete")
    @Operation(summary = "Delete equipment", description = "Deletes the selected equipment record by ID.")
    public String deleteEquipment(@RequestParam(value = "equipmentid") int equipmentId) {
    	logger.info("deleteEquipment(): begin");
    	String response = "";
    	try {
    		EquipmentModel equipmentModel = equipService.getEquipmentModelByEquipmentId(equipmentId);
    		equipService.deleteEquipment(equipmentModel);
    		response = ResponseConstants.RESPONSE_SUCCESS;
    	} catch(Exception e) {
    		logger.error("Exception while deleting equipment.", e);
    		response = ResponseConstants.RESPONSE_ERROR;
    	}
		return response;
    }
    
    /** 
     * Edit equipment details
     * 
     * @param equipmentId
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @Operation(summary = "Edit equipment", description = "Update the selected equipment record.")
    public String editEquipment(@RequestParam(value = "equipmentid") int equipmentId, HttpServletRequest request) {
    	logger.info("editEquipment(): begin");
    	String response = "";
    	try {
    		EquipmentModel equipmentModel = equipService.getEquipmentModelByEquipmentId(equipmentId);
    		equipmentModel.setName(request.getParameter("equipmentName"));
    		equipmentModel.setTotalquantity(Integer.parseInt(request.getParameter("totalQuantity")));
    		equipmentModel.setCategory(request.getParameter("category"));
    		equipmentModel.setCondition(request.getParameter("condition"));
    		
    		equipService.saveEquipmentDetails(equipmentModel);
    		response = ResponseConstants.RESPONSE_SUCCESS;
    	} catch (Exception e) {
    		logger.error("Error updating Equipment details");
    		response = ResponseConstants.RESPONSE_ERROR;
    	}
    	return response;
    }
}
