package com.jobtest.orders.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends BaseEntity {

	private static final long serialVersionUID = -8838000986916870470L;
	
	@Column
	private ZonedDateTime createTime;
	
	@Column
	private ZonedDateTime updateTime;
	
	@Column(nullable = false, unique = true)
	private String uuid;
		
	@Column(nullable = false)
    private String name;
	
	@Column
	private BigDecimal price;
		    
}
