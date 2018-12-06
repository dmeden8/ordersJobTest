package com.jobtest.orders.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jobtest.orders.util.MoneySerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "OrderProductResponseDTO", description = "OrderProductResponseDTO")
public class OrderProductResponseDTO {
	
	@ApiModelProperty(value = "amount", example="2", notes = "Order product amount")
    private Integer amount;
    
	@ApiModelProperty(value = "productName", example="Product 1", notes = "Order product name")
    private String productName;
	
	@ApiModelProperty(value = "productUuid", example="1234-5678-1234-5678", notes = "Order product uuid")
    private String productUuid;
	
	@ApiModelProperty(value = "orderProductPrice", example="20.23", notes = "Order product price")
	@JsonSerialize(using = MoneySerializer.class)
    private BigDecimal orderProductPrice;

}
