package com.jobtest.orders.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jobtest.orders.util.MoneySerializer;
import com.jobtest.orders.util.ZonedDateTimeConverter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ProductResponseDTO", description = "DTO object which is used for retrieving products")
public class ProductResponseDTO {
	
	@ApiModelProperty(value = "uuid", example = "1234-5678-1234-5678", notes = "UUID of product")
    private String uuid;
	
	@ApiModelProperty(value = "name", example = "Product 1", notes = "Name of product")
    private String name;
	
	@ApiModelProperty(value = "price", example = "25.25", notes = "Product price")
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal price;
	
	@ApiModelProperty(value = "createTime", example="17/08/2018 - 16:25:27", notes = "Product create time")
	@JsonSerialize(converter = ZonedDateTimeConverter.Serializer.class)
	private ZonedDateTime createTime;
}
