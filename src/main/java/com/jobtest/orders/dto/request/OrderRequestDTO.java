package com.jobtest.orders.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderRequestDTO", description = "DTO object which is used for creating order")
public class OrderRequestDTO {
	
	@ApiModelProperty(value = "buyerEmail", example = "xxx.xxx@gmail.com", notes = "Buyer's email address", required = true)
	@NotNull(message="Buyer email is mandatory")
	@Email
	private String buyerEmail;
	
	@ApiModelProperty(value = "productList", notes = "List of order products", required = true)
	@Valid
	@NotNull(message="Product list should contain at least one product")
	private List<OrderProductRequestDTO> productList;
	
}

	
