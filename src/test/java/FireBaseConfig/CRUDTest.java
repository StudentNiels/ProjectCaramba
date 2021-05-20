package FireBaseConfig;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class CRUDTest {

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
    }

    /**
     * A connection method to prevent duplicate code
     *
     * @return
     */
    public Firestore dbConnect() {
        db = FirestoreClient.getFirestore();
        return db;
    }

    /**
     * A method to setup a product to be added to the database
     * param attention required - starts out FALSE
     *
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
     * Adds a document of information about a product to the database
     *
     * @param db
     * @param productDocument
     * @param docData
     * @throws Exception
     */
    public String addProductDocument(Firestore db, String productDocument, Map docData) throws Exception {
        FireStoreConfig config = new FireStoreConfig();
        config.fireStoreConfig();
        ApiFuture<WriteResult> future = db.collection("Products").document(productDocument).set(docData);
        return ("Update time : " + future.get().getUpdateTime());
    }

    /**
     *
     */
    public Map readFromDB(Firestore db, String collection, String documentNumber) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(documentNumber);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        }
        else {
            System.out.println("No such document!");
        }
        return document.getData();
    }

    @Test
    @DisplayName("create")
    public void checkProduct() throws Exception {
        fireStoreConfig();
        db = dbConnect();
        Map setup1 = setupProductDocument("Winter", 50, "Anti vries", "123459", 150);
        addProductDocument(db, "1234", setup1);
        Map dbInsert = readFromDB(db, "Products", "1234");
        assertEquals(setup1, dbInsert);
        System.out.println("test uitgevoerd");
    }
}