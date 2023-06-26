package com.example.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.validation.Validator;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("商品一覧コントローラー")
class showItemListControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private showItemListController target;
	
	@Autowired
	private showItemListController showItemListController;
	
	@Autowired
	Validator validator;
	
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
	
	
	//ここから下のshowItemListメソッド（findAll）が不明点あり
	@Test
	@DisplayName("並び順指定なしで商品一覧画面を表示する")
	void id順商品一覧表示テスト()throws Exception {
		
		//テスト対象メソッド（showItemListメソッド）を実行
		mockMvc.perform(get("/"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
		
	}
	
	@Test
	@DisplayName("値段の高い順で商品一覧画面を表示する")
	void 高い順商品一覧表示テスト()throws Exception {
		
		//テスト対象メソッド（showItemListメソッド）を実行
		mockMvc.perform(get("/").param("order", "high"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
		
	}
	
	@Test
	@DisplayName("値段の安い順で商品一覧画面を表示する")
	void 安い順商品一覧表示テスト()throws Exception {
		
		//テスト対象メソッド（showItemListメソッド）を実行
		mockMvc.perform(get("/").param("order", "low"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
		
	}
	
	//名前一覧検索
	@Test
	@DisplayName("名前が空で並び順指定なしの名前の一覧検索をする")
	public void 名前が空で並び順指定なし名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "").param("order", ""))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("名前が空で値段の高い順の名前の一覧検索をする")
	public void 名前が空で値段の高い順名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "").param("order", "high"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("名前が空で値段の安い順の名前の一覧検索をする")
	public void 名前が空で値段の安い順名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "").param("order", "low"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("名前該当なしで並び順指定なしの名前の一覧検索をする")
	public void 名前が該当なしで並び順指定なし名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "ハワイアンパラダイス").param("order", ""))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("名前該当なしで高い順の名前の一覧検索をする")
	public void 名前が該当なしで高い順名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "ハワイアンパラダイス").param("order", "high"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("名前該当なしで安い順の名前の一覧検索をする")
	public void 名前が該当なしで安い順名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "ハワイアンパラダイス").param("order", "low"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
		.andExpect(model().attribute("result", "検索結果が0件の為、全件検索します"))
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("検索結果一件の名前の一覧検索をする")
	public void 検索結果一件の名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "明太バターチーズ").param("order", ""))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
	
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("検索結果複数件で並び順指定なしの名前の一覧検索をする")
	public void 検索結果複数件でid順の名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "バ").param("order", ""))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
	
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("検索結果複数件で高い順の名前の一覧検索をする")
	public void 検索結果複数件で高い順の名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "バ").param("order", "high"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
	
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
	@Test
	@DisplayName("検索結果複数件で安い順の名前の一覧検索をする")
	public void 検索結果複数件で安い順の名前一覧検索テスト() throws Exception {
		
		//テスト対象メソッド（findByNameメソッド）を実行
		mockMvc.perform(get("/findByName").param("name", "バ").param("order", "low"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//modelの中身が合っているか
	
		//次画面の遷移先がmaterialize-version/item_listであることを確認
		.andExpect(view().name("materialize-version/item_list"));
	
	}
	
}
