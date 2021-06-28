package com.caramba.ordertool;

import com.caramba.ordertool.models.Product;
import com.caramba.ordertool.models.Recommendation;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Saves recommendations as CSV files
 */
public class CSVCreator {

    /**
     * Save the specified recommendation as a csv file in the chosen path
     *
     * @param path           path to save the file to
     * @param recommendation recommendation to export
     */
    public void saveRecommendation(String path, Recommendation recommendation) {
        DateTimeFormatter creationDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter finalOrderDateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        try {
            FileWriter writer = new FileWriter(path);
            writer.append("sep=;\nORDER AANBEVELING;;\n");
            writer.append("Automatisch gegenereerd op:;").append(recommendation.getCreationDate().format(creationDateFormatter)).append(";\n");
            writer.append("Leverancier:;").append(recommendation.getSupplier().getName()).append(";\n");
            writer.append("Ter voorbereiding van verwachte verkopen in:;").append(recommendation.getYearMonthToOrderFor().format(yearMonthFormatter)).append(";\n");
            writer.append("Uiterste besteldatum:;").append(recommendation.getFinalOrderDate().format(finalOrderDateDateFormatter)).append(" (geschatte levertijd van ").append(String.valueOf(recommendation.getSupplier().getAvgDeliveryTime())).append(" dagen);\n");
            writer.append(";;\nTE BESTELLEN ARTIKELEN;;\n");
            writer.append("ARTIKEL NUMMER;BESCHRIJVING;HOEVEELHEID\n");
            for (Map.Entry<Product, Integer> entry : recommendation.getProductRecommendation().entrySet()) {
                Product p = entry.getKey();
                int q = entry.getValue();
                writer.append(p.getProductNum()).append(";");
                writer.append(p.getDescription()).append(";");
                writer.append(String.valueOf(q)).append(";\n");
            }
            writer.close();
            NotificationManager.show(new Notification(NotificationType.INFO, path + " saved successfully"));
        } catch (IOException e) {
            NotificationManager.show(new Notification(NotificationType.ERROR, "Failed to save csv file"));
            NotificationManager.showExceptionError(e);
        }
    }
}
