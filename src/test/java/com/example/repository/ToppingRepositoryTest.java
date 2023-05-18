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
		assertEquals(1, topping1.getId(), "最初のトッピングIDが登録されていません");
		assertEquals("オニオン", topping1.getName(), "最初のトッピングの名前が登録されていません");
		assertEquals(200, topping1.getPriceM(), "最初のトッピングのMサイズの金額が登録されていません");
		assertEquals(300, topping1.getPriceL(), "最初のトッピングのLサイズの金額が登録されていません");
		
		
		Topping topping28 = toppingList.get(27);
		assertEquals(28, topping28.getId(), "最後のトッピングIDが登録されていません");
		assertEquals("チーズ増量", topping28.getName(), "最後のトッピングの名前が登録されていません");
		assertEquals(200, topping28.getPriceM(), "最後のトッピングのMサイズの金額が登録されていません");
		assertEquals(300
				, topping28.getPriceL(), "最後のトッピングのLサイズの金額が登録されていません");
		
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
