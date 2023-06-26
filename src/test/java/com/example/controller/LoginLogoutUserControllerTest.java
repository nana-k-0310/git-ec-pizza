package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.example.domain.UserInfo;
import com.example.form.LoginLogoutUserForm;
import com.example.service.LoginLogoutUserService;

import ch.qos.logback.core.model.Model;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ログイン機能コントローラー")
class LoginLogoutUserControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private LoginLogoutUserController target;
	
	@Autowired
	private LoginLogoutUserController loginLogoutUserController;
	
	@Autowired
	private LoginLogoutUserService loginLogoutUserService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	Validator validator;
	
	/**
	 * テスト 
	 */
	
	@Test
	@DisplayName("ログイン画面を表示する")
	public void ログイン画面表示テスト() throws Exception {
		
		//テスト対象メソッド（registerメソッド）を実行
		mockMvc.perform(get("/login"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
	
	}
	
	@Test
	@DisplayName("DBに登録済ユーザーのログインする")
	public void DB登録済ユーザーログインテスト() throws Exception {
		
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		
		form.setEmail("abc@gmail.com");
		form.setPassword("aaaaaaaa");
		
		//テスト対象メソッド（registerメソッド）を実行
		mockMvc.perform(post("/login/toLogin").flashAttr("loginLogoutUserForm",form))
		//HTTPステータスがOKであることを確認（リダイレクトがあるときは「redirectedUrl("/sample/openBoardList.do")」）
		.andExpect(redirectedUrl("/"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("redirect:/"));
		
	}
	
	@Test
	@DisplayName("DBに登録未済ユーザーのログインする")
	public void DB登録未済ユーザーログインテスト() throws Exception {
		
		LoginLogoutUserForm form = new LoginLogoutUserForm();
		
		form.setEmail("kono@gmail.com");
		form.setPassword("bbbbbbbb");
		
		//テスト対象メソッド（registerメソッド）を実行
	    mockMvc.perform(post("/login/toLogin").flashAttr("loginLogoutUserForm",form))
		//HTTPステータスがOKであることを確認（リダイレクトがあるときは「redirectedUrl("/sample/openBoardList.do")」）
		.andExpect(status().isOk())	
		//modelの中身が合っているか
		.andExpect(model().attribute("errorMessage", "メールまたはパスワードが間違っています"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
	}
	
	@Test
	@DisplayName("ログアウトする")
	public void ログアウトテスト() throws Exception{
		
		
		//テスト用の仮のログイン状態のセッション(仮ログイン用userを作成)
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("user", new User());
		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login/toLogout").session(session);
		
		//テスト対象メソッド（toLogoutメソッド）を実行
		mockMvc.perform(requestBuilder)
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//sessionスコープ（"user"）が破棄されているか確認
		.andExpect(request().sessionAttributeDoesNotExist("user"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
		System.out.println("ログアウト完了しました");
	}
	
	
	@AfterEach
	@DisplayName("ログアウトを最後にする")
	public void ログアウトを最後にする() throws Exception{
		
		//テスト対象メソッド（registerメソッド）を実行
		mockMvc.perform(get("/login/toLogout"))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		.andExpect(request().sessionAttributeDoesNotExist("user"))
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		
		System.out.println("ログアウト完了しました");
	}

}
