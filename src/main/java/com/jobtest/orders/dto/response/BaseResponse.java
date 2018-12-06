package com.jobtest.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(value = "BaseResponse", description = "Base response DTO which contains all relevant response informations")
public class BaseResponse<T> {
	
	private ResponseCode responseCode;
	
	private String responseMessage;
	
	private String errorMessage;	
	
	private T content;
}
