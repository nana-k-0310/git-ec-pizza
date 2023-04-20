package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.OrderRepository;

/**
 * 注文履歴表示の業務処理をするサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class OrderHistoryService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * ユーザーIDで注文情報を取得する.
	 * 
	 * @param userId　ユーザーID
	 * @return　検索された注文情報
	 */
	public List<Order> showOrderHistory(Integer userId) {
		List<Order> orderList = orderRepository.findHistoryByUserIdAndStatus(userId);
		return orderList;
	}
}
