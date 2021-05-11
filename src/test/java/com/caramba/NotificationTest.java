package com.caramba;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private Notification notification;

    @BeforeEach
    public void setup(){
        notification = new Notification();
        notification.fillProductsMap();
    }

    @Test
    @DisplayName("Generate notification")
    public void testNotification(){
        notification.sellProduct("Item-1",100);
        notification.sellProduct("Item-2",80);
        notification.notifyLowStock();
        assertEquals(100, notification.getOrderList().get("Item-1"));
        assertEquals(80, notification.getOrderList().get("Item-2"));
    }

}