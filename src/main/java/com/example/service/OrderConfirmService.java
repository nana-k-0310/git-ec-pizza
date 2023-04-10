package com.example.service;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

@Service
@Temperature
public class OrderConfirmService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order getUserId(Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		return order;
	}
	
}
