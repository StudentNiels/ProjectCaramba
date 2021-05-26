package com.caramba.ordertool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockManagerTest {

    private StockManager stockManager;

    @BeforeEach
    public void setup(){
        stockManager = new StockManager();
        stockManager.fillProductsMap();
    }

    @Test
    @DisplayName("Generate notification")
    public void testNotification(){
        stockManager.sellProduct("Item-1",100);
        stockManager.sellProduct("Item-2",80);
        stockManager.notifyLowStock();
        assertEquals(100, stockManager.getOrderList().get("Item-1"));
        assertEquals(80, stockManager.getOrderList().get("Item-2"));
    }

}