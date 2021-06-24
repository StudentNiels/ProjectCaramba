package com.caramba.ordertool.notifications;

import java.time.LocalDateTime;

public class Notification {
    private final NotificationType type;
    private final String contents;
    private LocalDateTime timestamp;

    public Notification(NotificationType type, String contents) {
        this.type = type;
        this.contents = contents;
        timestamp = LocalDateTime.now();
    }

    public NotificationType getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}

