package com.caramba.orderlistgen;


import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Command line jar that allows the user to edit and view a json with vendors
 */
public class VendorManager {

    private static File vendorsFile = new File("json/vendors.json");
    private static VendorList vendorlist = new VendorList();
    private static ObjectMapper objectmapper = new ObjectMapper();

    public static void main(String[] args){
        loadJson();
        if(args.length == 0){
            //behaviour when no arguments are given
            printVendors();
        }else{
            switch (args[0]) {
                case "help" -> showHelp();
                case "add" -> addVendor(args);
                case "rm" -> removeVendor(args);
                case "clear" -> removeAllVendors();
                default -> System.out.println("ERROR: Unknown argument " + args[0] + ". Use --help to see supported arguments");
            }
        }
    }

    /**
     * Loads the json to the vendorlist object and creates a new json file if it doesn't exist.
     */
    private static void loadJson(){
        //create json if it doesn't exist
        try{
            vendorsFile.getParentFile().mkdirs();
            if(vendorsFile.createNewFile()){
                System.out.println("Created a new json file");
                updateJson();
            }
        } catch (IOException | SecurityException e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
            System.exit(1);
        }
        try{
            vendorlist = objectmapper.readValue(vendorsFile, VendorList.class);
        }catch(IOException e){
            System.out.println("ERROR: failed to read json file");
            e.printStackTrace();
        }
    }

    /**
     * Writes vendorlist to the json file
     */
    private static void updateJson(){
        try {
            objectmapper.writeValue(vendorsFile, vendorlist);
        } catch (IOException e) {
            System.out.println("ERROR: failed to write json file");
            e.printStackTrace();
        }
    }

    /**
     * Clears the vendor list
     */
    private static void removeAllVendors() {
        vendorlist.clear();
        updateJson();
    }

    /**
     * Prints the vendor information of each vendor on the list
     */
    private static void printVendors() {
        if(vendorlist.count() == 0){
            System.out.println("The vendor list is empty");
        }else{
            System.out.println("The following vendors are registered:\n");
            for (int i = 0; i < vendorlist.count(); i++){
                Vendor v = vendorlist.getVendor(i);
                System.out.println("| #" + i + " | Name: " + v.getName() + " | Estimated delivery time: " + v.getDeliveryTime() + " |");
            }
        }
    }

    private static void removeVendor(String[] args) {
        if(args.length >= 2){
            try {
                int i = Integer.parseInt(args[1]);
                Vendor v = vendorlist.getVendor(i);
                if(v == null){
                    System.out.println("Vendor #" + i + " does not exist");
                }else{
                    System.out.println(v.getName() + "was deleted");
                    vendorlist.remove(i);
                    updateJson();
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
        System.out.println("=========================");
    }

    private static void printArgumentError(){
        System.out.println("ERROR: Invalid number of arguments. Use --help to see supported arguments");
    }

    private static void printInvalidNumError(String s){
        System.out.println("ERROR: '" + s + "' is not a valid number");
    }
}
