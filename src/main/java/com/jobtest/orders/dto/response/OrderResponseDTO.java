package com.jobtest.orders.dto.response;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jobtest.orders.type.OrderStatus;
import com.jobtest.orders.util.ZonedDateTimeConverter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderResponseDTO", description = "DTO object which is used for retrieving orders")
public class OrderResponseDTO {
	
	@ApiModelProperty(value = "uuid", example = "1234-5678-1234-5678", notes = "UUID of product")
    private String uuid;
	
	@ApiModelProperty(value = "buyerEmail", example = "xxx.xxx@gmail.com", notes = "Buyer's email")
    private String buyerEmail;
	
	@ApiModelProperty(value = "createTime", example="17/08/2018 - 16:25:27", notes = "Order create time")
	@JsonSerialize(converter = ZonedDateTimeConverter.Serializer.class)
	private ZonedDateTime createTime;
	
	@ApiModelProperty(value = "status", example="NEW", notes = "Order status")
	private OrderStatus status;
	
	@ApiModelProperty(value = "orderProducts", notes = "List of order products")
	List<OrderProductResponseDTO> orderProducts;
			
}
