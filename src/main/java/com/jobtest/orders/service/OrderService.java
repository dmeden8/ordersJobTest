package com.jobtest.orders.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jobtest.orders.domain.Order;
import com.jobtest.orders.domain.OrderProduct;
import com.jobtest.orders.domain.Product;
import com.jobtest.orders.dto.request.ChangeOrderStatusRequestDTO;
import com.jobtest.orders.dto.request.OrderProductRequestDTO;
import com.jobtest.orders.dto.request.OrderRequestDTO;
import com.jobtest.orders.dto.response.OrderProductResponseDTO;
import com.jobtest.orders.dto.response.OrderResponseDTO;
import com.jobtest.orders.error.OrdersException;
import com.jobtest.orders.mapper.OrderMapper;
import com.jobtest.orders.mapper.OrderProductMapper;
import com.jobtest.orders.repository.OrderProductRepository;
import com.jobtest.orders.repository.OrderRepository;
import com.jobtest.orders.repository.ProductRepository;
import com.jobtest.orders.type.OrderStatus;

@Component
@Transactional
public class OrderService {
	
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
	private OrderProductRepository orderProductRepository;
	
	@Autowired
    private OrderMapper orderMapper;
	
	@Autowired
    private OrderProductMapper orderProductMapper;
	
	String pattern = "yyyy-MM-dd HH:mm:ss";
	
	public String createOrder(OrderRequestDTO orderRequestDTO) {
						
		Order order = new Order();				
		order.setCreateTime(ZonedDateTime.now());	
		order.setStatus(OrderStatus.NEW);
		order.setUuid(UUID.randomUUID().toString());
		order.setBuyerEmail(orderRequestDTO.getBuyerEmail());
		order = orderRepository.saveAndFlush(order);
						
		List<OrderProduct> orderProducts = new ArrayList<>();
		
		for (OrderProductRequestDTO orderProductRequestDTO : orderRequestDTO.getProductList()) {
			
			OrderProduct orderProduct = new OrderProduct();			
			Product product = findProduct(orderProductRequestDTO.getProductUuid());		
						
			orderProduct.setProduct(product);
			orderProduct.setAmount(orderProductRequestDTO.getAmount());
			orderProduct.setOrderProductPrice(product.getPrice());
						
			orderProduct = orderProductRepository.saveAndFlush(orderProduct);
			orderProduct.setOrder(order);
			orderProducts.add(orderProduct);
		}
				
		order.setOrderProducts(orderProducts);
		order = orderRepository.saveAndFlush(order);
				
		return order.getUuid();		
	}
	
	
	public OrderStatus changeStatus(ChangeOrderStatusRequestDTO changeOrderStatusRequestDTO) {
				
		Order order = findOrder(changeOrderStatusRequestDTO.getOrderUuid());	
	    		
		order.setStatus(OrderStatus.valueOf(changeOrderStatusRequestDTO.getOrderStatus()));
		orderRepository.saveAndFlush(order);
				
		return order.getStatus();	    	
    }
	
		
	public BigDecimal calculateOrderTotalAmount(String orderUuid) {
		
		Order order = findOrder(orderUuid);	
		BigDecimal totalPrice = new BigDecimal(0);
		
		if(order.getStatus() == OrderStatus.NEW) {			
			for(OrderProduct orderProduct : order.getOrderProducts()) {
				orderProduct.setOrderProductPrice(orderProduct.getProduct().getPrice());
				totalPrice = totalPrice.add(orderProduct.getProduct().getPrice().multiply(new BigDecimal(orderProduct.getAmount())));
			}			
		}
		else if (order.getStatus() == OrderStatus.IN_PROGRESS || order.getStatus() == OrderStatus.FINISHED) {
			for(OrderProduct orderProduct : order.getOrderProducts()) {
				totalPrice = totalPrice.add(orderProduct.getOrderProductPrice().multiply(new BigDecimal(orderProduct.getAmount())));
			}
		}	
		
		return totalPrice;
	}
	
	
	public List<OrderResponseDTO> getOrdersWithinPeriod(String dateFrom, String dateTo) {
		
		List<OrderResponseDTO> orderDTOs = new ArrayList<>();		
		List<Order> orders = orderRepository.findByCreateTimeGreaterThanAndCreateTimeLessThanOrderByCreateTime(convertDate(dateFrom), convertDate(dateTo));
		
		for (Order order : orders) {			
			OrderResponseDTO orderDTO = orderMapper.entityToDto(order);	
			List<OrderProductResponseDTO> orderProductDTOs = new ArrayList<>();	
			for(OrderProduct orderProduct : order.getOrderProducts()) {
				OrderProductResponseDTO orderProductDTO = orderProductMapper.entityToDto(orderProduct);
				orderProductDTOs.add(orderProductDTO);
			}
			orderDTO.setOrderProducts(orderProductDTOs);
			orderDTOs.add(orderDTO);
		}
		
		return orderDTOs;
	}
	
	
	private Product findProduct(String productUuid) {   		
		Product product = productRepository.findByUuid(productUuid);		
		if(product == null)
			throw new OrdersException("Product does not exist for provided product uuid: " + productUuid); 		
   		return product;    	
    }
	
	private Order findOrder(String orderUuid) {   		
		Order order = orderRepository.findByUuid(orderUuid);		
		if(order == null)
			throw new OrdersException("Order does not exist for provided order uuid: " + orderUuid); 		
   		return order;    	
    }
	
	private ZonedDateTime convertDate(String dateText) {
		DateTimeFormatter parser = DateTimeFormatter.ofPattern(pattern);
		ZoneId timeZone = ZoneId.systemDefault();
        ZonedDateTime date = LocalDateTime.parse(dateText, parser).atZone(timeZone);
        return date;        
	}

}
