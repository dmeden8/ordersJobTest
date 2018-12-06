package com.jobtest.orders.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jobtest.orders.domain.Product;
import com.jobtest.orders.dto.request.AddProductRequestDTO;
import com.jobtest.orders.dto.request.UpdateProductRequestDTO;
import com.jobtest.orders.dto.response.BaseResponse;
import com.jobtest.orders.dto.response.ProductResponseDTO;
import com.jobtest.orders.dto.response.ResponseCode;
import com.jobtest.orders.error.OrdersException;
import com.jobtest.orders.mapper.ProductMapper;
import com.jobtest.orders.repository.ProductRepository;

@Component
@Transactional
public class ProductService {
	
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private ProductMapper productMapper;
	
	public String addNewProduct(AddProductRequestDTO productRequestDTO) {
								
		Product product = productMapper.dtoToEntity(productRequestDTO);
		product.setUuid(UUID.randomUUID().toString());
		product.setCreateTime(ZonedDateTime.now());
		product = productRepository.saveAndFlush(product);
		
        return product.getUuid();
	}
	
	
	public BaseResponse<Object> updateProduct(String productUuid, UpdateProductRequestDTO productRequestDTO) {		
		
		Product product = findProduct(productUuid);
		
		product = productMapper.merge(productRequestDTO, product);
		product.setUpdateTime(ZonedDateTime.now());
		product = productRepository.saveAndFlush(product);
		
		return successResponse();
	}
	
	
	public List<ProductResponseDTO> getAllProducts() {
					
		List<ProductResponseDTO> productDTOs = new ArrayList<>();		
		List<Product> products = productRepository.findAllByOrderByName();
		
		for (Product product : products) {			
			ProductResponseDTO productDTO = productMapper.entityToDto(product);															
			productDTOs.add(productDTO);																				
		}
		
		return productDTOs;
	}
	
	
	private Product findProduct(String productUuid) {   		
		Product product = productRepository.findByUuid(productUuid);		
		if(product == null)
			throw new OrdersException("Product does not exist for provided product uuid: " + productUuid); 		
   		return product;    	
    }
	
	
	private BaseResponse<Object> successResponse() {
		BaseResponse<Object> baseResponse = new BaseResponse<>();
    	baseResponse.setResponseCode(ResponseCode.OK);
    	return baseResponse;
	}
	
}
