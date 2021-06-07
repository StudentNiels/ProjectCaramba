package com.caramba.ordertool.notifications;

public enum NotificationType {
    INFO(true),
    WARNING(true),
    ERROR(true);

    public final boolean printToConsole;

    NotificationType(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }
}
