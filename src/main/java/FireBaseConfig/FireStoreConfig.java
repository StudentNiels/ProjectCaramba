package FireBaseConfig;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConfig {

    private Firestore db;

    public void fireStoreConfig() throws Exception {
        /**
         * The SDK of Firebase Admin is implemented here, a json file with credentials is already present (car-nl-firebase-adminsdk-6aga3-db41e98ceb.json)
         */
        FileInputStream serviceAccount = new FileInputStream("./././car-nl-firebase-adminsdk-6aga3-db41e98ceb.json");

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(options);

        dbConnect();
        Map setup1 = setupProductDocument("Zomer", 10, "Naturado Onbemeste Tuinaarde 20 liter", "1234568", 60);
        addProductDocument(db, "1234", setup1);
    }

    /**
     * A connection method to prevent duplicate code
     * @return
     */
    public Firestore dbConnect() {
        db = FirestoreClient.getFirestore();
        return db;
    }

    /**
     * todo: write a method that can write multiple hashmaps to the DB
     *
     */
    public void writeToDB(Firestore db) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Products").document("123");

        Map<String, String> data = new HashMap<>();
        data.put("first", "Tinus");
        data.put("last", "Lovelace");
        data.put("born", "1815");
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
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
    public Map setupProductDocument(String categorie_tag, int min_supply, String product_descript, String product_nr, int supply)
    {
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
     * Adds a document of information about a product to the database
     * @param db
     * @param productDocument
     * @param docData
     * @throws Exception
     */
    public void addProductDocument(Firestore db, String productDocument, Map docData) throws Exception {
        ApiFuture<WriteResult> future = db.collection("Products").document(productDocument).set(docData);
        System.out.println("Update time : " + future.get().getUpdateTime());
    }

    public Iterable<DocumentReference> retrieveAllProducts(Firestore db) {
        Iterable<DocumentReference> collections = db.collection("Products").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections) {
            i++;
            System.out.println("Product ID: " + collRef.getId());
        }
        System.out.println(i);
        return collections;
    }

    public Iterable<DocumentReference> retrieveAllSales(Firestore db) {
        Iterable<DocumentReference> collections = db.collection("Sales").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections)
        {
            i++;
            System.out.println("Sale ID: " + collRef.getId());
        }
        System.out.println(i);
        return collections;
    }

    public Iterable<DocumentReference> retrieveAllSuppliers(Firestore db) {
        Iterable<DocumentReference> collections = db.collection("Suppliers").listDocuments();
        int i = 0;
        for (DocumentReference collRef : collections)
        {
            i++;
            System.out.println("Supplier ID: " + collRef.getId());
        }
        System.out.println(i);
        return collections;
    }

    /**
     * todo: write a method that can read from the DB
     */
    public void readFromDB(Firestore db) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Products").document("123");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        }
        else {
            System.out.println("No such document!");
        }
    }
}
