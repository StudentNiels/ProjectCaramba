package com.caramba.ordertool;

import com.caramba.ordertool.models.*;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Manages the connection to firebase
 */
public class FireStoreConfig {

    private Firestore db;

    /**
     * The SDK of Firebase Admin is implemented here, a json file with credentials is already present (firebase.json)
     * Establish the connection
     */
    public void fireStoreConfig() {
        try {
            FileInputStream serviceAccount = null;
            try{
                serviceAccount = new FileInputStream("./././firebase.json");
            }catch (FileNotFoundException e){
                try {
                    serviceAccount = new FileInputStream("/firebase.json");
                }catch (FileNotFoundException e2){
                    NotificationManager.show(new Notification(NotificationType.ERROR, "Could not find firebase credentials. Please place the account credentials json in the same directory as the OrderTool jar"));
                    System.exit(1);
                }
            }
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            NotificationManager.showExceptionError(e);
        }
        db = FirestoreClient.getFirestore();
    }


    /**
     * Make a list of all the products
     *
     * @return ProductList with all products in firebase
     */
    public ProductList retrieveAllProducts() {
        ProductList result = new ProductList();
        Iterable<DocumentReference> collections = db.collection("Products").listDocuments();
        for (DocumentReference collRef : collections) {
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if (docSnapshot.exists()) {
                    Product p = docSnapshot.toObject(Product.class);
                    result.add(collRef.getId(), p);
                }
            } catch (InterruptedException | ExecutionException e) {
                NotificationManager.showExceptionError(e);
            }
        }
        //save to history
        saveProductQuantityHistory(result);
        return result;
    }

    /**
     * Saves the current quantity of the product to the history of this year and month.
     * This is currently run every time the products are retrieved
     * Ideally this should be run automatically once at the end of the month instead
     * This could be probably be done using firebase's 'scheduled functions' feature
     * However this feature is exclusive to the paid plan of firebase, which we don't currently have access to
     *
     * @param productList products to save stock history of
     */
    public void saveProductQuantityHistory(ProductList productList) {
        LocalDate now = LocalDate.now();
        for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
            String k = entry.getKey();
            Product p = entry.getValue();
            Map<String, Integer> data = new HashMap<>();
            data.put("quantity", p.getQuantity());
            db.collection("Products").document(k).collection("History").document(String.valueOf(now.getYear())).collection("Months").document(Integer.toString(now.getMonth().getValue())).set(data);
        }
    }

    /**
     * Make a list of all the Sales
     *
     * @return SalesList with all sales in firebase
     */
    public SalesList retrieveAllSales() {
        SalesList result = new SalesList();
        Iterable<DocumentReference> collections = db.collection("Sales").listDocuments();
        for (DocumentReference collRef : collections) {
            //add subcollection to hashmap
            HashMap<String, Integer> products = new HashMap<>();
            Iterable<DocumentReference> subCollections = collRef.collection("SalesList").listDocuments();
            for (DocumentReference subRef : subCollections) {
                ApiFuture<DocumentSnapshot> promise = subRef.get();
                try {
                    DocumentSnapshot docSnapshot = promise.get();
                    if (docSnapshot.exists()) {
                        Long amountLong = docSnapshot.getLong("amount");
                        int amount = 0;
                        if (amountLong != null) {
                            amount = amountLong.intValue();
                        }
                        products.put(subRef.getId(), amount);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    NotificationManager.showExceptionError(e);
                }
            }

            //add date from main collection
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if (docSnapshot.exists()) {
                    Date d = docSnapshot.getDate("date");
                    if (d != null) {
                        Sale s = new Sale(products, d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                        result.addToSalesList(s);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                NotificationManager.showExceptionError(e);
            }
        }
        return result;
    }


    /**
     * Make a list of all the suppliers
     *
     * @return SupplierList with all suppliers in firebase
     */
    public SupplierList retrieveAllSuppliers() {
        SupplierList result = new SupplierList();
        Iterable<DocumentReference> collections = db.collection("Suppliers").listDocuments();
        for (DocumentReference collRef : collections) {
            //add subcollection to arraylist
            ArrayList<Product> products = new ArrayList<>();
            Iterable<DocumentReference> subCollections = collRef.collection("products").listDocuments();
            for (DocumentReference subRef : subCollections) {
                ApiFuture<DocumentSnapshot> promise = subRef.get();
                try {
                    DocumentSnapshot docSnapshot = promise.get();
                    if (docSnapshot.exists()) {
                        String id = subRef.getId();
                        Product product = OrderTool.getProducts().get(id);
                        products.add(product);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    NotificationManager.showExceptionError(e);
                }
            }

            //add date from main collection
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if (docSnapshot.exists()) {
                    Double avgDouble = docSnapshot.getDouble("avgDeliveryTime");
                    int avg = 0;
                    if (avgDouble != null) {
                        avg = avgDouble.intValue();
                    }
                    String n = docSnapshot.getString("name");
                    Supplier s = new Supplier(n, avg);
                    int i;
                    for (i = 0; i < products.size(); i++) {
                        s.addProduct(products.get(i));
                    }
                    result.add(collRef.getId(), s);
                }
            } catch (InterruptedException | ExecutionException e) {
                NotificationManager.showExceptionError(e);
            }
        }
        return result;
    }

    /**
     * Returns the amount of products in stock in certain YearMonth according to the db
     * Goes through the database to retrieve years and months for each product
     * For each month the product's supply is behind retrieved
     * This will be used in the graph and table of the program
     *
     * @param productId the id of the product to get the stock quantity history of
     * @return hashmap with the YearMonth of the product's history as key and the amount of units in stock at that point as value
     */
    public Map<YearMonth, Integer> getProductHistoryQuantity(String productId) {
        Map<YearMonth, Integer> result = new HashMap<>();
        Iterable<DocumentReference> years = db.collection("Products").document(productId).collection("History").listDocuments();
        for (DocumentReference yearDocReference : years) {
            Iterable<DocumentReference> months = yearDocReference.collection("Months").listDocuments();
            for (DocumentReference monthDocReference : months) {
                ApiFuture<DocumentSnapshot> promise = monthDocReference.get();
                try {
                    DocumentSnapshot docSnapshot = promise.get();
                    if (docSnapshot.exists()) {
                        try {
                            int month = Integer.parseInt(monthDocReference.getId());
                            int year = Integer.parseInt(yearDocReference.getId());
                            Long quantity = docSnapshot.getLong("quantity");
                            if (quantity != null) {
                                result.put(YearMonth.of(year, month), Math.toIntExact(quantity));
                            }
                        } catch (NumberFormatException e) {
                            NotificationManager.show(new Notification(NotificationType.WARNING, "The database contains an invalid value. Please check if the database formatted correctly."));
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    NotificationManager.showExceptionError(e);
                }
            }
        }
        return result;
    }

    /**
     * Retrieves all recommendations from the database
     * Suppliers and products need to be loaded before recommendations!
     *
     * @return recommendationList containing all recommendations from firebase
     */
    public RecommendationList getRecommendations() {
        SupplierList suppliers = OrderTool.getSuppliers();
        ProductList products = OrderTool.getProducts();
        RecommendationList result = new RecommendationList();
        for (Map.Entry<String, Supplier> supplierEntry : suppliers.getSuppliers().entrySet()) {
            String supplierKey = supplierEntry.getKey();
            Supplier supplier = supplierEntry.getValue();
            Iterable<DocumentReference> yearDocumentReferences = db.collection("Suppliers").document(supplierKey).collection("recommendations").listDocuments();
            for (DocumentReference yearDocumentReference : yearDocumentReferences) {
                int year = Integer.parseInt(yearDocumentReference.getId());
                Iterable<DocumentReference> monthDocumentReferences = yearDocumentReference.collection("months").listDocuments();
                for (DocumentReference monthDocumentReference : monthDocumentReferences) {
                    int month = Integer.parseInt(monthDocumentReference.getId());
                    ApiFuture<DocumentSnapshot> monthDoc = monthDocumentReference.get();
                    Date creationDate = null;
                    Boolean isConfirmed = true;
                    try {
                        com.google.cloud.Timestamp timestamp = (com.google.cloud.Timestamp) monthDoc.get().get("creationDate");
                        isConfirmed = (Boolean) monthDoc.get().get("isConfirmed");
                        if (timestamp != null) {
                            creationDate = timestamp.toDate();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        NotificationManager.showExceptionError(e);
                    }
                    LocalDateTime creationLocalDateTime = null;
                    if (creationDate != null) {
                        creationLocalDateTime = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                    Recommendation rec = new Recommendation(supplier, YearMonth.of(year, month), creationLocalDateTime);
                    if (isConfirmed != null) {
                        rec.setConfirmed(isConfirmed);
                    }
                    Iterable<DocumentReference> productDocumentReferences = monthDocumentReference.collection("products").listDocuments();
                    for (DocumentReference productDocumentReference : productDocumentReferences) {
                        String productID = productDocumentReference.getId();
                        Integer productQuantity = null;
                        try {
                            ApiFuture<DocumentSnapshot> promise = productDocumentReference.get();
                            DocumentSnapshot docSnapshot = promise.get();
                            Long quantityLong = docSnapshot.getLong("quantity");
                            if (quantityLong != null) {
                                productQuantity = quantityLong.intValue();
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            NotificationManager.showExceptionError(e);
                        }
                        if (productQuantity != null && productQuantity != 0) {
                            Product p = products.get(productID);
                            rec.addProductToRecommendation(p, productQuantity);
                        }
                    }
                    result.addRecommendation(rec);
                }
            }
        }
        return result;
    }

    /**
     * Set a recommendation to confirmed or not confirmed in the database, without overwriting the entire entry
     *
     * @param recommendation the recommendation to change the confirmed field of
     * @param isConfirmed    value to set the confirmed field to
     */
    public void confirmRecommendation(Recommendation recommendation, boolean isConfirmed) {
        //get key of the supplier
        String supplierKey = null;
        for (Map.Entry<String, Supplier> supplierEntry : OrderTool.getSuppliers().getSuppliers().entrySet()) {
            if (supplierEntry.getValue() == recommendation.getSupplier()) {
                supplierKey = supplierEntry.getKey();
                break;
            }
        }
        if (supplierKey == null) {
            NotificationManager.show(new Notification(NotificationType.ERROR, "This recommendation is coupled to a supplier that doesn't exist."));
            return;
        }
        YearMonth yearMonthToOrderFor = recommendation.getYearMonthToOrderFor();
        ApiFuture<DocumentSnapshot> promise = db.collection("Suppliers").document(supplierKey).collection("recommendations").document(Integer.toString(yearMonthToOrderFor.getYear())).collection("months").document(Integer.toString(yearMonthToOrderFor.getMonthValue())).get();
        try {
            Map<String, Object> data = promise.get().getData();
            if (data != null) {
                data.put("isConfirmed", isConfirmed);
                db.collection("Suppliers").document(supplierKey).collection("recommendations").document(Integer.toString(yearMonthToOrderFor.getYear())).collection("months").document(Integer.toString(yearMonthToOrderFor.getMonthValue())).set(data).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a recommendationList to the database. Does not overwrite if a recommendation of the Supplier and YearMonth already exist.
     *
     * @param recommendationList recommendations to add
     */
    public void addRecommendations(RecommendationList recommendationList) {
        for (Recommendation recommendation : recommendationList.getRecommendations()) {
            //get key of the supplier
            String supplierKey = null;
            for (Map.Entry<String, Supplier> supplierEntry : OrderTool.getSuppliers().getSuppliers().entrySet()) {
                if (supplierEntry.getValue() == recommendation.getSupplier()) {
                    supplierKey = supplierEntry.getKey();
                    break;
                }
            }
            if (supplierKey == null) {
                break;
            }
            YearMonth date = recommendation.getYearMonthToOrderFor();
            Map<String, Object> data = new HashMap<>();
            LocalDateTime creationLocalDateTime = recommendation.getCreationDate();
            Date creationDate = Date.from(creationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            data.put("creationDate", creationDate);
            data.put("isConfirmed", false);
            //don't overwrite existing documents
            ApiFuture<DocumentSnapshot> promise = db.collection("Suppliers").document(supplierKey).collection("recommendations").document(Integer.toString(date.getYear())).collection("months").document(Integer.toString(date.getMonthValue())).get();
            try {
                DocumentSnapshot doc = promise.get();
                if (!doc.exists()) {
                    db.collection("Suppliers").document(supplierKey).collection("recommendations").document(Integer.toString(date.getYear())).collection("months").document(Integer.toString(date.getMonthValue())).set(data);
                    for (Map.Entry<Product, Integer> entry : recommendation.getProductRecommendation().entrySet()) {
                        data.clear();
                        String productID = OrderTool.getProducts().getIDbyProduct(entry.getKey());
                        data.put("quantity", entry.getValue());
                        ApiFuture<WriteResult> writeResult = db.collection("Suppliers").document(supplierKey).collection("recommendations").document(Integer.toString(date.getYear())).collection("months").document(Integer.toString(date.getMonthValue())).collection("products").document(productID).set(data);
                        //wait until the write is finished
                        writeResult.get();
                    }
                    NotificationManager.show(new Notification(NotificationType.INFO, "Recommendation Added"));
                }
            } catch (ExecutionException e) {
                NotificationManager.showExceptionError(e);
            } catch (InterruptedException e) {
                NotificationManager.show(new Notification(NotificationType.INFO, "File export was canceled"));
            }
        }
    }
}
