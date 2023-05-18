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
	@DisplayName("商品全件取得")
	void 商品情報全件取得()throws Exception {
		List<Item> itemListHigh = showItemListService.findAll("high");
		assertEquals("とろけるビーフシチュー", itemListHigh.get(0).getName(), "値段の高い順で最初の商品が登録されていません");
		assertEquals("熟成ベーコンとマッシュルーム", itemListHigh.get(17).getName(), "値段の高い順で最後の商品が登録されていません");
		
		List<Item> itemListLow = showItemListService.findAll("low");
		assertEquals("じゃがバターベーコン", itemListLow.get(0).getName(), "値段の順で安い最初の商品が登録されていません");
		assertEquals("とろけるビーフシチュー", itemListLow.get(17).getName(), "値段の安い順で最後の商品が登録されていません");
		
		List<Item> itemListNull = showItemListService.findAll(null);
		assertEquals("じゃがバターベーコン", itemListNull.get(0).getName(), "順序指定なしで最初の商品が登録されていません");
		assertEquals("贅沢フォルマッジ", itemListNull.get(17).getName(), "順序指定なしで最後の商品が登録されていません");
		
	}
	
	@Test
	@DisplayName("商品名前指定取得")
	void 商品名前指定取得() throws Exception {
		List<Item> itemListTC1 = showItemListService.findByName("明太バターチーズ", null);
		System.out.println(itemListTC1.size());
		assertEquals(1, itemListTC1.size(), "TC1の商品数が正しくありません");
		assertEquals("明太バターチーズ", itemListTC1.get(0).getName(), "TC１名前エラー");
		
		List<Item> itemListTC2 = showItemListService.findByName("バ", null);
		assertEquals(4, itemListTC2.size(), "TC2の商品数が正しくありません");
		assertEquals("じゃがバターベーコン", itemListTC2.get(0).getName(), "TC2最初の名前エラー");
		assertEquals("バラエティー４", itemListTC2.get(3).getName(), "TC2最後の名前エラー");
		
		List<Item> itemListTC3 = showItemListService.findByName("じゃがバター", null);
		assertEquals(2, itemListTC3.size(), "TC3の商品数が正しくありません");
		assertEquals("じゃがバターベーコン", itemListTC3.get(0).getName(), "TC3最初の名前エラー");
		assertEquals("カレーじゃがバター", itemListTC3.get(1).getName(), "TC3最後の名前エラー");
		
		List<Item> itemListTC4 = showItemListService.findByName("ー", null);
		assertEquals(10, itemListTC4.size(), "TC4の商品数が正しくありません");
		assertEquals("じゃがバターベーコン", itemListTC4.get(0).getName(), "TC4最初の名前エラー");
		assertEquals("シーフードミックス", itemListTC4.get(9).getName(), "TC4最後の名前エラー");
		
		List<Item> itemListTC5 = showItemListService.findByName("", null);
		assertEquals(18, itemListTC5.size(), "TC5の商品数が正しくありません");
		assertEquals("じゃがバターベーコン", itemListTC5.get(0).getName(), "TC5最初の名前エラー");
		assertEquals("贅沢フォルマッジ", itemListTC5.get(17).getName(), "TC5最後の名前エラー");
		
		List<Item> itemListTC6 = showItemListService.findByName("ハワイアンパラダイス", null);
		assertEquals(0, itemListTC6.size(), "TC6の商品数が正しくありません");
				
		List<Item> itemListTC7 = showItemListService.findByName("--", null);
		assertEquals(0, itemListTC7.size(), "TC7の商品数が正しくありません");
		
	}

}
