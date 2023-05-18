package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SystemPropertyUtils;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;
import com.example.repository.ToppingRepository;

/**
 * 商品詳細情報を操作するサービス.
 * 
 * @author nanakono
 *
 */
@Service
@Transactional
public class ShowItemDetailService {

	@Autowired
	public ItemRepository itemRepository;
	
	@Autowired
	public ToppingRepository toppingRepository;
	
	@Autowired
	public OrderItemRepository orderItemRepository;
	
	@Autowired
	public OrderRepository orderRepository;
	
	@Autowired
	public OrderToppingRepository orderToppingRepository;
	
	/**
	 * 指定された商品の詳細情報をトッピングリストと合わせて取得する.
	 * 
	 * @param itemId　指定された商品ID
	 * @return　指定された商品詳細情報とトッピングリスト
	 */
	public Item showItemDetail(Integer id) {
		Item item = itemRepository.load(id);
		item.setToppingList(toppingRepository.findAll());
		return item;
	}
	
	/**
	 * カートにあり未注文のオーダー情報があれば返し、ない場合はnullを返す
	 * 
	 * @param insertUserId ユーザーID
	 * @return　未注文のカート情報、なければnull
	 */
	public Order findByUserIdAndStatus(Integer insertUserId) {
		Order nowOrderStatus = orderRepository.findByUserIdAndStatus(insertUserId, 0);
		return nowOrderStatus;
	}
	
	
	/** オーダーIDから商品リストを渡す */
	public List<OrderItem> itemListByOrderId(Integer orderId) {
		List<OrderItem> orderItemList = orderItemRepository.sameOrderLoad(orderId);
		for(OrderItem orderItem : orderItemList) {
			
			System.out.println("orderItemは" + orderItem + "である");
			
				Item item = itemRepository.load(orderItem.getItemId());
				
				orderItem.setItem(item);
				
				List<OrderTopping> orderToppingList = orderToppingRepository.findOrderItemId(orderItem.getId());
				
				orderItem.setOrderToppingList(orderToppingList);
				
			}
		
		System.out.println("ここまでは" + orderItemList + "です");
		
		return orderItemList;
		
		}
	
	/**　オーダートッピングのトッピングIDからトッピング情報を取得する */
	
	public Topping loadTopping(Integer toppingId) {
		Topping topping = toppingRepository.load(toppingId);
				return topping;
	}
	
	
	/**　注文商品の個数を更新する */
	public void updateCount(Integer newQuantity, Integer id) {
		orderItemRepository.updateCount(newQuantity, id);
	}
		
	}
	
	

