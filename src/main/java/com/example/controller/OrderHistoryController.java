package com.example.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
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
	public String showOrderHistory(LoginLogoutUserForm userForm, Model model) {
		UserInfo historyUser = (UserInfo) session.getAttribute("user");
		
		if(Objects.isNull(historyUser)) {
			return goLogin(userForm);
		}
		
		Integer userId = historyUser.getId();
		
		List<Order> orderList = orderHistoryService.showOrderHistory(userId);
		
		model.addAttribute("orderList", orderList);
		
		return "materialize-version/order_history";
	}
	
	
	/**
	 * ユーザー情報がない場合、ログイン画面に遷移する.
	 * 
	 * @param userForm ユーザーフォーム
	 * @return　ログイン画面
	 */
	@GetMapping("/goLogin")
	public String goLogin(LoginLogoutUserForm userForm) {
		return "materialize-version/login";
	}

}
