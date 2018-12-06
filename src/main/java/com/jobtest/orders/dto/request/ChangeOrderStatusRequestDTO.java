package com.jobtest.orders.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ChangeOrderStatusRequestDTO", description = "DTO object which is used for changing order status")
public class ChangeOrderStatusRequestDTO {
	
	@ApiModelProperty(value = "orderUuid", example = "1-2-3-4", notes = "Order uuid", required = true)
	@NotNull(message="Order UUID is mandatory")
	private String orderUuid;
	
	@ApiModelProperty(value = "orderStatus", example = "IN_PROGRESS", notes = "Order product new status", required = true)
	@NotNull(message="Order status is mandatory")
	@Pattern(regexp = "NEW|IN_PROGRESS|FINISHED|CANCELED", message="Order status should be one of following: NEW|IN_PROGRESS|FINISHED|CANCELED")
	private String orderStatus;

}
