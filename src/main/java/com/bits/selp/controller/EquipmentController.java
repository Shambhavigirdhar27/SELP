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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

import com.bits.selp.model.EquipmentModel;
import com.bits.selp.service.EquipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

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
        description = "Fetch a list of all equipment available in the system.",
        responses = {
        	@ApiResponse(
        		responseCode = "200",
                description = "List of all equipment retrieved successfully.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EquipmentModel.class))
        	),
            @ApiResponse(responseCode = "500", description = "Server error while fetching equipment list.")
    	}
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
    
    /** 
     * Save new equipment details
     * 
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Operation(
    	summary = "Add new equipment",
        description = "Save a new equipment record to the system.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        	description = "Equipment details to add",
            required = true,
            content = @Content(schema = @Schema(implementation = EquipmentModel.class))
        ),
        responses = {
        	@ApiResponse(responseCode = "201", description = "Equipment added successfully."),
            @ApiResponse(responseCode = "500", description = "Failed to add new equipment.")
    	}
    )
    public ResponseEntity<Map<String, Object>> addNewEquipment(@RequestBody EquipmentModel equipmentModel) {
        logger.info("addNewEquipment(): begin");

        Map<String, Object> response = new HashMap<>();
        try {

            equipService.saveEquipmentDetails(equipmentModel);

            response.put("status", "success");
            response.put("message", "Equipment added successfully");
            response.put("equipment", equipmentModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error while adding new equipment.", e);
            response.put("status", "error");
            response.put("message", "Failed to add new equipment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /** 
     * Delete Equipment
     * 
     * @param equipmentId
     * @return
     */
    @PostMapping("/delete")
    @Operation(
    	summary = "Delete equipment",
        description = "Deletes the selected equipment record by its unique ID.",
        parameters = {
        	@Parameter(name = "equipmentid", description = "ID of the equipment to delete", required = true, example = "101")
    	},
        responses = {
        	@ApiResponse(responseCode = "200", description = "Equipment deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Equipment not found."),
            @ApiResponse(responseCode = "500", description = "Error while deleting equipment.")
    	}
    )
    public ResponseEntity<Map<String, Object>> deleteEquipment(@RequestParam(value = "equipmentid") int equipmentId) {
    	logger.info("deleteEquipment(): begin");
    	Map<String, Object> response = new HashMap<>();
    	try {
    		EquipmentModel equipmentModel = equipService.getEquipmentModelByEquipmentId(equipmentId);
    		equipService.deleteEquipment(equipmentModel);
    		response.put("status", "success");
            response.put("message", "Equipment deleted successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    	} catch(Exception e) {
    		logger.error("Exception while deleting equipment.", e);
    		response.put("status", "error");
            response.put("message", "Failed to delete equipment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	}
    }
    
    /** 
     * Edit equipment details
     * 
     * @param equipmentId
     * @param request
     * @return
     */
    @PostMapping("/edit/{equipmentid}")
    @Operation(
    	summary = "Edit equipment details",
    	description = "Updates an existing equipment record in the system using its unique equipment ID.",
    	parameters = {
    		@io.swagger.v3.oas.annotations.Parameter(
    			name = "equipmentid",
    	        description = "Unique ID of the equipment to update",
    	        required = true,
    	        example = "101"
    		)
    	},
    	requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    		description = "Updated equipment details (name, category, condition, totalquantity)",
    	    required = true
    	),
    	responses = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
    			responseCode = "200",
    	        description = "Equipment updated successfully"
    		),
    	    @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	    	responseCode = "404",
    	        description = "Equipment not found"
    	    ),
    	    @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	    	responseCode = "500",
    	        description = "Internal server error while updating equipment"
    	    )
    	}
    )
    public ResponseEntity<Map<String, Object>> editEquipment(@RequestBody EquipmentModel equipmentRequest, @PathVariable(value = "equipmentid") Integer equipmentid) {
        logger.info("editEquipment(): begin");
        Map<String, Object> response = new HashMap<>();
        try {
            EquipmentModel equipmentModel = equipService.getEquipmentModelByEquipmentId(equipmentid);
            if (equipmentModel == null) {
                response.put("status", "error");
                response.put("message", "Equipment not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Update fields
            equipmentModel.setName(equipmentRequest.getName());
            equipmentModel.setCategory(equipmentRequest.getCategory());
            equipmentModel.setCondition(equipmentRequest.getCondition());
            equipmentModel.setTotalquantity(equipmentRequest.getTotalquantity());

            equipService.saveEquipmentDetails(equipmentModel);

            response.put("status", "success");
            response.put("message", "Equipment updated successfully");
            response.put("equipment", equipmentModel);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating Equipment details", e);
            response.put("status", "error");
            response.put("message", "Failed to update equipment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /** 
     * 
     * @param equipmentid
     * @return
     */
    @Operation(
    	summary = "Get equipment details by ID",
    	description = "Fetches details of a specific equipment item using its unique equipment ID.",
    	parameters = {
    		@io.swagger.v3.oas.annotations.Parameter(
    			name = "equipmentid",
    	        description = "Unique ID of the equipment to fetch",
    	        required = true,
    	        example = "101"
    		)
    	},
    	responses = {
    		@io.swagger.v3.oas.annotations.responses.ApiResponse(
    			responseCode = "200",
    	        description = "Equipment details fetched successfully"
    		),
    	    @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	    	responseCode = "404",
    	        description = "Equipment not found"
    	    ),
    	    @io.swagger.v3.oas.annotations.responses.ApiResponse(
    	    	responseCode = "500",
    	        description = "Internal server error while retrieving equipment"
    	    )
    	}
    )
    @GetMapping("/{equipmentid}")
    public EquipmentModel getEquipmentByEquipmentId(@PathVariable(value = "equipmentid") Integer equipmentid) {
    	try {
    		EquipmentModel equipmentModel = equipService.getEquipmentModelByEquipmentId(equipmentid);
        	return equipmentModel;
    	} catch (Exception e) {
    		logger.error("Error getting Equipment details", e);
    		return null;
    	}
    }

}
