package com.jobtest.orders.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AddProductRequestDTO", description = "DTO object which is used for adding product")
public class AddProductRequestDTO {
	
	@ApiModelProperty(value = "name", example = "Product 1", notes = "Name of product", required = true)
	@NotNull(message="Name of product is mandatory")
    private String name;
	
	@ApiModelProperty(value = "price", example = "25.25", notes = "Product price", required = true)
	@NotNull(message="Product price is mandatory")
	private BigDecimal price;
}
