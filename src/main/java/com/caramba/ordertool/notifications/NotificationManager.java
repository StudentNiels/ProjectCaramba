package com.caramba.ordertool.notifications;

import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class NotificationManager {
    //#endregion
    public static void show(Notification notification) {
        NotificationType t = notification.getType();
        if (t.printToConsole) {
            printToConsole(notification);
        }
        if (t.showPopUp) {
            showPopUp(notification);
        }
    }

    public static void showExceptionError(Exception e) {
        show(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    private static void printToConsole(Notification n) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "[" + n.getTimestamp().format(f) + "] " +
                n.getType().toString() + ": " +
                n.getContents();
        System.out.println(s);
    }

    private static void showPopUp(Notification n) {
        NotificationType type = n.getType();
        Alert.AlertType alertType = Alert.AlertType.NONE;
        switch (type) {
            case ERROR -> alertType = Alert.AlertType.ERROR;
            case INFO -> alertType = Alert.AlertType.INFORMATION;
            case WARNING -> alertType = Alert.AlertType.WARNING;
        }
        Alert alert = new Alert(alertType);
        alert.setContentText(n.getContents());
        alert.showAndWait();
    }
}
