package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Topping;

@SpringBootTest
class ToppingRepositoryTest {
	
	@Autowired
	private ToppingRepository repository;

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
	void トッピング全件検索() throws Exception {
		List<Topping> toppingList = repository.findAll();
		assertEquals(28, toppingList.size(), "登録されていないデータがあります");
		Topping topping1 = toppingList.get(0);
		assertEquals("オニオン", topping1.getName(), "名前が登録されていません");
		Topping topping28 = toppingList.get(27);
		assertEquals("チーズ増量", topping28.getName(), "名前が登録されていません");
	}

}
