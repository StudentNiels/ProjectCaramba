import com.caramba.ordertool.FireStoreConfig;
import com.google.firebase.database.*;
import org.apache.commons.math3.analysis.function.Log;


public class BuyProducts {
    final FirebaseDatabase database;
    DatabaseReference ref;

    private int supply;

    public BuyProducts() {
        // Get a reference to our posts
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("/Products/");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BuyProducts buyProducts = dataSnapshot.getValue(BuyProducts.class);
                System.out.println(buyProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void readData() {
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BuyProducts newBuyProducts = dataSnapshot.getValue(BuyProducts.class);
                System.out.println("Supply: " + newBuyProducts.supply);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
