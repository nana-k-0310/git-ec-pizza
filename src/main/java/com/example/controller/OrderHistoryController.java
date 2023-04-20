package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.service.OrderHistoryService;

import jakarta.servlet.http.HttpSession;

/**
 * 注文履歴画面表示の機能の処理を行うコントローラ.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/orderHistory")
public class OrderHistoryController {
	
	@Autowired
	private OrderHistoryService orderHistoryService;
	@Autowired
	private HttpSession session;
	
	/** 注文履歴画面を表示する*/
	@GetMapping("")
	public String showOrderHistory(Model model) {
		UserInfo historyUser = (UserInfo) session.getAttribute("user");
		
		Integer userId = historyUser.getId();
		
		List<Order> orderList = orderHistoryService.showOrderHistory(userId);
		
		System.out.println("orderListは" + orderList + "です");
		
		model.addAttribute("orderList", orderList);
		
		return "materialize-version/order_history";
	}

}
