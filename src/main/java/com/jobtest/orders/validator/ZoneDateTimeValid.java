package com.jobtest.orders.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ZoneDateTimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ZoneDateTimeValid {
	
	String message() default "Date is not in right format, it should be: 'yyyy-MM-dd HH:mm:ss'";
    
    Class<?>[] groups() default {};
     
    Class<? extends Payload>[] payload() default {};

}
