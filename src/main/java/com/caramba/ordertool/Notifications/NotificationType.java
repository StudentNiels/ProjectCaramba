package com.caramba.ordertool.Notifications;

public enum NotificationType {
    INFO(true),
    WARNING(true),
    ERROR(true);

    public final boolean printToConsole;

    NotificationType(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }
}
