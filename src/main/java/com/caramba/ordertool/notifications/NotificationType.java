package com.caramba.ordertool.notifications;

public enum NotificationType {
    INFO(false),
    WARNING(false),
    ERROR(true);

    /**
     * Enable notifications of this type to be printed to console
     */
    public final boolean printToConsole;
    /**
     * Enable notifications of this type to be shown in a javafx dialog
     */
    public final boolean showPopUp;

    NotificationType(boolean showPopUp) {
        this.printToConsole = true;
        this.showPopUp = showPopUp;
    }
}
