package com.jobtest.orders.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
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
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jobtest.orders.domain.Order;
import com.jobtest.orders.domain.Product;
import com.jobtest.orders.dto.request.ChangeOrderStatusRequestDTO;
import com.jobtest.orders.dto.request.OrderProductRequestDTO;
import com.jobtest.orders.dto.request.OrderRequestDTO;
import com.jobtest.orders.dto.response.OrderResponseDTO;
import com.jobtest.orders.repository.OrderProductRepository;
import com.jobtest.orders.repository.OrderRepository;
import com.jobtest.orders.repository.ProductRepository;
import com.jobtest.orders.type.OrderStatus;

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
public class OrderServiceTest {
	
	private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
	private OrderProductRepository orderProductRepository;
	
	@Test
	public void createOrderTest() {
		
		log.info("Testing create order");
		
		OrderRequestDTO orderRequestDTO = createOrderRequest();
		
		String orderUuid = orderService.createOrder(orderRequestDTO);
		Order order = orderRepository.findByUuid(orderUuid);
				
		assertThat(order.getBuyerEmail(), is(orderRequestDTO.getBuyerEmail()));
		assertThat(orderRequestDTO.getProductList().size(), is(3));
        assertThat(order.getOrderProducts().size(), is(orderRequestDTO.getProductList().size()));
        assertNotNull(order.getCreateTime());
        assertNotNull(order.getUuid());
        
        assertThat(order.getOrderProducts().get(0).getAmount(), is(orderRequestDTO.getProductList().get(0).getAmount()));
        assertThat(order.getOrderProducts().get(0).getProduct().getUuid(), is(orderRequestDTO.getProductList().get(0).getProductUuid()));
        assertThat(order.getOrderProducts().get(0).getOrderProductPrice(), is(order.getOrderProducts().get(0).getProduct().getPrice()));
        
        assertThat(order.getOrderProducts().get(1).getAmount(), is(orderRequestDTO.getProductList().get(1).getAmount()));
        assertThat(order.getOrderProducts().get(1).getProduct().getUuid(), is(orderRequestDTO.getProductList().get(1).getProductUuid()));
        assertThat(order.getOrderProducts().get(1).getOrderProductPrice(), is(order.getOrderProducts().get(1).getProduct().getPrice()));
        
        assertThat(order.getOrderProducts().get(2).getAmount(), is(orderRequestDTO.getProductList().get(2).getAmount()));
        assertThat(order.getOrderProducts().get(2).getProduct().getUuid(), is(orderRequestDTO.getProductList().get(2).getProductUuid()));
        assertThat(order.getOrderProducts().get(2).getOrderProductPrice(), is(order.getOrderProducts().get(2).getProduct().getPrice()));        		
	}
	
	
	@Test
	public void changeStatusTest() {
		
		log.info("Testing change order status");
		
		ChangeOrderStatusRequestDTO changeOrderStatusRequestDTO = new ChangeOrderStatusRequestDTO();
		changeOrderStatusRequestDTO.setOrderStatus("IN_PROGRESS");
		changeOrderStatusRequestDTO.setOrderUuid("2-0-0-2");;
		
		OrderStatus newStatus = orderService.changeStatus(changeOrderStatusRequestDTO);
		assertThat(newStatus, is(OrderStatus.IN_PROGRESS));
		
		Order order = orderRepository.findByUuid("2-0-0-2");
		assertThat(order.getStatus(), is(OrderStatus.IN_PROGRESS));		
	}
	
	
	@Test
	public void calculateOrderTotalAmountTest() {
		
		log.info("Testing calculate order total amount in state NEW");
				
		BigDecimal totalAmount = orderService.calculateOrderTotalAmount("2-0-0-1");
		assertThat(totalAmount, is(new BigDecimal("504.25")));
		
		Product product = productRepository.findByUuid("1-0-0-1");
		product.setPrice(new BigDecimal("20.00"));
		productRepository.saveAndFlush(product);
		
		totalAmount = orderService.calculateOrderTotalAmount("2-0-0-1");
		assertThat(totalAmount, is(new BigDecimal("516.25")));
		
				
		log.info("Testing calculate order total amount in state FINISHED");
		
		totalAmount = orderService.calculateOrderTotalAmount("2-0-0-3");
		assertThat(totalAmount, is(new BigDecimal("504.25")));
		
		Product product2 = productRepository.findByUuid("1-0-0-2");
		product2.setPrice(new BigDecimal("200.00"));
		productRepository.saveAndFlush(product2);
		
		totalAmount = orderService.calculateOrderTotalAmount("2-0-0-3");
		assertThat(totalAmount, is(new BigDecimal("504.25")));
	}
	
	
	@Test
	public void getOrdersWithinPeriodTest() {
		
		log.info("Testing get orders within period");
		
		List<OrderResponseDTO> orderDTOs = orderService.getOrdersWithinPeriod("2018-12-01 11:09:00", "2018-12-09 12:09:37");
		assertThat(orderDTOs.size(), is(2));
		
		orderDTOs = orderService.getOrdersWithinPeriod("2018-12-01 11:09:00", "2018-12-21 12:09:37");
		assertThat(orderDTOs.size(), is(3));
		
		orderDTOs = orderService.getOrdersWithinPeriod("2018-12-21 11:09:00", "2018-12-25 12:09:37");
		assertThat(orderDTOs.size(), is(0));
		
		orderDTOs = orderService.getOrdersWithinPeriod("2018-12-01 11:09:00", "2018-12-04 12:09:37");
		assertThat(orderDTOs.size(), is(1));
		
		Order order = orderRepository.findByUuid("2-0-0-1");
		
		assertThat(orderDTOs.get(0).getBuyerEmail(), is(order.getBuyerEmail()));
		assertThat(orderDTOs.get(0).getCreateTime(), is(order.getCreateTime()));
		assertThat(orderDTOs.get(0).getUuid(), is(order.getUuid()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(0).getAmount(), is(order.getOrderProducts().get(0).getAmount()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(0).getProductName(), is(order.getOrderProducts().get(0).getProduct().getName()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(0).getProductUuid(), is(order.getOrderProducts().get(0).getProduct().getUuid()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(0).getOrderProductPrice(), is(order.getOrderProducts().get(0).getOrderProductPrice()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(1).getAmount(), is(order.getOrderProducts().get(1).getAmount()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(1).getProductName(), is(order.getOrderProducts().get(1).getProduct().getName()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(1).getProductUuid(), is(order.getOrderProducts().get(1).getProduct().getUuid()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(1).getOrderProductPrice(), is(order.getOrderProducts().get(1).getOrderProductPrice()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(2).getAmount(), is(order.getOrderProducts().get(2).getAmount()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(2).getProductName(), is(order.getOrderProducts().get(2).getProduct().getName()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(2).getProductUuid(), is(order.getOrderProducts().get(2).getProduct().getUuid()));
		assertThat(orderDTOs.get(0).getOrderProducts().get(2).getOrderProductPrice(), is(order.getOrderProducts().get(2).getOrderProductPrice()));
		
	}
	
	
	private OrderRequestDTO createOrderRequest() {
				
		List<OrderProductRequestDTO> productList = new ArrayList<>();
		OrderProductRequestDTO product1 = new OrderProductRequestDTO();
		product1.setAmount(2);
		product1.setProductUuid("1-0-0-1");
		OrderProductRequestDTO product2 = new OrderProductRequestDTO();
		product2.setAmount(1);
		product2.setProductUuid("1-0-0-2");
		OrderProductRequestDTO product3 = new OrderProductRequestDTO();
		product3.setAmount(5);
		product3.setProductUuid("1-0-0-3");
		
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		
		OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
		orderRequestDTO.setBuyerEmail("xxx.xxx@gmail.com");
		orderRequestDTO.setProductList(productList);
		
		return orderRequestDTO;		
	}
	
	
	@After
	public void clear() {
		orderProductRepository.deleteAll();
        orderProductRepository.flush();
        orderRepository.deleteAll();
        orderRepository.flush();
	}

}
