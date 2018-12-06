package com.jobtest.orders.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.jobtest.orders.domain.Product;
import com.jobtest.orders.dto.request.AddProductRequestDTO;
import com.jobtest.orders.dto.request.UpdateProductRequestDTO;
import com.jobtest.orders.dto.response.ProductResponseDTO;
import com.jobtest.orders.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup(value = { "/testData/ordersTestData.xml" })
@DatabaseTearDown(value = { "/testData/ordersTestData.xml" }, type=DatabaseOperation.DELETE_ALL)
@ActiveProfiles("test")
@Transactional
public class ProductServiceTest {
		
	private static final Logger log = LoggerFactory.getLogger(ProductServiceTest.class);
	
	@Autowired
    private ProductService productService;
	
	@Autowired
    private ProductRepository productRepository;
	
	@Test
	public void addNewProductTest() {
		
		log.info("Testing add new product");
		
		AddProductRequestDTO addProductRequestDTO = new AddProductRequestDTO();
		addProductRequestDTO.setName("Product 1");
		addProductRequestDTO.setPrice(new BigDecimal("100"));
		
		String productUuid = productService.addNewProduct(addProductRequestDTO);				
		Product product = productRepository.findByUuid(productUuid);
        
        assertThat(product.getName(), is(addProductRequestDTO.getName()));
        assertThat(product.getPrice(), is(addProductRequestDTO.getPrice()));
        assertNotNull(product.getCreateTime());
        assertNotNull(product.getUuid());
		
	}
	
	
	@Test
	public void updateProductTest() {
		
		log.info("Testing update existing product");		
						
		Product product = productRepository.findByUuid("1-0-0-1");
		
		assertThat(product.getName(), is("Product 4"));
        assertThat(product.getPrice(), is(new BigDecimal("14.00")));
        
		UpdateProductRequestDTO updateProductRequestDTO = new UpdateProductRequestDTO();
		updateProductRequestDTO.setName("Product 11");
		updateProductRequestDTO.setPrice(new BigDecimal("1000.25"));
						
		productService.updateProduct(product.getUuid(), updateProductRequestDTO);
		
		product = productRepository.findByUuid("1-0-0-1");
		
		assertThat(product.getName(), is("Product 11"));
        assertThat(product.getPrice(), is(new BigDecimal("1000.25")));
        assertNotNull(product.getUpdateTime());
        		
	}
	
	
	@Test
	public void getAllProductsTest() {
		
		log.info("Testting get all products");		
						
		List<ProductResponseDTO> products = productService.getAllProducts();
		
		assertThat(products.get(0).getName(), is("Product 1"));
        assertThat(products.get(0).getPrice(), is(new BigDecimal("4.00")));
        
        assertThat(products.get(1).getName(), is("Product 2"));
        assertThat(products.get(1).getPrice(), is(new BigDecimal("152.00")));
        
        assertThat(products.get(2).getName(), is("Product 3"));
        assertThat(products.get(2).getPrice(), is(new BigDecimal("20.25")));
        
        assertThat(products.get(3).getName(), is("Product 4"));
        assertThat(products.get(3).getPrice(), is(new BigDecimal("14.00")));
        		
	}

}
