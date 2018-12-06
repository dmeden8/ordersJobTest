package com.jobtest.orders.validator;

import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ZoneDateTimeValidator implements ConstraintValidator<ZoneDateTimeValid, String> {
    private DateTimeFormatter dateTimeFormatter;
    
    String pattern = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void initialize(ZoneDateTimeValid arg0) {
		dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);		
	}

	@Override
	public boolean isValid(String dateText, ConstraintValidatorContext ctx) {
				
		if(StringUtils.isEmpty(dateText)){
	       return true;
		}
		try {
			dateTimeFormatter.parse(dateText);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	

}
