package com.caramba.ordertool;

import com.caramba.ordertool.notifications.NotificationManager;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FireStoreConfig {

    private Firestore db;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The SDK of Firebase Admin is implemented here, a json file with credentials is already present (car-nl-firebase-adminsdk-6aga3-db41e98ceb.json)
     */
    public void fireStoreConfig(){
        try {
            FileInputStream serviceAccount = new FileInputStream("./././car-nl-firebase-adminsdk-6aga3-db41e98ceb.json");
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
            FirebaseApp.initializeApp(options);
        }
        catch (IOException e) {
            NotificationManager.addExceptionError(e);
        }

        //region Template interactions
        /*
        //Create product document
        HashMap<String, Object> setupP = setupProductDocument("Zomer", 10, "Naturado Onbemeste Tuinaarde 20 liter", "1234568", 60);
        addProductDocument("Products", "1236", setupP);

        //Create sales document
        HashMap<String, Object> setupS = setupSalesDocument("1234568", getTimeStamp());
        addSalesDocument("1234", setupS);

        //Create supplier document
        HashMap<String, Object> setupSup = setupSuppliersDocument(21, "Bremen");
        addSuppliersDocument("00002", setupSup);

        //Read individual
        readFromDB("Products", "123456");

        //List all products or sales or suppliers
        retrieveAllProducts();
        retrieveAllSales();
        retrieveAllSuppliers();

        //Update a field of a product, sale, supplier
        updateDocument("Products", "1234", "Supply", 70);

        //Delete a document from a collection
        deleteDocument("Sales", "1234");

        //Delete an entire collection in batches
        deleteCollection("test", 1);
        */
        // endregion

        //Read individual
        //readFromDB("Products", "c1dd2174-c207-11eb-8529-0242ac130003");
    }


    /**
     * A connection method to create an often used variable to prevent duplicate code
     * @return the database data which is use by most of the methods in the class
     */
    public Firestore dbConnect() {
        db = FirestoreClient.getFirestore();
        return db;
    }

    /**
     * Get timestamp in a fitting format
     */
    private String getTimeStamp(){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return dateFormat.format(timestamp);
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @return the hashmap which can be added to the database with the correct method
     */
    public HashMap setupProductDocument(String description, String productNum, int quantity) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("description", description);
        docData.put("productNum", productNum);
        docData.put("quantity", quantity);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @return the hashmap which can be added to the database with the correct method
     */
    public HashMap<String, Object> setupSalesDocument(String product_nr, String timeStamp) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("Product_NR", product_nr);
        docData.put("Sell_Date", timeStamp);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @return the hashmap which can be added to the database with the correct method
     */
    public HashMap setupSuppliersDocument(int avgDeliveryTime, String name) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("avgDeliveryTime", avgDeliveryTime);
        docData.put("name", name);
        return docData;
    }

    /**
     * Adds a document of information about a product to the database
     * @param productDocument
     * @param docData
     * 
     */
    public void addProductDocument(String collection, String productDocument, HashMap docData){
        dbConnect();
        ApiFuture<WriteResult> future = db.collection(collection).document(productDocument).set(docData);
        try{
            System.out.println("Update time : " + future.get().getUpdateTime());
        }catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * Adds a document of information about a product to the database
     * 
     */
    public void addSalesDocument(String salesDocument, HashMap docData) {
        dbConnect();
        ApiFuture<WriteResult> future = db.collection("Sales").document(salesDocument).set(docData);
        try {
            System.out.println("Update time : " + future.get().getUpdateTime());
        }catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * Adds a document of information about a product to the database
     * @param suppliersDocument insert the supplier number
     * 
     */
    public void addSuppliersDocument(String suppliersDocument, HashMap docData){
        dbConnect();
        ApiFuture<WriteResult> future = db.collection("Suppliers").document(suppliersDocument).set(docData);
        try{
            System.out.println("Update time : " + future.get().getUpdateTime());
        }catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * Make a list of all the products
     */
    public ProductList retrieveAllProducts(){
        ProductList result = new ProductList();
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Products").listDocuments();
        for (DocumentReference collRef : collections) {
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if(docSnapshot.exists()){
                    Product p = docSnapshot.toObject(Product.class);
                    result.add(collRef.getId(), p);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        closeDb();
        return result;
    }

    /**
     * Make a list of all the Sales
     */
    public Saleslist retrieveAllSales(){
        Saleslist result = new Saleslist();
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Sales").listDocuments();
        for (DocumentReference collRef : collections)
        {
            //add subcollection to hashmap
            HashMap<String, Integer> products = new HashMap<>();
            Iterable<DocumentReference> subCollections = collRef.collection("SalesList").listDocuments();
            for(DocumentReference subRef : subCollections){
                ApiFuture<DocumentSnapshot> promise = subRef.get();
                try{
                    DocumentSnapshot docSnapshot = promise.get();
                    if(docSnapshot.exists()){
                        products.put(subRef.getId(), docSnapshot.getLong("amount").intValue());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            //add date from main collection
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if(docSnapshot.exists()){
                    Date d = docSnapshot.getDate("date");
                    Sale s = new Sale(products, d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    result.addToSalesList(s);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        closeDb();
        return result;
    }

    /**
     * Make a list of all the suppliers
     */
    public SupplierList retrieveAllSuppliers(){
        SupplierList result = new SupplierList();
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Suppliers").listDocuments();
        for (DocumentReference collRef : collections)
        {
            //add subcollection to arraylist
            ArrayList<Product> products = new ArrayList<>();
            Iterable<DocumentReference> subCollections = collRef.collection("products").listDocuments();
            for (DocumentReference subRef : subCollections){
                ApiFuture<DocumentSnapshot> promise = subRef.get();
                try{
                    DocumentSnapshot docSnapshot = promise.get();
                    if(docSnapshot.exists()){
                        String id = subRef.getId();
                        Product product = OrderTool.getProducts().get(id);
                        products.add(product);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            //add date from main collection
            ApiFuture<DocumentSnapshot> promise = collRef.get();
            try {
                DocumentSnapshot docSnapshot = promise.get();
                if(docSnapshot.exists()){
                    Integer avg = docSnapshot.getDouble("avgDeliveryTime").intValue();
                    String n = docSnapshot.getString("name");
                    Supplier s = new Supplier(n, avg);
                    int i;
                    for(i = 0; i < products.size();i++)
                    {
                        s.addProduct(products.get(i));
                    }
                    result.add(collRef.getId(), s);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        closeDb();
        return result;
    }

    /**
     * Read out a specific product, product's sales date or supplier
     */
    public void readFromDB(String collection, String documentNumber){
        dbConnect();
        DocumentReference docRef = db.collection(collection).document(documentNumber);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        try {
            System.out.println("Document: " + document.getData());
        } catch(Exception e) {
            System.out.println("No document found");
        }
        closeDb();
    }

    /**
     * Update a field inside a product or supplier or sales data
     */
    public void updateDocument(String collectionName, String documentNumber, String fieldType, Object value){
    dbConnect();
    DocumentReference docRef = db.collection(collectionName).document(documentNumber);
    HashMap<String, Object> update = new HashMap<>();
    update.put(fieldType, value);

    ApiFuture<WriteResult> updateData = docRef.update(update);
        try {
            System.out.println("Update time : " + updateData.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * Delete a document from one of the collections
     */
    public void deleteDocument(String collection, String documentNumber){
    dbConnect();
    ApiFuture<WriteResult> writeResult = db.collection(collection).document(documentNumber).delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * Delete a collection in batches to prevent out-of-memory errors
     */
    public void deleteCollection(String collection, int batchSize){
        try {
            dbConnect();
            CollectionReference colRef = db.collection(collection);
            // retrieve a small batch of documents to avoid out-of-memory errors
            ApiFuture<QuerySnapshot> future = colRef.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
        closeDb();
    }

    private void closeDb(){
        try{
            //db.close();
        }catch(Exception e){
            NotificationManager.addExceptionError(e);
        }
    }
}
