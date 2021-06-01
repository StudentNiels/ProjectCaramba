import com.caramba.ordertool.Application;
import com.caramba.ordertool.FireStoreConfig;

public class BuyProducts {

    SoldProducts soldProducts;
    Application application;

    private static final FireStoreConfig config = new FireStoreConfig();

    public BuyProducts() {
        config.fireStoreConfig();

    }

    public void retrieveSupply() {

    }

}
