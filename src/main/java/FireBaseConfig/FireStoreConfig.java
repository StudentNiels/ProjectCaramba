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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConfig {
    public void fireStoreConfig() throws ExecutionException, InterruptedException, FileNotFoundException {
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

        readFromDB();
    }

    /**
     * todo: write a method that can write multiple hashmaps to the DB
     *
     */
    public void writeToDB() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("sampleData").document("inspiration");

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
     * todo: write a method that can read from the DB
     */
    public void readFromDB() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("sampleData").document("inspiration");

        //asynchronously write data
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        }
        else {
            System.out.println("No such document!");
        }
    }
}
