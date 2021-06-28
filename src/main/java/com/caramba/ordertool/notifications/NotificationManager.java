package com.caramba.ordertool.notifications;

import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Displays incoming notifications
 */
public class NotificationManager {
    //#endregion

    /**
     * Display the specified notification in the way(s) determined by their type
     *
     * @param notification notification to show
     */
    public static void show(Notification notification) {
        NotificationType t = notification.getType();
        if (t.printToConsole) {
            printToConsole(notification);
        }
        if (t.showPopUp) {
            showPopUp(notification);
        }
    }

    /**
     * Show an error notification with the stacktrace of the specified exception
     *
     * @param e exception to create notification of
     */
    public static void showExceptionError(Exception e) {
        show(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    /**
     * Prints the notification to console
     *
     * @param notification notification to print
     */
    private static void printToConsole(Notification notification) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "[" + notification.getTimestamp().format(f) + "] " +
                notification.getType().toString() + ": " +
                notification.getContents();
        System.out.println(s);
    }

    /**
     * Shows the notification in a javafx alert dialog. The thread is blocked until it's closed.
     *
     * @param notification notification to show
     */
    private static void showPopUp(Notification notification) {
        NotificationType type = notification.getType();
        Alert.AlertType alertType = Alert.AlertType.NONE;
        switch (type) {
            case ERROR -> alertType = Alert.AlertType.ERROR;
            case INFO -> alertType = Alert.AlertType.INFORMATION;
            case WARNING -> alertType = Alert.AlertType.WARNING;
        }
        Alert alert = new Alert(alertType);
        alert.setContentText(notification.getContents());
        alert.showAndWait();
    }
}
