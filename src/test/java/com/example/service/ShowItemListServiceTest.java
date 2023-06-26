package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;

@SpringBootTest
@DisplayName("商品一覧サービス")
class ShowItemListServiceTest {

	@Autowired
	private ShowItemListService showItemListService;
	
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
	@DisplayName("商品全件を並べ替え指定なしで検索する")
	public void id順商品全件検索テスト() throws Exception{
		List<Item> itemList = showItemListService.findAll(null);
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
	}
	
	
	@Test
	@DisplayName("値段の高い順で商品全件取得する")
	void 値段の高い順商品情報全件取得テスト()throws Exception {
		List<Item> itemHighList = showItemListService.findAll("high");
		assertEquals(18, itemHighList.size(), "登録されていないデータがあります");
		
		assertEquals(14, itemHighList.get(0).getId(), "高さ1番目の商品IDが登録されていません");
		assertEquals(6, itemHighList.get(1).getId(), "高さ2番目の商品IDが登録されていません");
		assertEquals(9, itemHighList.get(2).getId(), "高さ3番目の商品IDが登録されていません");
		assertEquals(11, itemHighList.get(3).getId(), "高さ4番目の商品IDが登録されていません");
		assertEquals(15, itemHighList.get(4).getId(), "高さ5番目の商品IDが登録されていません");
		assertEquals(18, itemHighList.get(5).getId(), "高さ6番目の商品IDが登録されていません");
		assertEquals(7, itemHighList.get(6).getId(), "高さ7番目の商品IDが登録されていません");
		assertEquals(16, itemHighList.get(7).getId(), "高さ8番目の商品IDが登録されていません");
		assertEquals(17, itemHighList.get(8).getId(), "高さ9番目の商品IDが登録されていません");
		assertEquals(8, itemHighList.get(9).getId(), "高さ10番目の商品IDが登録されていません");
		assertEquals(10, itemHighList.get(10).getId(), "高さ11番目の商品IDが登録されていません");
		assertEquals(12, itemHighList.get(11).getId(), "高さ12番目の商品IDが登録されていません");
		assertEquals(13, itemHighList.get(12).getId(), "高さ13番目の商品IDが登録されていません");
		assertEquals(4, itemHighList.get(13).getId(), "高さ14番目の商品IDが登録されていません");
		assertEquals(5, itemHighList.get(14).getId(), "高さ15番目の商品IDが登録されていません");
		assertEquals(1, itemHighList.get(15).getId(), "高さ16番目の商品IDが登録されていません");
		assertEquals(2, itemHighList.get(16).getId(), "高さ17番目の商品IDが登録されていません");
		assertEquals(3, itemHighList.get(17).getId(), "高さ18番目の商品IDが登録されていません");
		
	}
	
	@Test
	@DisplayName("値段の低い順で商品全件取得する")
	void 値段の低い順商品情報全件取得テスト()throws Exception {
		List<Item> itemLowList = showItemListService.findAll("low");
		assertEquals(18, itemLowList.size(), "登録されていないデータがあります");
		
		assertEquals(1, itemLowList.get(0).getId(), "安さ1番目の商品IDが登録されていません");
		assertEquals(2, itemLowList.get(1).getId(), "安さ2番目の商品IDが登録されていません");
		assertEquals(3, itemLowList.get(2).getId(), "安さ3番目の商品IDが登録されていません");
		assertEquals(4, itemLowList.get(3).getId(), "安さ4番目の商品IDが登録されていません");
		assertEquals(5, itemLowList.get(4).getId(), "安さ5番目の商品IDが登録されていません");
		assertEquals(8, itemLowList.get(5).getId(), "安さ6番目の商品IDが登録されていません");
		assertEquals(10, itemLowList.get(6).getId(), "安さ7番目の商品IDが登録されていません");
		assertEquals(12, itemLowList.get(7).getId(), "安さ8番目の商品IDが登録されていません");
		assertEquals(13, itemLowList.get(8).getId(), "安さ9番目の商品IDが登録されていません");
		assertEquals(16, itemLowList.get(9).getId(), "安さ10番目の商品IDが登録されていません");
		assertEquals(17, itemLowList.get(10).getId(), "安さ11番目の商品IDが登録されていません");
		assertEquals(7, itemLowList.get(11).getId(), "安さ12番目の商品IDが登録されていません");
		assertEquals(6, itemLowList.get(12).getId(), "安さ13番目の商品IDが登録されていません");
		assertEquals(9, itemLowList.get(13).getId(), "安さ14番目の商品IDが登録されていません");
		assertEquals(11, itemLowList.get(14).getId(), "安さ15番目の商品IDが登録されていません");
		assertEquals(15, itemLowList.get(15).getId(), "安さ16番目の商品IDが登録されていません");
		assertEquals(18, itemLowList.get(16).getId(), "安さ17番目の商品IDが登録されていません");
		assertEquals(14, itemLowList.get(17).getId(), "安さ18番目の商品IDが登録されていません");
		
	}
	
