package com.example.controller;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.UserInfo;
import com.example.form.OrderForm;
import com.example.service.OrderConfirmService;
import com.example.service.OrderService;

import jakarta.servlet.http.HttpSession;

/**
 * 注文手順を操作するコントローラ.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/orderCon")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderConfirmService orderConfirmService;
	@Autowired
	private HttpSession session;
	
	/**
	 * 登録画面へ遷移する.
	 * 
	 * @param form　フォーム
	 * @param orderId　ID
	 * @param model　オーダー情報
	 * @return　注文画面へ遷移.
	 */
	@GetMapping("/toOrderConfirm")
	public String orderConfirm(OrderForm form, Model model) {
		UserInfo orderUser = (UserInfo)session.getAttribute("user");
		form.setDestinationName(orderUser.getName());
		form.setDestinationEmail(orderUser.getEmail());
		form.setDestinationZipcode(orderUser.getZipcode());
		form.setDestinationAddress(orderUser.getAddress());
		form.setDestinationTel(orderUser.getTelephone());
	
		Integer userId = orderUser.getId();
		
		Order orderList = orderConfirmService.getOrderByUserId(userId);
		
		model.addAttribute("order", orderList);
		
		return "/materialize-version/order_confirm";
		
	}
	
	
	
	/**
	 * 受け取った利用者情報の登録を行います.
	 * 
	 * @param form
	 * @param model
	 * @param userInfo
	 * @return
	 */
	@PostMapping("/order")
	public String order(@Validated OrderForm form, BindingResult result, Model model) {
		
		if(form.getDestinationEmail().equals("")) {
			result.rejectValue("destinationEmail", "", "メールアドレスを入力して下さい");
		}

		//必要な場合↓のコメント外す.
//		if(form.getDeliveryDate() == null) {
//			result.rejectValue("deliveryDate", null, "配達日を入力して下さい");
//		}
		
		if(result.hasErrors()) {
			return orderConfirm(form, model);
		}
		
		
		LocalDateTime nowLocalDateTime = LocalDateTime.now();
		nowLocalDateTime = nowLocalDateTime.plusHours(3);
		Timestamp after3TimeStamp = Timestamp.valueOf(nowLocalDateTime);
		
		try {
			final String yyyyMMddhh = form.getDeliveryDate() + "-" + form.getDeliveryTime();
			
			Date deliveryTime = (Date) new SimpleDateFormat("yyyy-MM-dd-hh").parse(yyyyMMddhh);
			
			Timestamp deliveryDateTimestamp = new Timestamp(deliveryTime.getTime());
			
			if(deliveryDateTimestamp.before(after3TimeStamp)) {
				
				result.rejectValue("deliveryDate", null, "今から3時間後の日時を入力して下さい");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(result.hasErrors()) {
			return orderConfirm(form, model);
		} 
		
		Order order = orderService.order(form);
		
		System.out.println(order + "注文完了");
		
		return "/materialize-version/order_finished";
	}

}
