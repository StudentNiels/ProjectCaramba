package com.caramba.ordertool;


import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import com.caramba.ordertool.Notifications.Notification;
import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.Notifications.NotificationType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PDFCreator {
    private final String path;
    private final String filename;
    private final SupplierList suppliers;

    private final PDDocument document = new PDDocument();
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

    public PDFCreator(String path, SupplierList suppliers){
        this(path, "Orderlist-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd-HH-mm-ss")) + ".pdf", suppliers);
    }

    public void addProductList(ProductList products){
        PDPage page = new PDPage(PDRectangle.A4);
        HashMap<String, ProductList> productMap = separateProductListPerSupplier(products);
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
            for (Map.Entry<String, ProductList> productListEntry : productMap.entrySet()) {
                BaseTable table = new BaseTable(y, yStartNewPage, margin, tableWidth, margin, document, page, true, true);
                Row<PDPage> headerRow = table.createRow(15f);
                Cell<PDPage> cell = headerRow.createCell(100, "ORDER #" + tableCount);
                table.addHeaderRow(headerRow);
                Row<PDPage> row = table.createRow(12);
                cell = row.createCell(100, "Order for: " + productListEntry.getKey());
                row = table.createRow(12);
                cell = row.createCell(4, "#");
                cell = row.createCell(32, "Product Number");
                cell = row.createCell(32, "Description");
                cell = row.createCell(32, "Quantity");
                int productCount = 1;
                for (Product p : productListEntry.getValue().getProducts().values()) {
                    row = table.createRow(12);
                    cell = row.createCell(4, Integer.toString(productCount));
                    cell = row.createCell(32, p.getProductNum());
                    cell = row.createCell(32, p.getDescription());
                    //todo quantity should be tracked by productList (or orderlist?). hardcoded to one for now
                    cell = row.createCell(32, "1");
                    productCount = productCount +  1;
                }
                y = table.draw() - lineSpace;
                tableCount = tableCount + 1;
            }

            cs.close();
        }catch (IOException e){
            NotificationManager.addExceptionError(e);
        }

        document.addPage(page);
    }

    private HashMap<String, ProductList> separateProductListPerSupplier(ProductList productlist){
        HashMap<String, ProductList> result = new HashMap<>();
        HashMap<UUID, Product> productsToCheck = productlist.getProducts();
        //check all known suppliers
        for (Map.Entry<UUID, Supplier> sEntry : suppliers.getSuppliers().entrySet()) {
            ProductList supplierProductList = new ProductList();
            Iterator<Map.Entry<UUID, Product>> itP = productsToCheck.entrySet().iterator();
            while (itP.hasNext()) {
                Map.Entry<UUID, Product> pEntry = itP.next();
                if (sEntry.getValue().containsProduct(pEntry.getValue())) {
                    supplierProductList.add(pEntry.getValue());
                    itP.remove();
                }
            }
            result.put(sEntry.getValue().getName(), supplierProductList);
        }
        if(!productsToCheck.isEmpty()){
            ProductList pList = new ProductList();
            for(Product p : productsToCheck.values()){
                pList.add(p);
            }
            result.put("Unknown supplier", pList);
        }
        return result;
    }

    private String getFullPath(){
        String s = path;
        if(!path.endsWith("/")){
            s = s + "/";
        }
        return s + filename;
    }

    public void save(){
        try{
            document.save(getFullPath());
            document.close();
            NotificationManager.add(new Notification(NotificationType.INFO, filename + " was saved successfully"));
        }catch (IOException e) {
            NotificationManager.add(new Notification(NotificationType.ERROR, "Failed to save pdf file"));
            NotificationManager.addExceptionError(e);
        }
    }
}
