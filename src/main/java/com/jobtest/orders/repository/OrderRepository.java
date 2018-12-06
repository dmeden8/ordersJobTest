package com.jobtest.orders.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobtest.orders.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Order findByUuid(String uuid);
	
	List<Order> findByCreateTimeGreaterThanAndCreateTimeLessThanOrderByCreateTime(ZonedDateTime dateFrom, ZonedDateTime dateTo);
					
}
