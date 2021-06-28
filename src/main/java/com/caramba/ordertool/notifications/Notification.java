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

    public Notification(String contents) {
        this(NotificationType.INFO, contents);
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

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

