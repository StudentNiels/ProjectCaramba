package com.caramba.ordertool.notifications;

public enum NotificationType {
    INFO(true, false),
    WARNING(true, false),
    ERROR(true, true);

    public final boolean printToConsole;
    public final boolean showPopUp;

    NotificationType(boolean printToConsole, boolean showPopUp) {
        this.printToConsole = printToConsole;
        this.showPopUp = showPopUp;
    }
}