	@Test
	@DisplayName("一件の商品を名前指定で取得する")
	public void 一件商品名前指定取得テスト() throws Exception {
		List<Item> itemList = showItemListService.findByName("明太バターチーズ", null);
		assertEquals(1, itemList.size(), "一件商品検索数が正しくありません");
		Item item = itemList.get(0);
		
		assertEquals(5, item.getId(), "一件商品検索のIDが登録されていません");
		assertEquals("明太バターチーズ", item.getName(), "一件商品検索の名前が登録されていません");
		assertEquals("大きくカットしたポテトにコーンとベーコンをトッピングして、明太クリームソース、バター、チーズを合わせた、家族で楽しめるピザです", item.getDescription(), "一見商品検索の説明が登録されていません");
		assertEquals(1900, item.getPriceM(), "一件商品検索の商品Mが登録されていません");
		assertEquals(2980, item.getPriceL(), "一件商品検索の商品Lが登録されていません");
		assertEquals("5.jpg", item.getImagePath(), "一件商品検索の画像が登録されていません");
		assertEquals(false, item.getDeleted(), "一件商品検索の削除情報が登録されていません");
		
	}
	
	@Test
	@DisplayName("id順で複数件の商品を名前指定で取得する")
	public void id順複数件商品名前指定取得テスト() throws Exception {
		List<Item> itemList = showItemListService.findByName("バ", null);
		assertEquals(4, itemList.size(), "id順複件商品検索数が正しくありません");
		
		assertEquals(1, itemList.get(0).getId(), "1番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(1).getId(), "2番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(2).getId(), "3番目の商品IDが登録されていません");
		assertEquals(12, itemList.get(3).getId(), "4番目の商品IDが登録されていません");
		
	}
		
	@Test
	@DisplayName("高い順で複数件の商品を名前指定で取得する")
	public void 高い順複数件商品名前指定取得テスト() throws Exception {
		List<Item> itemList = showItemListService.findByName("バ", "high");
		assertEquals(4, itemList.size(), "高い順複件商品検索数が正しくありません");
		
		assertEquals(12, itemList.get(0).getId(), "高さ1番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(1).getId(), "高さ2番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(2).getId(), "高さ3番目の商品IDが登録されていません");
		assertEquals(1, itemList.get(3).getId(), "高さ4番目の商品IDが登録されていません");
		
	}
	
	@Test
	@DisplayName("安い順で複数件の商品を名前指定で取得する")
	public void 安い順複数件商品名前指定取得テスト() throws Exception {
		List<Item> itemList = showItemListService.findByName("バ", "low");
		assertEquals(4, itemList.size(), "安い順複件商品検索数が正しくありません");
		
		assertEquals(1, itemList.get(0).getId(), "安さ1番目の商品IDが登録されていません");
		assertEquals(4, itemList.get(1).getId(), "安さ2番目の商品IDが登録されていません");
		assertEquals(5, itemList.get(2).getId(), "安さ3番目の商品IDが登録されていません");
		assertEquals(12, itemList.get(3).getId(), "安さ4番目の商品IDが登録されていません");
		
	}
	
	@Test
	@DisplayName("結果なしの複数件商品を名前指定で取得する")
	public void 結果なしの複数件商品名前指定取得テスト() throws Exception {
		List<Item> itemList = showItemListService.findByName("ハワイアンパラダイス", null);
		assertEquals(0, itemList.size(), "結果なし複件商品検索数が正しくありません");
		
	}
	
}
