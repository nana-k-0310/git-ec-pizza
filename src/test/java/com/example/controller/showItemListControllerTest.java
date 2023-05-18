package com.example.controller;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("商品一覧コントローラー")
class showItemListControllerTest {

	@Autowired
	private showItemListController showItemListController;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("商品一覧画面表示")
	void 商品一覧表示テスト()throws Exception {
		showItemListController.showItemList(null, null);
		
		//Sysoutでrequestスコープのmodel（この場合itemList）を表示する場合
		System.out.println("itemList");
	}

}
