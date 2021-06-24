package com.caramba.ordertool.notifications;

public enum NotificationType {
    INFO(false),
    WARNING(false),
    ERROR(true);

    public final boolean printToConsole;
    public final boolean showPopUp;

    NotificationType(boolean showPopUp) {
        this.printToConsole = true;
        this.showPopUp = showPopUp;
    }
}
