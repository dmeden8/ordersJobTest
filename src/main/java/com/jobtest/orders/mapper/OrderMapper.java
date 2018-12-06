package com.jobtest.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.jobtest.orders.domain.Order;
import com.jobtest.orders.dto.response.OrderResponseDTO;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderMapper {

	OrderResponseDTO entityToDto(Order order);
	
}
