package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.SystemPropertyUtils;

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
	@DisplayName("DBにあるidから商品を取得する")
	@Test
	void DBidあり商品ロードテスト() throws Exception {
		Item item1 = repository.load(1);
		assertEquals("じゃがバターベーコン", item1.getName(), "名前が登録されていません");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "説明が登録されていません");
		assertEquals(1490, item1.getPriceM(), "商品Mが登録されていません");
		assertEquals(2570, item1.getPriceL(), "商品Lが登録されていません");
		assertEquals("1.jpg", item1.getImagePath(), "画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
	}
	
	@DisplayName("DBにないidから商品を取得する")
	@Test
	void DBidなし商品ロードテスト() throws Exception {
		assertEquals(null, repository.load(20), "DBにないデータがあります");

	}
	
	
	//全件検索（最初と最後のID呼び出し）
	@DisplayName("商品全件を並べ替え指定なしで検索する")
	@Test
	void id順商品全件検索テスト() throws Exception{
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
		
		assertEquals(2, itemList.get(1).getId(), "2番目の商品IDが登録されていません");
		assertEquals(3, itemList.get(2).getId(), "3番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(3).getId(), "4番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(4).getId(), "5番目の商品IDが登録されていません");
		assertEquals(6, itemList.get(5).getId(), "6番目の商品IDが登録されていません");
		assertEquals(7, itemList.get(6).getId(), "7番目の商品IDが登録されていません");
		assertEquals(8, itemList.get(7).getId(), "8番目の商品IDが登録されていません");
		assertEquals(9, itemList.get(8).getId(), "9番目の商品IDが登録されていません");
		assertEquals(10, itemList.get(9).getId(), "10番目の商品IDが登録されていません");
		assertEquals(11, itemList.get(10).getId(), "11番目の商品IDが登録されていません");
		assertEquals(12, itemList.get(11).getId(), "12番目の商品IDが登録されていません");
		assertEquals(13, itemList.get(12).getId(), "13番目の商品IDが登録されていません");
		assertEquals(14, itemList.get(13).getId(), "14番目の商品IDが登録されていません");
		assertEquals(15, itemList.get(14).getId(), "15番目の商品IDが登録されていません");
		assertEquals(16, itemList.get(15).getId(), "16番目の商品IDが登録されていません");
		assertEquals(17, itemList.get(16).getId(), "17番目の商品IDが登録されていません");
		assertEquals(18, itemList.get(17).getId(), "18番目の商品IDが登録されていません");
		
//		Item item18 = itemList.get(17);
//		assertEquals(18, item18.getId(), "IDが登録されていません");
//		assertEquals("贅沢フォルマッジ", item18.getName() , "名前が登録されていません");
//		assertEquals("濃厚なカマンベールソース＆カマンベールと香りとコクのパルメザンチーズをトッピング", item18.getDescription(), "説明が登録されていません");
//		assertEquals(2700, item18.getPriceM(), "商品Mが登録されていません");
//		assertEquals(4050, item18.getPriceL(), "商品Lが登録されていません");
//		assertEquals("18.jpg", item18.getImagePath(), "画像が登録されていません");
//		assertEquals(false, item18.getDeleted(), "削除情報が登録されていません");
	}
	
	@DisplayName("商品全件を値段の高い順で検索する")
	@Test
	void 値段高い順商品全件検索テスト() throws Exception{
		List<Item> itemList = repository.findAll("high");
		assertEquals(18, itemList.size(), "登録されていないデータがあります");
		
		assertEquals(14, itemList.get(0).getId(), "高さ1番目の商品IDが登録されていません");
		assertEquals(6, itemList.get(1).getId(), "高さ2番目の商品IDが登録されていません");
		assertEquals(9, itemList.get(2).getId(), "高さ3番目の商品IDが登録されていません");
		assertEquals(11, itemList.get(3).getId(), "高さ4番目の商品IDが登録されていません");
		assertEquals(15, itemList.get(4).getId(), "高さ5番目の商品IDが登録されていません");
		assertEquals(18, itemList.get(5).getId(), "高さ6番目の商品IDが登録されていません");
		assertEquals(7, itemList.get(6).getId(), "高さ7番目の商品IDが登録されていません");
		assertEquals(16, itemList.get(7).getId(), "高さ8番目の商品IDが登録されていません");
		assertEquals(17, itemList.get(8).getId(), "高さ9番目の商品IDが登録されていません");
		assertEquals(8, itemList.get(9).getId(), "高さ10番目の商品IDが登録されていません");
		assertEquals(10, itemList.get(10).getId(), "高さ11番目の商品IDが登録されていません");
		assertEquals(12, itemList.get(11).getId(), "高さ12番目の商品IDが登録されていません");
		assertEquals(13, itemList.get(12).getId(), "高さ13番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(13).getId(), "高さ14番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(14).getId(), "高さ15番目の商品IDが登録されていません");
		assertEquals(1, itemList.get(15).getId(), "高さ16番目の商品IDが登録されていません");
		assertEquals(2, itemList.get(16).getId(), "高さ17番目の商品IDが登録されていません");
		assertEquals(3, itemList.get(17).getId(), "高さ18番目の商品IDが登録されていません");
		
//		Item item1 = itemList.get(0);
//		assertEquals(14, item1.getId(), "IDが登録されていません");
//		assertEquals("とろけるビーフシチュー", item1.getName() , "名前が登録されていません");
//		assertEquals("デミグラスソースでじっくり煮込んだ旨味たっぷりのビーフシチューのピザ", item1.getDescription(), "説明が登録されていません");
//		assertEquals(2980, item1.getPriceM(), "商品Mが登録されていません");
//		assertEquals(4460, item1.getPriceL(), "商品Lが登録されていません");
//		assertEquals("14.jpg", item1.getImagePath(), "画像が登録されていません");
//		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
	}
	
	
	@DisplayName("商品全件を値段の安い順で検索する")
	@Test
	void 値段安い順商品全件検索テスト() throws Exception{
		List<Item> itemList = repository.findAll("low");
		assertEquals(18, itemList.size(), "登録されていないデータがあります");
		
		assertEquals(1, itemList.get(0).getId(), "安さ1番目の商品IDが登録されていません");
		assertEquals(2, itemList.get(1).getId(), "安さ2番目の商品IDが登録されていません");
		assertEquals(3, itemList.get(2).getId(), "安さ3番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(3).getId(), "安さ4番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(4).getId(), "安さ5番目の商品IDが登録されていません");
		assertEquals(8, itemList.get(5).getId(), "安さ6番目の商品IDが登録されていません");
		assertEquals(10, itemList.get(6).getId(), "安さ7番目の商品IDが登録されていません");
		assertEquals(12, itemList.get(7).getId(), "安さ8番目の商品IDが登録されていません");
		assertEquals(13, itemList.get(8).getId(), "安さ9番目の商品IDが登録されていません");
		assertEquals(16, itemList.get(9).getId(), "安さ10番目の商品IDが登録されていません");
		assertEquals(17, itemList.get(10).getId(), "安さ11番目の商品IDが登録されていません");
		assertEquals(7, itemList.get(11).getId(), "安さ12番目の商品IDが登録されていません");
		assertEquals(6, itemList.get(12).getId(), "安さ13番目の商品IDが登録されていません");
		assertEquals(9, itemList.get(13).getId(), "安さ14番目の商品IDが登録されていません");
		assertEquals(11, itemList.get(14).getId(), "安さ15番目の商品IDが登録されていません");
		assertEquals(15, itemList.get(15).getId(), "安さ16番目の商品IDが登録されていません");
		assertEquals(18, itemList.get(16).getId(), "安さ17番目の商品IDが登録されていません");
		assertEquals(14, itemList.get(17).getId(), "安さ18番目の商品IDが登録されていません");
		
		
		
//		Item item1 = itemList.get(0);
//		assertEquals(1, item1.getId(), "IDが登録されていません");
//		assertEquals("じゃがバターベーコン", item1.getName(), "名前が登録されていません");
//		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "説明が登録されていません");
//		assertEquals(1490, item1.getPriceM(), "商品Mが登録されていません");
//		assertEquals(2570, item1.getPriceL(), "商品Lが登録されていません");
//		assertEquals("1.jpg", item1.getImagePath(), "画像が登録されていません");
//		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
	}
	
	
	//曖昧検索
	@DisplayName("商品の名前（曖昧）を1件検索する")
	@Test
	void 一件商品名前曖昧検索テスト() throws Exception{
		List<Item> itemList = repository.findByName("明太バターチーズ", null);
		
		assertEquals(1, itemList.size(), "正常に検索出来ません");
		
		Item item1 = itemList.get(0);
		assertEquals(5, item1.getId(), "IDが登録されていません");
		assertEquals("明太バターチーズ", item1.getName(), "検索される商品名が違います");
		assertEquals("大きくカットしたポテトにコーンとベーコンをトッピングして、明太クリームソース、バター、チーズを合わせた、家族で楽しめるピザです", item1.getDescription(), "説明が登録されていません");
		assertEquals(1900, item1.getPriceM(), "商品Mが登録されていません");
		assertEquals(2980, item1.getPriceL(), "商品Lが登録されていません");
		assertEquals("5.jpg", item1.getImagePath(), "画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "削除情報が登録されていません");
		
	}	
	
	@DisplayName("商品の名前（曖昧）を複数件値段の高い順で検索する")
	@Test
	void 複数件値段高い順商品名前曖昧検索テスト() throws Exception{
		List<Item> itemList = repository.findByName("バ", "high");
		
		assertEquals(4, itemList.size(), "正常に検索出来ません");
	
		Item item1 = itemList.get(0);
		assertEquals(12, item1.getId(), "1番目に高い商品が登録されていません");
		Item item2 = itemList.get(1);
		assertEquals(4, item2.getId(), "2番目に高い商品が登録されていません");
		Item item3 = itemList.get(2);
		assertEquals(5, item3.getId(), "3番目に高い商品が登録されていません");
		Item item4 = itemList.get(3);
		assertEquals(1, item4.getId(), "4番目に高い商品が登録されていません");
		
	}
	
	@DisplayName("商品の名前（曖昧）を複数件値段の安い順で検索する")
	@Test
	void 複数件値段安い順商品名前曖昧検索テスト() throws Exception{
		List<Item> itemList = repository.findByName("バ", "low");
		
		assertEquals(4, itemList.size(), "正常に検索出来ません");
	
		Item item1 = itemList.get(0);
		assertEquals(1, item1.getId(), "1番目に安い商品が登録されていません");
		Item item2 = itemList.get(1);
		assertEquals(4, item2.getId(), "2番目に安い商品が登録されていません");
		Item item3 = itemList.get(2);
		assertEquals(5, item3.getId(), "3番目に安い商品が登録されていません");
		Item item4 = itemList.get(3);
		assertEquals(12, item4.getId(), "4番目に安い商品が登録されていません");
	}
	
	@DisplayName("商品の名前（曖昧）を複数件並び替え指定なしで検索する")
	@Test
	void 複数件値段id順商品名前曖昧検索テスト() throws Exception{
		List<Item> itemList = repository.findByName("バ", null);
		
		assertEquals(4, itemList.size(), "正常に検索出来ません");
	
		Item item1 = itemList.get(0);
		assertEquals(1, item1.getId(), "id1番目の商品が登録されていません");
		Item item2 = itemList.get(1);
		assertEquals(4, item2.getId(), "id2番目の商品が登録されていません");
		Item item3 = itemList.get(2);
		assertEquals(5, item3.getId(), "id3番目の商品が登録されていません");
		Item item4 = itemList.get(3);
		assertEquals(12, item4.getId(), "id4番目の商品が登録されていません");
	}
	
	@DisplayName("商品の名前（曖昧）を0件検索する")
	@Test
	void 件数なし商品名前曖昧検索テスト() throws Exception{
		
		List<Item> itemList = repository.findByName("ハワイアンパラダイス", null);
		assertEquals(0, itemList.size(), "正常に検索出来ません");
		
	}	
	
}
