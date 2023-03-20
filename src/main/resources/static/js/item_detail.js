"use strict";
$(function(){
	calc_price();
	$(".size").on("change", function(){
		calc_price();
	});
	
	$(".checkbox").on("click", function(){
		calc_price();
	});
	
	$("#pizanum").on("change", function(){
		calc_price();
	});
	
	//値段の計算をして変更する関数
	function calc_price(){
		console.log('ファンクションが反応しています')
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
			topping_price = 300 * topping_count;
		}
		
		let removecomma = size_price.replace(/,/g, "");
		var num = parseInt(removecomma, 10);
		let price = (num + topping_price) * piza_num;
		$("#totalprice").text(price.toLocaleString());
	}
	
	//オートコンプリート機能が画面周辺をクリック時にも候補が出てくるようにする
	$(function(){
		$(".seartch-form").on("click", function(){
			const button = document.querySelector("#search")
			button.focus();
		});
		
	});
	
});

