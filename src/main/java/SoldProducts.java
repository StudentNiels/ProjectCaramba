import java.util.*;

public class SoldProducts {


    HashMap<Integer, String> productsSold;
    HashMap<String, HashMap<Integer, String>> soldProductsPerMonth;

    Date date;
    Calendar cal;


    public SoldProducts() {
        productsSold = new HashMap<Integer, String>();
        soldProductsPerMonth = new HashMap<String, HashMap<Integer, String>>();

        date = new Date();
        cal = Calendar.getInstance();

        fillsoldProductsPerMonthMap();
        printMap();
        AverageSellsTwoMonths();
    }


    public void fillsoldProductsPerMonthMap() {

        productsSold = new HashMap<Integer, String>();
        productsSold.put(19, "Antenneplakkers");
        soldProductsPerMonth.put("Apr", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(25, "Antenneplakkers");
        soldProductsPerMonth.put("May", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(35, "Antenneplakkers");
        soldProductsPerMonth.put("Jun", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(23, "Antenneplakkers");
        soldProductsPerMonth.put("Jul", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(22, "Antenneplakkers");
        soldProductsPerMonth.put("Aug", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(31, "Antenneplakkers");
        soldProductsPerMonth.put("Sep", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(14, "Antenneplakkers");
        soldProductsPerMonth.put("Oct", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(16, "Antenneplakkers");
        soldProductsPerMonth.put("Nov", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(20, "Antenneplakkers");
        soldProductsPerMonth.put("Dec", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(35, "Antenneplakkers");
        soldProductsPerMonth.put("Jan", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(40, "Antenneplakkers");
        soldProductsPerMonth.put("Feb", productsSold);

        productsSold = new HashMap<Integer, String>();
        productsSold.put(34, "Antenneplakkers");
        soldProductsPerMonth.put("Mar", productsSold);
    }

    public void printMap() {
        for (String name : soldProductsPerMonth.keySet()) {
            String key = name.toString();
            String value = soldProductsPerMonth.get(name).toString();
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
            case 0:
                thisMonth = "January";
                sellsMonthOne = soldProductsPerMonth.get("Nov").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Dec").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average+ "!");
                break;
            case 1:
                thisMonth = "February";
                sellsMonthOne = soldProductsPerMonth.get("Dec").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Jan").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average+ "!");
                break;
            case 2:
                thisMonth = "March";
                sellsMonthOne = soldProductsPerMonth.get("Jan").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Feb").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average+ "!");
                break;
            case 3:
                thisMonth = "April";
                sellsMonthOne = soldProductsPerMonth.get("Feb").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Mar").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average + "!");
                break;
            case 4:
                thisMonth = "May";
                sellsMonthOne = soldProductsPerMonth.get("Mar").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Apr").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 5:
                thisMonth = "June";
                sellsMonthOne = soldProductsPerMonth.get("Apr").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("May").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 6:
                thisMonth = "Juli";
                sellsMonthOne = soldProductsPerMonth.get("May").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Jun").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 7:
                thisMonth = "Augustus";
                sellsMonthOne = soldProductsPerMonth.get("Jun").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Jul").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 8:
                thisMonth = "September";
                sellsMonthOne = soldProductsPerMonth.get("Jul").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Aug").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 9:
                thisMonth = "October";
                sellsMonthOne = soldProductsPerMonth.get("Aug").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Sep").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 10:
                thisMonth = "November";
                sellsMonthOne = soldProductsPerMonth.get("Sep").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Oct").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
            case 11:
                thisMonth = "December";
                sellsMonthOne = soldProductsPerMonth.get("Oct").keySet().hashCode();
                sellsMonthTwo = soldProductsPerMonth.get("Nov").keySet().hashCode();
                average = (sellsMonthOne + sellsMonthTwo) / 2;

                System.out.println("It is " + thisMonth + ". Average sells over two months is " + average);
                break;
        }
    }
}
