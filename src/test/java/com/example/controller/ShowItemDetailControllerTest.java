package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("商品詳細コントローラー")
class ShowItemDetailControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private ShowItemDetailController target;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * テスト 
	 */
	@Test
	@DisplayName("商品詳細画面表示")
	public void 商品詳細画面表示テスト()throws Exception {
		//テスト対象メソッド（showItemDetailメソッド）を実行
		mockMvc.perform(get("/showItem/toItemDetail").param("id", "1"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_detail"));
				
	}

}
