package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Topping;

@SpringBootTest
@DisplayName("トッピングリポジトリ")
class ToppingRepositoryTest {
	
	@Autowired
	private ToppingRepository toppingRepository;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/** トッピング全件検索 */
	@Test
	@DisplayName("トッピング全件検索")
	void トッピング全件検索取得() throws Exception {
		List<Topping> toppingList = toppingRepository.findAll();
		assertEquals(28, toppingList.size(), "登録されていないデータがあります");
		
		Topping topping1 = toppingList.get(0);
		assertEquals(1, topping1.getId(), "1番目のトッピングIDが登録されていません");
		assertEquals("オニオン", topping1.getName(), "1番目のトッピングの名前が登録されていません");
		assertEquals(200, topping1.getPriceM(), "1番目のトッピングのMサイズの金額が登録されていません");
		assertEquals(300, topping1.getPriceL(), "1番目のトッピングのLサイズの金額が登録されていません");
		
		assertEquals(2, toppingList.get(1).getId(), "2番目のトッピングIDが登録されていません");
		assertEquals(3, toppingList.get(2).getId(), "3番目のトッピングIDが登録されていません");
		assertEquals(4, toppingList.get(3).getId(), "4番目のトッピングIDが登録されていません");
		assertEquals(5, toppingList.get(4).getId(), "5番目のトッピングIDが登録されていません");
		assertEquals(6, toppingList.get(5).getId(), "6番目のトッピングIDが登録されていません");
		assertEquals(7, toppingList.get(6).getId(), "7番目のトッピングIDが登録されていません");
		assertEquals(8, toppingList.get(7).getId(), "8番目のトッピングIDが登録されていません");
		assertEquals(9, toppingList.get(8).getId(), "9番目のトッピングIDが登録されていません");
		assertEquals(10, toppingList.get(9).getId(), "10番目のトッピングIDが登録されていません");
		assertEquals(11, toppingList.get(10).getId(), "11番目のトッピングIDが登録されていません");
		assertEquals(12, toppingList.get(11).getId(), "12番目のトッピングIDが登録されていません");
		assertEquals(13, toppingList.get(12).getId(), "13番目のトッピングIDが登録されていません");
		assertEquals(14, toppingList.get(13).getId(), "14番目のトッピングIDが登録されていません");
		assertEquals(15, toppingList.get(14).getId(), "15番目のトッピングIDが登録されていません");
		assertEquals(16, toppingList.get(15).getId(), "16番目のトッピングIDが登録されていません");
		assertEquals(17, toppingList.get(16).getId(), "17番目のトッピングIDが登録されていません");
		assertEquals(18, toppingList.get(17).getId(), "18番目のトッピングIDが登録されていません");
		assertEquals(19, toppingList.get(18).getId(), "19番目のトッピングIDが登録されていません");
		assertEquals(20, toppingList.get(19).getId(), "20番目のトッピングIDが登録されていません");
		assertEquals(21, toppingList.get(20).getId(), "21番目のトッピングIDが登録されていません");
		assertEquals(22, toppingList.get(21).getId(), "22番目のトッピングIDが登録されていません");
		assertEquals(23, toppingList.get(22).getId(), "23番目のトッピングIDが登録されていません");
		assertEquals(24, toppingList.get(23).getId(), "24番目のトッピングIDが登録されていません");
		assertEquals(25, toppingList.get(24).getId(), "25番目のトッピングIDが登録されていません");
		assertEquals(26, toppingList.get(25).getId(), "26番目のトッピングIDが登録されていません");
		assertEquals(27, toppingList.get(26).getId(), "27番目のトッピングIDが登録されていません");
		assertEquals(28, toppingList.get(27).getId(), "28番目のトッピングIDが登録されていません");
		
	}
	
	/** 主キー検索 */
	@Test
	@DisplayName("トッピング主キー検索")
	void トッピング主キー検索取得() throws Exception{
		Topping topping1 = toppingRepository.load(1);
		assertEquals(1, topping1.getId(), "最初のトッピングIDが登録されていません");
		assertEquals("オニオン", topping1.getName(), "最初のトッピングの名前が登録されていません");
		assertEquals(200, topping1.getPriceM(), "最初のトッピングのMサイズの金額が登録されていません");
		assertEquals(300, topping1.getPriceL(), "最初のトッピングのLサイズの金額が登録されていません");
		
	}
	

}
