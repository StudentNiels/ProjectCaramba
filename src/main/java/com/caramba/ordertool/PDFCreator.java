package com.caramba.ordertool;


import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Row;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PDFCreator {
    private final String path;
    private final String filename;
    //private final SupplierList suppliers;
    private final Recommendation recommendation;

    private final PDDocument document = new PDDocument();
    /*
    private PDFCreator(String path, String filename, SupplierList suppliers){
        this.filename = filename;
        this.path = path;
        this.suppliers = suppliers;
        //create the file
        try{
            File f = new File(getFullPath());
            if(f.getParentFile().mkdirs()){
                NotificationManager.add(new Notification(NotificationType.INFO, "Created new directory"));
            }
            if(!f.createNewFile()){
                NotificationManager.add(new Notification(NotificationType.WARNING, "The file " + filename + " already exists. Saving will overwrite it's contents."));
            }
        }catch (IOException e){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Failed to create file " + filename));
        }
    }
*/
    public PDFCreator(String path, Recommendation recommendation){
        this.filename = path.substring(path.lastIndexOf("/") + 1);
        this.path = path;
        this.recommendation = recommendation;
        //create the file
        try{
            File f = new File(path);
            if(f.getParentFile().mkdirs()){
                NotificationManager.add(new Notification(NotificationType.INFO, "Created new directory"));
            }
            if(!f.createNewFile()){
                NotificationManager.add(new Notification(NotificationType.WARNING, "The file " + filename + " already exists. Saving will overwrite it's contents."));
            }
        }catch (IOException e){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Failed to create file " + filename));
        }
    }
/*
    public PDFCreator(String path, SupplierList suppliers){
        this(path, "Orderlist-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd-HH-mm-ss")) + ".pdf", suppliers);
    }
*/
    public void addProducts(){
        PDPage page = new PDPage(PDRectangle.A4);

        RecommendationList recommendations = OrderTool.getRecommendations();
        recommendations.sortRecommendationsByDate();

        try{
            float margin = 50;
            float lineSpace = 20;
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
            float y = 800;
            float x = 50;
            PDPageContentStream cs = new PDPageContentStream(document, page);

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 20);
            cs.newLineAtOffset(x, y);
            y = y - lineSpace;
            cs.showText("Orderlist");
            cs.endText();
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 16);
            cs.newLineAtOffset(x, y);
            y = y - lineSpace;
            cs.showText("created on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            cs.endText();
            //add a table per supplier
            int tableCount = 1;
                BaseTable table = new BaseTable(y, yStartNewPage, margin, tableWidth, margin, document, page, true, true);
                Row<PDPage> headerRow = table.createRow(15f);
                headerRow.createCell(100, "ORDER #" + tableCount);
                table.addHeaderRow(headerRow);
                Row<PDPage> row = table.createRow(12);
                row.createCell(100, "Order for: " + recommendation.getSupplier().getName()); //Supplier name
                row = table.createRow(12);
                row.createCell(4, "#");
                row.createCell(32, "Product Number");
                row.createCell(32, "Description");
                row.createCell(32, "Quantity");
                int productCount = 1;
                HashMap<Product, Integer> productQuantityMap = recommendation.getProductRecommendation(); // Product HashMap
                for (Map.Entry<Product, Integer> productQuantityEntry : productQuantityMap.entrySet()) {
                    Product p = productQuantityEntry.getKey(); // Product from Product HashMap
                    int amount = productQuantityEntry.getValue(); // Amount from Product HashMap

                    //add the cells for this product
                    row = table.createRow(12);
                    row.createCell(4, Integer.toString(productCount));
                    row.createCell(32, p.getProductNum()); // Product number
                    row.createCell(32, p.getDescription()); // Product description
                    row.createCell(32, String.valueOf(amount));
                    productCount = productCount +  1;
                }
                y = table.draw() - lineSpace;
                tableCount = tableCount + 1;
            cs.close();
        }catch (IOException e){
            NotificationManager.addExceptionError(e);
        }

        document.addPage(page);
    }

    public void save(){
        try{
            document.save(path);
            document.close();
            NotificationManager.add(new Notification(NotificationType.INFO, filename + " was saved successfully"));
        }catch (IOException e) {
            NotificationManager.add(new Notification(NotificationType.ERROR, "Failed to save pdf file"));
            NotificationManager.addExceptionError(e);
        }
    }
}
