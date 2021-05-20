package FireBaseConfig;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.text.Document;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConfig {

    private Firestore db;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The SDK of Firebase Admin is implemented here, a json file with credentials is already present (car-nl-firebase-adminsdk-6aga3-db41e98ceb.json)
     */
    public void fireStoreConfig() throws Exception {

        FileInputStream serviceAccount = new FileInputStream("./././car-nl-firebase-adminsdk-6aga3-db41e98ceb.json");

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(options);

        //region Template interactions
        /*
        //Create product document
        Map setupP = setupProductDocument("Zomer", 10, "Naturado Onbemeste Tuinaarde 20 liter", "1234568", 60);
        addProductDocument("Products", "1235", setupP);

        //Create sales document
        Map setupS = setupSalesDocument("1234568", getTimeStamp());
        addSalesDocument("1234", setupS);

        //Create supplier document
        Map setupSup = setupSuppliersDocument(21, "Bremen");
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
     * @param categorie_tag
     * @param min_supply
     * @param product_descript
     * @param product_nr
     * @param supply
     * @return the hashmap which can be added to the database with the correct method
     */
    public Map setupProductDocument(String categorie_tag, int min_supply, String product_descript, String product_nr, int supply) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("Attention_Required", false);
        docData.put("Categorie_Tag", categorie_tag);
        docData.put("Min_Supply", min_supply);
        docData.put("Product_Descript", product_descript);
        docData.put("Product_NR", product_nr);
        docData.put("Supply", supply);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @param product_nr
     * @return the hashmap which can be added to the database with the correct method
     */
    public Map setupSalesDocument(String product_nr, String timeStamp) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("Product_NR", product_nr);
        docData.put("Sell_Date", timeStamp);
        return docData;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     * @param AVG_DeliveryTime
     * @param Supplier_Name
     * @return the hashmap which can be added to the database with the correct method
     */
    public Map setupSuppliersDocument(int AVG_DeliveryTime, String Supplier_Name) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("AVG_DeliveryTime", AVG_DeliveryTime);
        docData.put("Supplier_Name", Supplier_Name);
        return docData;
    }

    /**
     * Adds a document of information about a product to the database
     * @param productDocument
     * @param docData
     * @throws Exception
     */
    public void addProductDocument(String collection, String productDocument, Map docData) throws Exception {
        dbConnect();
        ApiFuture<WriteResult> future = db.collection(collection).document(productDocument).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
        db.close();
    }

    /**
     * Adds a document of information about a product to the database
     * @param docData
     * @throws Exception
     */
    public void addSalesDocument(String salesDocument, Map docData) throws Exception {
        dbConnect();
        ApiFuture<WriteResult> future = db.collection("Sales").document(salesDocument).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
        db.close();
    }

    /**
     * Adds a document of information about a product to the database
     * @param suppliersDocument insert the supplier number
     * @throws Exception
     */
    public void addSuppliersDocument(String suppliersDocument, Map docData) throws Exception {
        dbConnect();
        ApiFuture<WriteResult> future = db.collection("Suppliers").document(suppliersDocument).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
        db.close();
    }

    /**
     * Make a list of all the products
     * @return
     */
    public Iterable<DocumentReference> retrieveAllProducts() throws Exception {
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Products").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections) {
            i++;
            System.out.println("Product ID: " + collRef.getId());
        }
        System.out.println(i);
        db.close();
        return collections;
    }

    /**
     * Make a list of all the Sales
     * @return
     */
    public Iterable<DocumentReference> retrieveAllSales() throws Exception {
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Sales").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections)
        {
            i++;
            System.out.println("Sale ID: " + collRef.getId());
        }
        System.out.println(i);
        db.close();
        return collections;
    }

    /**
     * Make a list of all the suppliers
     * @return
     */
    public Iterable<DocumentReference> retrieveAllSuppliers() throws Exception {
        dbConnect();
        Iterable<DocumentReference> collections = db.collection("Suppliers").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections)
        {
            i++;
            System.out.println("Supplier ID: " + collRef.getId());
        }
        System.out.println(i);
        db.close();
        return collections;
    }

    /**
     * Read out a specific product, product's sales date or supplier
     * @param collection
     * @param documentNumber
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void readFromDB(String collection, String documentNumber) throws Exception {
        dbConnect();
        DocumentReference docRef = db.collection(collection).document(documentNumber);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        try {
            System.out.println("Document: " + document.getData());
        } catch(Exception e) {
            System.out.println("No document found");
        }
        db.close();
    }

    /**
     * Update a field inside a product or supplier or sales data
     * @param collectionName
     * @param documentNumber
     * @param fieldType
     * @param value
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void updateDocument(String collectionName, String documentNumber, String fieldType, Object value) throws Exception {
    dbConnect();
    DocumentReference docRef = db.collection(collectionName).document(documentNumber);
    Map<String, Object> update = new HashMap<>();
    update.put(fieldType, value);

    ApiFuture<WriteResult> updateData = docRef.update(update);
        System.out.println("Update time : " + updateData.get().getUpdateTime());
        db.close();
    }

    /**
     * Delete a document from one of the collections
     * @param collection
     * @param documentNumber
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void deleteDocument(String collection, String documentNumber) throws Exception {
    dbConnect();
    ApiFuture<WriteResult> writeResult = db.collection(collection).document(documentNumber).delete();
        System.out.println("Update time : " + writeResult.get().getUpdateTime());
        db.close();
    }

    /**
     * Delete a collection in batches to prevent out-of-memory errors
     * @param collection
     * @param batchSize input how many of the collection should be deleted
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void deleteCollection(String collection, int batchSize) throws Exception {
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
        db.close();
    }
}
