package com.jobtest.orders.dto.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderProductRequestDTO", description = "DTO object which is used for adding order products")
public class OrderProductRequestDTO {
	
	@ApiModelProperty(value = "amount", example = "3", notes = "Order product amount", required = true)
	@NotNull(message="Amount is mandatory")
	private Integer amount;
	
	@ApiModelProperty(value = "productUuid", example = "1-2-3-4", notes = "Order product uuid", required = true)
	@NotNull(message="Product uuid is mandatory")
	private String productUuid;

}
