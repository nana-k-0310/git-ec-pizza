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
	
	/**
	 * ユーザーIDで注文情報を取得する.
	 * 
	 * @param userId　ユーザーID
	 * @return　検索された注文情報
	 */
	public Order getOrderByUserId(Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		return order;
	}
	
}
