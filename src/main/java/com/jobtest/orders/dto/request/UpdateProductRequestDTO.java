package com.jobtest.orders.dto.request;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UpdateProductRequestDTO", description = "DTO object which is used for product update")
public class UpdateProductRequestDTO {
	
	@ApiModelProperty(value = "name", example = "Product 1", notes = "Name of product")
    private String name;
	
	@ApiModelProperty(value = "price", example = "25.25", notes = "Product price")
	private BigDecimal price;
}
