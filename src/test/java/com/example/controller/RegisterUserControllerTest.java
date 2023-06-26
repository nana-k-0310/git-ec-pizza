package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.BindException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.example.form.RegisterUserForm;
import com.example.service.RegisterUserService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ユーザー登録コントローラー")
class RegisterUserControllerTest {
	
	/**
	 * MockMvcオブジェクト
	 */
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * テスト対象クラス
	 */
	@Autowired
	private RegisterUserController target;
	
	@Autowired
	private RegisterUserController registerUserController;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	Validator validator;
	
//	@Autowired
//	private RegisterUserForm form = new RegisterUserForm();
//	private BindingResult bindingResult = new BindException(form, "registerUserForm");
	
	@BeforeEach
	
	/**
	 * 前処理（各テストケースを実行する前に行われる処理）
	 */
//	public void setup() {
		//MockMvcオブジェクトにテスト対象メソッドを設定
//		mockMvc = MockMvcBuilders.standaloneSetup(target)
//				.setViewResolvers(viewResolver())
//				.build();
//	}
	
	/**
	 * ViewResolverをThymeleaf用に設定する
	 * @return設定したViewResolver 
	 */
//	private ViewResolver viewResolver() {
		//この設定が無いと、javax.servlet.ServletException: Circular view path would dispatch back to the current handler URL [/add] again.
		//という例外が発生する場合があるため、追加する.
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setPrefix("classpath:");
//	}
	
	
	
	
	
//	@DisplayName("仮ユーザーを登録する")
//	void ユーザー登録テスト() throws Exception {
//		RegisterUserForm form = new RegisterUserForm();
		
//		form.setFirstName("名");
//		form.setLastName("テスト");
//		form.setEmail("test@gmail.com");
//		form.setPassword("testtest");
//		form.setConfirmationPassword("testtest");
//		form.setZipcode("123-4567");
//		form.setAddress("テスト住所");
//		form.setTelephone("00-1234-5678");
		
//	}

	/**
	 * テスト
	 */
	
	@Test
	@DisplayName("ユーザー登録画面を表示する")
	public void ユーザー画面表示テスト() throws Exception {
		
		//テスト対象メソッド（registerメソッド）を実行
		mockMvc.perform(get("/register"))
		//HTTPステータスがOKであることを確認（ステータスコードはが200は「isOk(」)）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/register_user"));
	
	}
	@Test
	@DisplayName("ユーザー情報を登録する")
	void ユーザー情報登録テスト() throws Exception {
		
		RegisterUserForm form = new RegisterUserForm();
		
		form.setLastName("河野");
		form.setFirstName("夏菜");
		form.setEmail("kono@sample.co.jp");
		form.setPassword("KonoKono");
		form.setConfirmationPassword("KonoKono");
		form.setZipcode("123-4567");
		form.setAddress("東京都渋谷区１−１−１");
		form.setTelephone("03-1234-5678");
		
		//テスト対象メソッド（registerメソッド）を実行.「flashAttr("registerUserForm"...」のフォームはhtmlのth:objectのもの.
		mockMvc.perform(post("/register/registerUser").flashAttr("registerUserForm",form))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/login"));
		//モデルの値を詰める
		

	}
	
	@Test
	@DisplayName("パスワードと確認用パスワードが違うユーザー情報を登録する")
	void パスワードと確認用パス間違いユーザー情報登録テスト() throws Exception {
		
		RegisterUserForm form1 = new RegisterUserForm();
		
		form1.setLastName("河野");
		form1.setFirstName("夏菜");
		form1.setEmail("kono@sample1.co.jp");
		form1.setPassword("Kono1Kono1");
		form1.setConfirmationPassword("KonoKono");
		form1.setZipcode("123-4567");
		form1.setAddress("東京都渋谷区１−１−１");
		form1.setTelephone("03-1234-5678");
		
		//テスト対象メソッド（registerメソッド）を実行
		ResultActions results = mockMvc.perform(post("/register/registerUser").flashAttr("registerUserForm",form1))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("registerUserForm"))
		
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/register_user"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "registerUserForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("パスワードと確認用パスワードが一致していません", message);
		
	}
	
	@Test
	@DisplayName("確認用パスワードが空欄でユーザー情報を登録する")
	void 確認用パス空欄ユーザー情報登録テスト() throws Exception {
		
		RegisterUserForm form2 = new RegisterUserForm();
		
		form2.setLastName("河野");
		form2.setFirstName("夏菜");
		form2.setEmail("kono@sample2.co.jp");
		form2.setPassword("Kono1Kono2");
		form2.setConfirmationPassword("");
		form2.setZipcode("123-4567");
		form2.setAddress("東京都渋谷区１−１−１");
		form2.setTelephone("03-1234-5678");
		
		//テスト対象メソッド（registerメソッド）を実行
		ResultActions results = mockMvc.perform(post("/register/registerUser").flashAttr("registerUserForm",form2))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("registerUserForm"))
		
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/register_user"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "registerUserForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("確認用パスワードを入力して下さい", message);
		
	}
	
	@Test
	@DisplayName("メールアドレスがDBに登録済のユーザー情報を登録する")
	void DB登録済メールのユーザー情報登録テスト() throws Exception {
		
		RegisterUserForm form3 = new RegisterUserForm();
		
		form3.setLastName("河野");
		form3.setFirstName("夏菜");
		form3.setEmail("abc@gmail.com");
		form3.setPassword("Kono3Kono3");
		form3.setConfirmationPassword("Kono3Kono3");
		form3.setZipcode("123-4567");
		form3.setAddress("東京都渋谷区１−１−１");
		form3.setTelephone("03-1234-5678");
		
		//テスト対象メソッド（registerメソッド）を実行
		ResultActions results = mockMvc.perform(post("/register/registerUser").flashAttr("registerUserForm",form3))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("registerUserForm"))
		
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/register_user"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "registerUserForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("そのメールアドレスはすでに使われています", message);
		
	}
	
	@Test
	@DisplayName("バリデーションチェックエラーのユーザー情報を登録する")
	void バリデーションチェックエラーユーザー情報登録テスト() throws Exception {
		
		RegisterUserForm form4 = new RegisterUserForm();
		
		form4.setLastName("河野");
		form4.setFirstName("夏菜");
		form4.setEmail("kono@gmail.com");
		form4.setPassword("Kono");
		form4.setConfirmationPassword("Kono");
		form4.setZipcode("123-4567");
		form4.setAddress("東京都渋谷区１−１−１");
		form4.setTelephone("03-1234-5678");
		
		//テスト対象メソッド（registerメソッド）を実行
		ResultActions results = mockMvc.perform(post("/register/registerUser").flashAttr("registerUserForm",form4))
		//HTTPステータスがOKであることを確認（ステータスコードはが404は「NotFound()」）
		.andExpect(status().isOk())
		// postFormにエラーが存在すること
		.andExpect(model().attributeHasErrors("registerUserForm"))
		
		//次画面の遷移先がmaterialize-version/register_userであることを確認
		.andExpect(view().name("materialize-version/register_user"));
		
		BindingResult bindResult = (BindingResult) results.andReturn().getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "registerUserForm");
        String message = bindResult.getFieldError().getDefaultMessage();
        assertEquals("パスワードは8文字以上16文字以内で記載してください", message);
		
	}
	
	
	
	@AfterEach
	@DisplayName("仮ユーザーを削除する")
	void 登録ユーザー削除() throws Exception {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", "kono@sample.co.jp");
		template.update("DELETE FROM users WHERE email = :email", param);
		System.out.println("確認完了しました");
	}
	
	
	

}
