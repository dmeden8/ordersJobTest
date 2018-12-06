package com.jobtest.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import com.jobtest.orders.domain.Product;
import com.jobtest.orders.dto.request.AddProductRequestDTO;
import com.jobtest.orders.dto.request.UpdateProductRequestDTO;
import com.jobtest.orders.dto.response.ProductResponseDTO;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductMapper {
	
	Product dtoToEntity(AddProductRequestDTO dto);

	Product merge(UpdateProductRequestDTO dto, @MappingTarget Product product);
	
	ProductResponseDTO entityToDto(Product product);
}
