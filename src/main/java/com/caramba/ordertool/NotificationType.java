package com.caramba.ordertool;

public enum NotificationType {
    INFO(true),
    WARNING(true),
    ERROR(true);

    public final boolean printToConsole;

    NotificationType(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }
}
