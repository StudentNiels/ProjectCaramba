package FireBaseConfig;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException
    {
        FireStoreConfig config = new FireStoreConfig();
        config.fireStoreConfig();
    }
}
