package com.jobtest.orders.dto.response;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldMessage implements Comparable<Object> {
	
	private String field;
		
    private String message;
    	
	@Override
	public int compareTo(Object anotherFieldMessage) {
	    
	    return Comparator.comparing(FieldMessage::getField)
	              .thenComparing(FieldMessage::getMessage)
	              .compare(this, (FieldMessage)anotherFieldMessage);
	}
	
}
