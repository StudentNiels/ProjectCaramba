package com.caramba.ordertool.notifications;

import java.time.LocalDateTime;

/**
 * Notifications are used to handle debug information in a organized fashion
 */
public class Notification {
    private final NotificationType type;
    private final String contents;
    private final LocalDateTime timestamp;

    /**
     * Notification constructor
     *
     * @param type     the type of this notification. the notification type determines where it shows up in the application
     * @param contents the message of this notification
     */
    public Notification(NotificationType type, String contents) {
        this.type = type;
        this.contents = contents;
        timestamp = LocalDateTime.now();
    }

    /**
     * Returns the type of this notification. the notification type determines where it shows up in the application
     *
     * @return the notification type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * The message of this notification
     *
     * @return notification contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * LocalDateTime of when this notification was created
     *
     * @return LocalDateTime of when this notification was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}

