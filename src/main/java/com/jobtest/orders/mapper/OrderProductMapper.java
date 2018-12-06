package com.jobtest.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import com.jobtest.orders.domain.OrderProduct;
import com.jobtest.orders.dto.response.OrderProductResponseDTO;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderProductMapper {
	
	@Mappings({
		@Mapping(source = "product.uuid", target = "productUuid"),
		@Mapping(source = "product.name", target = "productName")
	})
	OrderProductResponseDTO entityToDto(OrderProduct orderProduct);

}
