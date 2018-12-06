package com.jobtest.orders.domain;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jobtest.orders.type.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
	
	private static final long serialVersionUID = 7169930941801893097L;
	
	@Column
	private ZonedDateTime createTime;
	
	@Column
	private ZonedDateTime updateTime;
	
	@Column(nullable = false, unique = true)
	private String uuid;
		
	@Enumerated(EnumType.STRING)
	@Column
    private OrderStatus status;
	
	@Column(nullable = false)
	private String buyerEmail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderProduct> orderProducts;
	
}
