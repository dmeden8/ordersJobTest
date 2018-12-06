package com.jobtest.orders.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobtest.orders.dto.request.ChangeOrderStatusRequestDTO;
import com.jobtest.orders.dto.request.OrderRequestDTO;
import com.jobtest.orders.dto.response.BaseResponse;
import com.jobtest.orders.dto.response.OrderResponseDTO;
import com.jobtest.orders.dto.response.ResponseCode;
import com.jobtest.orders.service.OrderService;
import com.jobtest.orders.type.OrderStatus;
import com.jobtest.orders.validator.ZoneDateTimeValid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Validated
@RequestMapping("/api")
@Api(value="/api/orders", description="Operations related to orders")
public class OrderController {
	
	private final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
    private OrderService orderService;
	
	
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	@ApiOperation(value = "Create new order", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order successfully created"),
            @ApiResponse(code = 400, message = "Input data validation failed"),
    		@ApiResponse(code = 404, message = "Product not found")
    })
	public ResponseEntity<BaseResponse<String>> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
		
		log.info("Called create order: {}", orderRequestDTO);
		
		String orderUuid = orderService.createOrder(orderRequestDTO);
				
		BaseResponse<String> response = new BaseResponse<>();
		response.setContent(orderUuid);
		response.setResponseCode(ResponseCode.OK);
		
		log.info("Create order successful, uuid: {}", orderUuid);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
	
	
	@RequestMapping(value = "/orders/recalculate/{orderUuid}", method = RequestMethod.GET)
	@ApiOperation(value = "Calculate order total amount", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order amount successfully calculated"),
            @ApiResponse(code = 400, message = "Input data validation failed"),
    		@ApiResponse(code = 404, message = "Order not found")
    })
    public ResponseEntity<BaseResponse<BigDecimal>> calculateOrderTotalAmount(@PathVariable(value="orderUuid") String orderUuid) {
		
		log.info("Called calculate order total amount, order uuid: {}", orderUuid);
		
		BigDecimal totalAmount = orderService.calculateOrderTotalAmount(orderUuid);
				
		BaseResponse<BigDecimal> response = new BaseResponse<>();
		response.setContent(totalAmount);
		response.setResponseCode(ResponseCode.OK);
		
		log.info("Calculate order total amount successful, total amount: {}", totalAmount);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
	
	
	@RequestMapping(value = "/orders/changestatus", method = RequestMethod.POST)
	@ApiOperation(value = "Change existing order status", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Change order status successful"),
            @ApiResponse(code = 400, message = "Input data validation failed"),
    		@ApiResponse(code = 404, message = "Order not found")
    })
    public ResponseEntity<BaseResponse<OrderStatus>> changeOrderStatus(@Valid @RequestBody ChangeOrderStatusRequestDTO changeOrderStatusRequestDTO) {
		
		OrderStatus newStatus = orderService.changeStatus(changeOrderStatusRequestDTO);
		
		BaseResponse<OrderStatus> changeOrderStatusResponse = new BaseResponse<>();
		changeOrderStatusResponse.setContent(newStatus);
		changeOrderStatusResponse.setResponseCode(ResponseCode.OK);
		
		log.info("Change order status successful, new status: {}", newStatus);
		
		return ResponseEntity.status(HttpStatus.OK).body(changeOrderStatusResponse);							
    }
	
	
	@RequestMapping(value = "/orders/all", method = RequestMethod.GET)
	@ApiOperation(value = "Get orders within period", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Orders successfully found"),
            @ApiResponse(code = 400, message = "Input data validation failed")
    })
	public ResponseEntity<BaseResponse<List<OrderResponseDTO>>> getOrdersWithinPeriod(@ZoneDateTimeValid @RequestParam(value="dateFrom") String dateFrom,
																				 @ZoneDateTimeValid @RequestParam(value="dateTo") String dateTo) {
		
		log.info("Called get orders from {} to {}", dateFrom, dateTo);
		
		List<OrderResponseDTO> orders = orderService.getOrdersWithinPeriod(dateFrom, dateTo);	
		
		BaseResponse<List<OrderResponseDTO>> response = new BaseResponse<>();		
		response.setResponseCode(ResponseCode.OK);
		if(orders.isEmpty())
			response.setResponseMessage("No orders in that period");
		else
			response.setContent(orders);
		
		log.info("Get orders within period successful, products: {}", orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}

}
