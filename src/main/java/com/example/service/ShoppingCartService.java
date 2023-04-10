package com.example.service;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.ShoppingCartForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * カート関連サービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class ShoppingCartService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	
	/**
	 * 注文情報インサート業務処理
	 * 
	 * @param form　ショッピングカートフォーム
	 * @param userId　ユーザーID
	 */
	public void insertCart(ShoppingCartForm form, Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		Order orderObject = new Order();
		//オーダーテーブルにユーザー情報がない場合注文テーブルにインサート
		if(order == null) {
			orderObject.setUserId(userId);
			orderObject.setStatus(0);
			orderObject.setTotalPrice(0);
			//↑なぜtotalが0？
			orderRepository.insert(orderObject);
		}
		//注文商品へインサート
		OrderItem orderItem = new OrderItem();
		//ショッピングカートのフォームをコピーしorderItemに移す（Formで入力したものがorderItemに入る）
		BeanUtils.copyProperties(form, orderItem);
		
		if(order != null) {
			orderItem.setOrderId(order.getId());
		} else {
			orderItem.setOrderId(orderObject.getId());
		}
		OrderItem orderItemInfo = orderItemRepository.insert(orderItem);
		
		//注文トッピングインサート、nullの時はインサートなし
		OrderTopping orderTopping = new OrderTopping();
		
		//ToppingIdListはショッピングカートFormにある
		if(form.getToppingIdList() != null) {
			for(Integer t : form.getToppingIdList()) {
				orderTopping.setToppingId(t);
				orderTopping.setOrderItemId(orderItemInfo.getId());
				orderToppingRepository.insert(orderTopping);
			}
		}
		
	}

	/**
	 * カートに中身表示サービス.
	 * 
	 * @param userId　ユーザーID
	 * @return　注文オーダー情報
	 */
	public Order showCart(Integer userId) {
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		//注文テーブルにインサートなしでカートの中身をみた時
		Order orderObject = new Order();
		if(Objects.isNull(order)) {
			orderObject.setUserId(userId);
			orderObject.setStatus(0);
			orderObject.setTotalPrice(0);
			orderRepository.insert(orderObject);
		}
		return orderRepository.findByUserIdAndStatus(userId, 0);
	}
	
	/**
	 * 注文商品削除.
	 * 
	 * @param orderItemId　注文商品ID
	 */
	public void deleteByOrderId(Integer orderItemId) {
		orderItemRepository.deleteByOrderId(orderItemId);
	}
	
	
	/**
	 * 商品を一括削除する.
	 * 
	 * @param orderItemOrderId　オーダーアイテムID
	 */
	public void allDeleteOrderItem(Integer orderItemOrderId) {
		orderItemRepository.allDeleteOrderItem(orderItemOrderId);
	}
	
}
