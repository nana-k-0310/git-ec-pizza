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

import com.example.domain.Item;
@SpringBootTest
@DisplayName("商品リポジトリ")
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
	@DisplayName("商品ロード名前")
	@Test
	void 商品テストロード名前() throws Exception {
		Item item = repository.load(1);
		assertEquals("じゃがバターベーコン", item.getName(), "名前が登録されていません");
	}
	@DisplayName("商品ロード説明")
	@Test
	void 商品テストロード説明() throws Exception{
		Item item = repository.load(1);
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item.getDescription(), "説明が登録されていません");
	}
	@DisplayName("商品ロードMサイズ値段")
	@Test
	void 商品テストロードサイズM() throws Exception{
		Item item = repository.load(1);
		assertEquals(1490, item.getPriceM(), "サイズMの値段が登録されていません");
	}
	@DisplayName("商品ロードlサイズ値段")
	@Test
	void 商品テストロードサイズL() throws Exception{
		Item item = repository.load(1);
		assertEquals(2570, item.getPriceL(), "サイズLの値段が登録されていません");
	}
	@DisplayName("商品ロード画像")
	@Test
	void 商品テストロード画像() throws Exception{
		Item item = repository.load(1);
		assertEquals("1.jpg", item.getImagePath(), "画像が登録されていません");
	}
	@DisplayName("商品ロード削除対象")
	@Test
	void 商品テストロード削除対象() throws Exception{
		Item item = repository.load(1);
		assertEquals(false, item.getDeleted(), "削除対象が登録されていません");
	}
	
	//全件検索（最初と最後のID呼び出し）
	@DisplayName("商品全件検索")
	@Test
	void 商品全件検索() throws Exception{
		List<Item> itemList = repository.findAll(null);
		assertEquals(18, itemList.size(), "登録されていないデータがあります");
		Item item1 = itemList.get(0);
		assertEquals(1, item1.getId(), "IDが登録されていません");
		assertEquals("じゃがバターベーコン", item1.getName(), "名前が登録されていません");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "説明が登録されていません");
		assertEquals(1490, item1.getPriceM(), "商品Mが登録されていません");
		assertEquals(2570, item1.getPriceL(), "商品Lが登録されていません");
		assertEquals("1.jpg", item1.getImagePath(), "画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
		Item item18 = itemList.get(17);
		assertEquals(18, item18.getId(), "IDが登録されていません");
		assertEquals("贅沢フォルマッジ", item18.getName() , "名前が登録されていません");
		assertEquals("濃厚なカマンベールソース＆カマンベールと香りとコクのパルメザンチーズをトッピング", item18.getDescription(), "説明が登録されていません");
		assertEquals(2700, item18.getPriceM(), "商品Mが登録されていません");
		assertEquals(4050, item18.getPriceL(), "商品Lが登録されていません");
		assertEquals("18.jpg", item18.getImagePath(), "画像が登録されていません");
		assertEquals(false, item18.getDeleted(), "削除情報が登録されていません");
	}
	
	//曖昧検索
	@DisplayName("商品曖昧検索")
	@Test
	void 商品曖昧検索() throws Exception{
		List<Item> itemList = repository.findByName("じゃが", null);
		
		assertEquals(2, itemList.size(), "正常に検索出来ません");
		
		Item item1 = itemList.get(0);
		assertEquals(1, item1.getId(), "IDが登録されていません");
		assertEquals("じゃがバターベーコン", item1.getName(), "検索される商品名が違います");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "説明が登録されていません");
		assertEquals(1490, item1.getPriceM(), "商品Mが登録されていません");
		assertEquals(2570, item1.getPriceL(), "商品Lが登録されていません");
		assertEquals("1.jpg", item1.getImagePath(), "画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
		Item item2 = itemList.get(1);
		assertEquals(4, item2.getId(), "IDが登録されていません");
		assertEquals("カレーじゃがバター", item2.getName(), "検索される商品名が違います");
		assertEquals("マイルドな味付けのカレーに大きくカットしたポテトをのせた、バターとチーズの風味が食欲をそそるお子様でも楽しめる商品です", item2.getDescription(), "説明が登録されていません");
		assertEquals(1900, item2.getPriceM(), "商品Mが登録されていません");
		assertEquals(2980, item2.getPriceL(), "商品Lが登録されていません");
		assertEquals("4.jpg", item2.getImagePath(), "画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
	}
	
	
}
