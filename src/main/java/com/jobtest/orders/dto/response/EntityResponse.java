package com.jobtest.orders.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EntityResponse extends BaseResponse {
       
	private  List<FieldMessage> responseFieldMessages;
	
}
