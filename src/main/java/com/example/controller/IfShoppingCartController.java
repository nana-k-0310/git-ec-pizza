//package com.example.controller;

//import java.util.List;

//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;

//import com.example.common.ToppingId;
//import com.example.domain.OrderItem;
//import com.example.domain.OrderTopping;
//import com.example.domain.UserInfo;
//import com.example.form.LoginLogoutUserForm;
//import com.example.form.ShoppingCartForm;

//@Controller
//@RequestMapping("/ifShopping")
//public class IfShoppingCartController {

	/**
	 * 商品をカートに追加する.
	 * 
	 * @param form　ショッピングカートフォーム
	 * @param model　モデル
	 * @param userinfo ユーザー情報
	 * @return　toCartへリダイレクト
	 */
//	@PostMapping("/insertCart")
//	public String insertCart(ShoppingCartForm form, LoginLogoutUserForm userForm,  Model model, UserInfo userinfo) {
		
		/**ログイン時にセッションスコープでログイン情報を登録してあるので取り出す*/
//		UserInfo insertUser = (UserInfo)session.getAttribute("user");
		
		/**もしログインしていなかったらログイン画面へ遷移する*/
//		if(Objects.isNull(insertUser)) {
//			return goLogin(userForm);
//		}
		
		/**ユーザーのIDを取り出す*/
//		Integer insertUserId = insertUser.getId();
		
		/**ユーザーIDから同じユーザーでカートに入っている商品がないか確認する。あったらそのオーダーを返し、ない場合はnullを返す*/
//		Order nowOrderStatus = showItemDetailService.findByUserIdAndStatus(insertUserId);
		
		/**オーダー情報からIDを取り、そのオーダーを商品情報リストを返す*/
		/** isNullはnullの時に値を返し、そうでなければfalseを返す*/
		
//		if(!(Objects.isNull(nowOrderStatus)) && !(nowOrderStatus.getCalcTotalPrice() == 0)){
		
			/**オーダーのIDから商品リストを受け取る */
//			List<OrderItem> orderItemList = showItemDetailService.itemListByOrderId(nowOrderStatus.getId());

		
//				for(OrderItem orderItem : orderItemList) {
			
					/**もしトッピングがあればトッピングIDリストを作成する */
					
//					ToppingId toppingIdList = new ToppingId();
				
//					List<Integer> toppingIdInsertList = toppingIdList.addId();
					
					
//						for(OrderTopping orderTopping : orderItem.getOrderToppingList()) {
			
//							toppingIdInsertList.add(orderTopping.getToppingId());
				
//						}
						
						
//				System.out.println("formのトッピングリストは" + form.getToppingIdList());
//				System.out.println("前に登録してある同じ商品のトッピングリストは" + toppingIdInsertList);
//				System.out.println("今回の注文商品のトッピングリストと前の登録商品のトッピングりすとは同じか？同じ→" + form.getToppingIdList().equals(toppingIdInsertList));
			
//				if(form.getItemId().equals(orderItem.getItemId()) && form.getSize().equals(orderItem.getSize()) && form.getToppingIdList().equals(toppingIdInsertList)) {
				
//				orderItem.setQuantity(orderItem.getQuantity() + form.getQuantity());
//				showItemDetailService.updateCount(orderItem.getQuantity(), orderItem.getId());
				
//				return "redirect:/shopping/toCart";
//				}
		
//		}
				
//	} //*←以前0円でないオーダーがあるときのifの括弧*//
		
		/**　重なる商品がない場合はインサートする */
		
//		shoppingCartService.insertCart(form, insertUser.getId());
		
//		return "redirect:/shopping/toCart";
//	}

	
	
	
//}
