package com.caramba.orderlistgen;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Command line jar that allows the user to edit and view a json with vendors
 */
public class VendorManager {
    private static File vendorsFile = new File("json/vendors.json");
    private static VendorList vendorlist = new VendorList();
    public static void main(String[] args){
        //create json if it doesn't exist
        try{
            vendorsFile.getParentFile().mkdirs();
            if(vendorsFile.createNewFile()){
                System.out.println("Created a new json file");
            }
        } catch (Exception e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
            System.exit(1);
        }
        if(args.length == 0){
            //behaviour when no arguments are given
            printVendors();
        }else{
            switch (args[0]) {
                case "help" -> showHelp();
                case "add" -> addVendor(args);
                case "rm" -> removeVendor(args);
                case "clear" -> removeAllVendors();
                case "edit" -> editVendor(args);
                default -> System.out.println("ERROR: Unknown argument " + args[0] + ". Use --help to see supported arguments");
            }
        }
    }

    /**
     * Loads the json to the vendorlist object
     */
    private static void loadJson(){
    }

    private static void updateJson(){
    }

    private static void removeAllVendors() {
        vendorlist.clear();
    }

    private static void printVendors() {
        if(vendorlist.count() == 0){
            System.out.println("The vendor list is empty");
        }else{
            System.out.println("The following vendors are registered:\n");
            for (int i = 0; i < vendorlist.count(); i++){
                Vendor v = vendorlist.getVendor(i);
                System.out.println("#" + i + " Name: " + v.getName() + " Estimated delivery time: " + v.getDeliveryTime());
            }
        }
    }

    private static void editVendor(String[] args) {
        throw new UnsupportedOperationException();
    }

    private static void removeVendor(String[] args) {
        if(args.length >= 2){
            try {
                int i = Integer.parseInt(args[1]) - 1;
                Vendor v = vendorlist.getVendor(i);
                if(v == null){
                    System.out.println("Vendor #" + i + " does not exist");
                }else{
                    vendorlist.remove(i);
                }
            }catch(NumberFormatException e){
                printInvalidNumError(args[1]);
            }
        }else{
            printArgumentError();
        }
    }

    private static void addVendor(String[] args) {
        if(args.length >= 3){
            String name = args[1];
            try{
                int deliverytime = Integer.parseInt(args[2]);
                vendorlist.addNew(name, deliverytime);
                System.out.println(name + " was added to the vendor list");
                updateJson();
            }catch (NumberFormatException e){
                printInvalidNumError(args[2]);
            }
        }else{
            printArgumentError();
        }
    }

    private static void showHelp(){
        throw new UnsupportedOperationException();
    }

    private static void printArgumentError(){
        System.out.println("ERROR: Invalid number of arguments. Use --help to see supported arguments");
    }

    private static void printInvalidNumError(String s){
        System.out.println("ERROR: '" + s + "' is not a valid number");
    }
}
