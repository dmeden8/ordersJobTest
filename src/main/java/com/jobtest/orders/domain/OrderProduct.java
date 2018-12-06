package com.jobtest.orders.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_product")
@Getter
@Setter
public class OrderProduct extends BaseEntity {

	private static final long serialVersionUID = 6309372553043409134L;
		
	@Column
    private Integer amount;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
		
	@OneToOne(fetch = FetchType.LAZY)
	private Product product;
	
	@Column
	private BigDecimal orderProductPrice;

}
