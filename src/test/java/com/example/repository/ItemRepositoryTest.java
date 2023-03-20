package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;
@SpringBootTest
class ItemRepositoryTest {

	@Autowired
	private ItemRepository repository;
	
//下のuserあるとき
//	@Autowired
//	private userRepository repository;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}
	
//例	
//	@BeforeEach
//	void setUp() throws Exception {
//		User user = new User();
//		user.setName("テスト名");
//		userRepository.save(user);
//	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	//主キー検索
	@Test
	void 商品テストロード名前() throws Exception {
		Item item = repository.load(1);
		assertEquals("じゃがバターベーコン", item.getName(), "名前が登録されていません");
	}
	@Test
	void 商品テストロード説明() throws Exception{
		Item item = repository.load(1);
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item.getDescription(), "説明が登録されていません");
	}
	@Test
	void 商品テストロードサイズM() throws Exception{
		Item item = repository.load(1);
		assertEquals(1490, item.getPriceM(), "サイズMの値段が登録されていません");
	}
	@Test
	void 商品テストロードサイズL() throws Exception{
		Item item = repository.load(1);
		assertEquals(2570, item.getPriceL(), "サイズLの値段が登録されていません");
	}
	@Test
	void 商品テストロード画像() throws Exception{
		Item item = repository.load(1);
		assertEquals("1.jpg", item.getImagePath(), "画像が登録されていません");
	}
	@Test
	void 商品テストロード削除対象() throws Exception{
		Item item = repository.load(1);
		assertEquals(false, item.getDeleted(), "削除対象が登録されていません");
	}
	
	//全件検索
	@Test
	void 商品全件検索() throws Exception{
		List<Item> itemList = repository.findAll();
		assertEquals(18, itemList.size(), "登録されていないデータがあります");
		Item item1 = itemList.get(0);
		assertEquals("じゃがバターベーコン", item1.getName(), "名前が登録されていません");
		Item item18 = itemList.get(17);
		assertEquals("贅沢フォルマッジ", item18.getName() , "名前が登録されていません");
	}
	
	
}
