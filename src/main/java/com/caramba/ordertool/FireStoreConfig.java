package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.NotificationManager;
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
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.caramba.ordertool.Application.getMainProductList;

public class FireStoreConfig {

    private Firestore db;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The SDK of Firebase Admin is implemented here, a json file with credentials is already present (car-nl-firebase-adminsdk-6aga3-db41e98ceb.json)
     */
    public void fireStoreConfig() throws ExecutionException, InterruptedException {
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
        HashMap<String, Object> setupS = setupSalesDocument("1234568", getTimeStamp(), 1);
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

        //Retrieve specific data from a collectiot_documentList
        retrieveSpecificProducts("Sales","Product_NR", "1234567");

        //Update a field of a product, sale, supplier
        updateDocument("Products", "1234", "Supply", 70);

        //Delete a document from a collection
        deleteDocument("Sales", "1234");

        //Delete an entire collection in batches
        deleteCollection("test", 1);
        */
        // endregion
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
    public HashMap setupProductDocument(String product_descript, TimePeriod timePeriod, String product_nr, int min_supply, int supply) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("Attention_Required", false);
        docData.put("Time period", timePeriod);
        docData.put("Min_Supply", min_supply);
        docData.put("Product_Descript", product_descript);
        docData.put("Product_NR", product_nr);
        docData.put("Supply", supply);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @return the hashmap which can be added to the database with the correct method
     */
    public HashMap<String, Object> setupSalesDocument(String product_nr, String timeStamp, int amount) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("Product_NR", product_nr);
        docData.put("Sell_Date", timeStamp);
        docData.put("Amount", amount);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @return the hashmap which can be added to the database with the correct method
     */
    public HashMap setupSuppliersDocument(int AVG_DeliveryTime, String Supplier_Name) {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("AVG_DeliveryTime", AVG_DeliveryTime);
        docData.put("Supplier_Name", Supplier_Name);
        return docData;
    }

    /**
     * Adds a document of information about a product to the database
     * @param docData
     * 
     */
    public void addProductDocument(HashMap docData){
        dbConnect();
        String collection = "Products";
        String productDocument = "productDocument";
        ApiFuture<WriteResult> future = db.collection(collection).document(productDocument).set(docData);
        try{
            System.out.println("Update time : " + future.get().getUpdateTime());
        }catch (InterruptedException | ExecutionException e) {
            NotificationManager.addExceptionError(e);
        }
        closeDb();
    }

    /**
     * TODO: Write method to add a productList to the DB
     */
    public void addProductListToDB()
    {
        //
        getMainProductList();
        //for (Map.Entry<UUID, Product> set : products.entrySet())
        //{
        //    addProductDocument(product.docData);
        //}
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
     * @return
     */
    public List<QueryDocumentSnapshot> retrieveAllProducts() throws ExecutionException, InterruptedException {
        dbConnect();
        ApiFuture<QuerySnapshot> future = db.collection("Products").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        //int i = 0;
        //for (DocumentSnapshot document : documents) {
        //    i++;
        //    System.out.println((i) + ". Product ID: " + document.getId() + document.toObject(Product.class));
        //}
        closeDb();
        return documents;
    }

    /**
     * Make a list of all the products
     */
    public void retrieveSpecificProducts(String selection, String selection2) throws ExecutionException, InterruptedException {
        dbConnect();
        CollectionReference products = db.collection("Products");
        Query query = products.whereEqualTo(selection, selection2);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        int i = 0;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            i++;
            System.out.println((i) + " " + document.getData());
        }
        System.out.println("Results : "+ i + " " + getTimeStamp());
        closeDb();
    }

    /**
     * Make a list of all the Sales
     */
    public void retrieveAllSales() throws ExecutionException, InterruptedException {
        dbConnect();
        ApiFuture<QuerySnapshot> future = db.collection("Sales").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        int i = 0;
        for (DocumentSnapshot document : documents) {
            i++;
            System.out.println((i) + ". Sales ID: " + document.getId());
        }
        closeDb();
    }

    /**
     * Make a list of all the suppliers
     */
    public void retrieveAllSuppliers() throws ExecutionException, InterruptedException {
        dbConnect();
        ApiFuture<QuerySnapshot> future = db.collection("Suppliers").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        int i = 0;
        for (DocumentSnapshot document : documents) {
            i++;
            System.out.println((i) + ". Supplier ID: " + document.getId());
        }
        closeDb();
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
            db.close();
        }catch(Exception e){
            NotificationManager.addExceptionError(e);
        }
    }
}
