package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;

@SpringBootTest
@DisplayName("商品詳細サービス")
class ShowItemDetailServiceTest {

	@Autowired
	private ShowItemDetailService showItemDetailService;
	
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
	@DisplayName("商品詳細情報")
	void 商品詳細情報取得() throws Exception {
		Item item1 = showItemDetailService.showItemDetail(1);
		assertEquals(1, item1.getId(), "最初の商品IDが登録されていません");
		assertEquals("じゃがバターベーコン", item1.getName(), "最初の商品名が登録されていません");
		assertEquals("ホクホクのポテトと旨味が凝縮されたベーコンを特製マヨソースで味わって頂く商品です。バター風味豊かなキューブチーズが食材の味を一層引き立てます。", item1.getDescription(), "最初の商品説明が登録されていません");
		assertEquals(1490, item1.getPriceM(), "最初の商品のMサイズの金額が登録されていません");
		assertEquals(2570, item1.getPriceL(), "最初の商品のLサイズの金額が登録されていません");
		assertEquals("1.jpg", item1.getImagePath(), "最初の商品の画像が登録されていません");
		assertEquals(false, item1.getDeleted(), "最初の商品の削除対象が登録されていません");
		assertEquals(28, item1.getToppingList().size(), "最初の商品にトッピングリストが登録されていません");
		
		
		Item item18 = showItemDetailService.showItemDetail(18);
		assertEquals(18, item18.getId(), "最後の商品IDが登録されていません");
		assertEquals("贅沢フォルマッジ", item18.getName(), "最後の商品名が登録されていません");
		assertEquals("濃厚なカマンベールソース＆カマンベールと香りとコクのパルメザンチーズをトッピング", item18.getDescription(), "最後の商品説明が登録されていません");
		assertEquals(2700, item18.getPriceM(), "最後の商品のMサイズの金額が登録されていません");
		assertEquals(4050, item18.getPriceL(), "最後の商品のLサイズの金額が登録されていません");
		assertEquals("18.jpg", item18.getImagePath(), "最後の商品の画像が登録されていません");
		assertEquals(false, item18.getDeleted(), "最後の商品の削除対象が登録されていません");
		assertEquals(28, item18.getToppingList().size(), "最後の商品にトッピングリストが登録されていません");
	}

}
