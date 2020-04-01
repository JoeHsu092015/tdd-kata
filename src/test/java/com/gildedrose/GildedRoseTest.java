package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void testAddConjured() {
        Item[] items = new Item[] { new Item("Conjured Mana Cake", 3, 6) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Conjured Mana Cake", app.items[0].name);
    }

    // 每天結束時，系統會降低每個商品的上述兩個值
    @Test
    void testUpdateSellinAndQuality() {
        Item[] items = new Item[] { new Item("foo", 5, 6) };

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(4, app.items[0].sellIn);
        assertEquals(5, app.items[0].quality);
    }

    // 商品的品質不能為負數
    @Test
    void testQualityMustPositive() {
        Item[] items = new Item[] { new Item("foo", 5, 0) };

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(4, app.items[0].sellIn);
        assertEquals(0, app.items[0].quality);
    }

    // 一但商品過了銷售剩餘天數之後還沒未賣出，那麼其每日品質下降的速度就會加倍
    @Test
    void testExceedRemainDaysQuality() {
        Item[] items = new Item[] { new Item("foo", -1, 6) };

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(-2, app.items[0].sellIn);
        assertEquals(4, app.items[0].quality);
    }

    // 陳年乾酪(Aged Brie)的品質值隨著時間的推移，不減反增
    @Test
    void testBrieItem() {
        Item[] items = { new Item("Aged Brie", 3, 8) };

        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(2, app.items[0].sellIn);
        assertEquals(9, app.items[0].quality);
    }

    // 商品的品質的上限為50
    @Test
    void testQualityUpperBound() {
        Item[] items = {
                new Item("Aged Brie", 2, 50),
                new Item("Backstage passes to a TAFKAL80ETC concert", 4, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 8, 49)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(1, app.items[0].sellIn);
        assertEquals(50, app.items[0].quality);
        assertEquals(3, app.items[1].sellIn);
        assertEquals(50, app.items[1].quality);
        assertEquals(7, app.items[2].sellIn);
        assertEquals(50, app.items[2].quality);
    }

    // 魔法錘(Sulfuras)是一個傳奇商品，其銷售剩餘天數和品質都不會變化
    @Test
    void testSulfurasItem() {
        Item[] items = { new Item("Sulfuras, Hand of Ragnaros", 3, 7) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(3, app.items[0].sellIn);
        assertEquals(7, app.items[0].quality);
    }

    // 劇院後台通行證(Backstage passes)
    // 當離演出開始不到10天時，品質每日提高2；當不到5天時，品質值每日提高3，當演出結束後，品質歸0
    @Test
    void testBackstagePassesItem() {

        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 4, 10),
                         new Item("Backstage passes to a TAFKAL80ETC concert", 8, 10),
                         new Item("Backstage passes to a TAFKAL80ETC concert", 0, 10)};

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals(3, app.items[0].sellIn);
        assertEquals(13, app.items[0].quality);
        assertEquals(7, app.items[1].sellIn);
        assertEquals(12, app.items[1].quality);
        assertEquals(-1, app.items[2].sellIn);
        assertEquals(0, app.items[2].quality);
    }

    // 魔法(Conjured)商品每日品質下降速度是正常商品的2倍(新需求)
    @Test
    void testConjuredQualityDown() {

        Item[] items = new Item[] { new Item("Conjured Mana Cake", 3, 6) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(2, app.items[0].sellIn);
        assertEquals(4, app.items[0].quality);
    }



}
