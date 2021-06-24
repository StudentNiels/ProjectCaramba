package com.caramba.ordertool.notifications;

import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NotificationManager {
    private static final ArrayList<Notification> notifications = new ArrayList<>();

    //#endregion
    public static void add(Notification notification) {
        notifications.add(notification);
        NotificationType t = notification.getType();
        if(t.printToConsole){
            printToConsole(notification);
        }
        if(t.showPopUp){
            showPopUp(notification);
        }
    }

    public static void addExceptionError(Exception e){
        add(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    public static void printToConsole(Notification n){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "[" + n.getTimestamp().format(f) + "] " +
                n.getType().toString() + ": " +
                n.getContents();
        System.out.println(s);
    }

    public static void showPopUp(Notification n){
        NotificationType type = n.getType();
        Alert.AlertType alertType = Alert.AlertType.NONE;
        switch (type){
            case ERROR -> alertType = Alert.AlertType.ERROR;
            case INFO -> alertType = Alert.AlertType.INFORMATION;
            case WARNING -> alertType = Alert.AlertType.WARNING;
        }
        Alert alert = new Alert(alertType);
        alert.setContentText(n.getContents());
        alert.showAndWait();
    }
}
