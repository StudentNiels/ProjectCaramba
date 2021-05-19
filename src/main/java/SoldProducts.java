import java.text.SimpleDateFormat;
import java.util.*;

public class SoldProducts {


    HashMap<Integer, String> productsSold;
    HashMap<String, HashMap<Integer, String>> soldProdurctsPerMonth;

    Date date;
    Calendar cal;


    public SoldProducts() {
        productsSold = new HashMap<Integer, String>();
        soldProdurctsPerMonth = new HashMap<String, HashMap<Integer, String>>();

        date = new Date();
        cal = Calendar.getInstance();

        fillsoldProductsPerMonthMap();
        printMap();
        AvarageSellsTwoMonths();
        System.out.println();
    }


    public void fillsoldProductsPerMonthMap() {

        productsSold = new HashMap<Integer, String>();
        productsSold.put(19, "Antenneplakkers");
        soldProdurctsPerMonth.put("Apr", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(25, "Antenneplakkers");
        soldProdurctsPerMonth.put("Mei", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(35, "Antenneplakkers");
        soldProdurctsPerMonth.put("Jun", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(23, "Antenneplakkers");
        soldProdurctsPerMonth.put("Jul", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(22, "Antenneplakkers");
        soldProdurctsPerMonth.put("Aug", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(31, "Antenneplakkers");
        soldProdurctsPerMonth.put("Sep", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(14, "Antenneplakkers");
        soldProdurctsPerMonth.put("Okt", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(16, "Antenneplakkers");
        soldProdurctsPerMonth.put("Nov", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(20, "Antenneplakkers");
        soldProdurctsPerMonth.put("Dec", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(35, "Antenneplakkers");
        soldProdurctsPerMonth.put("Jan", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(40, "Antenneplakkers");
        soldProdurctsPerMonth.put("Feb", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(34, "Antenneplakkers");
        soldProdurctsPerMonth.put("Maa", productsSold);
    }

    public void printMap() {
        for (String name : soldProdurctsPerMonth.keySet()) {
            String key = name.toString();
            String value = soldProdurctsPerMonth.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    public void AvarageSellsTwoMonths() {
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        String thisMonth;

        switch (month) {
            case 1:
                thisMonth = "January";
                soldProdurctsPerMonth.get(0);
                break;
            case 2:
                thisMonth = "February";
                soldProdurctsPerMonth.get(1);
                break;
            case 3:
                thisMonth = "March";
                soldProdurctsPerMonth.get(2);
                break;
            case 4:
                thisMonth = "April";
                soldProdurctsPerMonth.get(3);
                break;
            case 5:
                thisMonth = "Mei";
                soldProdurctsPerMonth.get(4);
                break;
            case 6:
                thisMonth = "June";
                soldProdurctsPerMonth.get(5);
                break;
            case 7:
                thisMonth = "Juli";
                soldProdurctsPerMonth.get(6);
                break;
            case 8:
                thisMonth = "Augustus";
                soldProdurctsPerMonth.get(7);
                break;
            case 9:
                thisMonth = "September";
                soldProdurctsPerMonth.get(8);
                break;
            case 10:
                thisMonth = "October";
                soldProdurctsPerMonth.get(9);
                break;
            case 11:
                thisMonth = "November";
                soldProdurctsPerMonth.get(10);
                break;
            case 12:
                thisMonth = "December";
                soldProdurctsPerMonth.get(11);
                break;


        }

    }

}
