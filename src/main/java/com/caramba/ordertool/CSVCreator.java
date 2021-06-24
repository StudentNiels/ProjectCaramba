package com.caramba.ordertool;

import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CSVCreator {

    public void saveRecommendation(String path, Recommendation recommendation){
        DateTimeFormatter creationDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter finalOrderDateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        try {
            FileWriter writer = new FileWriter(path);
            writer.append("sep=;\nORDER AANBEVELING;;\n");
            writer.append("Automatisch gegenereerd op:;" + recommendation.getCreationDate().format(creationDateFormatter) + ";\n");
            writer.append("Leverancier:;" + recommendation.getSupplier().getName() + ";\n");
            writer.append("Ter voorbereiding van verwachte verkopen in:;" + recommendation.getYearMonthToOrderFor().format(yearMonthFormatter) + ";\n");
            writer.append("Uiterste besteldatum:;" + recommendation.getFinalOrderDate().format(finalOrderDateDateFormatter) + " (geschatte levertijd van " + recommendation.getSupplier().getAvgDeliveryTime() + " dagen);\n");
            writer.append(";;\nTE BESTELLEN ARTIKELEN;;\n");
            writer.append("ARTIKEL NUMMER;BESCHRIJVING;HOEVEELHEID\n");
            for (Map.Entry<Product, Integer> entry : recommendation.getProductRecommendation().entrySet()) {
                Product p = entry.getKey();
                int q = entry.getValue();
                writer.append(p.getProductNum() + ";");
                writer.append(p.getDescription() + ";");
                writer.append(q + ";\n");
            }
            writer.close();
            NotificationManager.add(new Notification(NotificationType.INFO, path + " saved successfully"));
        }catch (IOException e){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Failed to save csv file"));
            NotificationManager.addExceptionError(e);
        }
    }
}
