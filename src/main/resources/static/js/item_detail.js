"use strict";
$(function(){
	calc_price();
	$(".size").on("change", function(){
		calc_price();
	});
	
	
	
	
	
	//値段の計算をして変更する関数
	function calc_price(){
		let size = $(".size:checked").val();
		let topping_count = $(".checkbox:checked").length;
		let piza_num = $("#pizanum").val();
		let size_price = 0;
		let topping_price = 0;
		if(size === "M"){
			size_price = $("#sizeMPrice").text();
			topping_price = 200 * topping_count;
		} else {
			size_price = $("#sizeLPrice").text();
			tropping_price = 300 * topping_count;
		}
		
		//ここにカンマの記述が入る
		
		let removecomma = size_price.replace(/,/g, "");
		var num = parseInt(removecomma, 10);
		
		
	}
});















//値段の計算をして変更する関数
//function calc price(){
	let size = $(".size:checked").val();
	let topping_count = $(".checkbox:checked").length;
	let piza_num = $()
//}