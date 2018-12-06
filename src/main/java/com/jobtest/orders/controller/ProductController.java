package com.jobtest.orders.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jobtest.orders.dto.request.AddProductRequestDTO;
import com.jobtest.orders.dto.request.UpdateProductRequestDTO;
import com.jobtest.orders.dto.response.BaseResponse;
import com.jobtest.orders.dto.response.ProductResponseDTO;
import com.jobtest.orders.dto.response.ResponseCode;
import com.jobtest.orders.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@Validated
@RequestMapping("/api")
@Api(value="/api/products", description="Operations related to products")
public class ProductController {
	
	private final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
    private ProductService productService;
	
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	@ApiOperation(value = "Add new product", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product successfully created"),
            @ApiResponse(code = 400, message = "Input data validation failed")
    })
	public ResponseEntity<BaseResponse<String>> addNewProduct(@Valid @RequestBody AddProductRequestDTO productRequestDTO) {
		
		log.info("Called add new product: {}", productRequestDTO);
		
		String productUuid = productService.addNewProduct(productRequestDTO);
				
		BaseResponse<String> response = new BaseResponse<>();
		response.setContent(productUuid);
		response.setResponseCode(ResponseCode.OK);
		
		log.info("Add new product successful, uuid: {}", productUuid);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
	
	
	@RequestMapping(value = "/products/{productUuid}", method = RequestMethod.POST)
	@ApiOperation(value = "Update existing product", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product successfully updated"),
            @ApiResponse(code = 400, message = "Input data validation failed"),
            @ApiResponse(code = 404, message = "Product not found")
    })
	public ResponseEntity<BaseResponse<Object>> updateProduct(@PathVariable(value="productUuid") String productUuid,
															  @Valid @RequestBody UpdateProductRequestDTO productRequestDTO) {
		
		log.info("Called update existing product with uuid {}: {}", productUuid, productRequestDTO);
		
		BaseResponse<Object> response = productService.updateProduct(productUuid, productRequestDTO);		
		
		log.info("Update existing product successful, uuid: {}", productUuid);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
	
	
	@RequestMapping(value = "/products/all", method = RequestMethod.GET)
	@ApiOperation(value = "Get all products", response = BaseResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products successfully found"),
            @ApiResponse(code = 400, message = "Input data validation failed")
    })
	public ResponseEntity<BaseResponse<List<ProductResponseDTO>>> getAllProducts() {
		
		log.info("Called get all products");
		
		List<ProductResponseDTO> products = productService.getAllProducts();	
		
		BaseResponse<List<ProductResponseDTO>> response = new BaseResponse<>();		
		response.setResponseCode(ResponseCode.OK);
		if(products.isEmpty())
			response.setResponseMessage("No products available");
		else
			response.setContent(products);
		
		log.info("Get all products successful, products: {}", products);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}

}
