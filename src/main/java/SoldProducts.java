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
    }


    public void fillsoldProductsPerMonthMap() {

        productsSold = new HashMap<Integer, String>();
        productsSold.put(19, "Antenneplakkers");
        soldProdurctsPerMonth.put("Apr", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(25, "Antenneplakkers");
        soldProdurctsPerMonth.put("May", productsSold);

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
        soldProdurctsPerMonth.put("Oct", productsSold);

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

    public void AverageSellsTwoMonths() {
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        String thisMonth;
        int average;
        int sellsMonthOne;
        int sellsMonthTwo;

        switch (month) {
            case 1:
                thisMonth = "January";
                sellsMonthOne = soldProdurctsPerMonth.get("Nov").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Dec").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 2:
                thisMonth = "February";
                sellsMonthOne = soldProdurctsPerMonth.get("Dec").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Jan").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 3:
                thisMonth = "March";
                sellsMonthOne = soldProdurctsPerMonth.get("Jan").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Feb").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 4:
                thisMonth = "April";
                sellsMonthOne = soldProdurctsPerMonth.get("Feb").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Mar").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 5:
                thisMonth = "May";
                sellsMonthOne = soldProdurctsPerMonth.get("Mar").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Apr").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 6:
                thisMonth = "June";
                sellsMonthOne = soldProdurctsPerMonth.get("Apr").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("May").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 7:
                thisMonth = "Juli";
                sellsMonthOne = soldProdurctsPerMonth.get("May").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Jun").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 8:
                thisMonth = "Augustus";
                sellsMonthOne = soldProdurctsPerMonth.get("Jun").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Jul").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 9:
                thisMonth = "September";
                sellsMonthOne = soldProdurctsPerMonth.get("Jul").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Aug").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 10:
                thisMonth = "October";
                sellsMonthOne = soldProdurctsPerMonth.get("Aug").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Sep").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 11:
                thisMonth = "November";
                sellsMonthOne = soldProdurctsPerMonth.get("Sep").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Oct").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 12:
                thisMonth = "December";
                sellsMonthOne = soldProdurctsPerMonth.get("Oct").keySet().hashCode();
                sellsMonthTwo = soldProdurctsPerMonth.get("Nov").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
        }

    }

}
