package com.caramba.orderlistgen;

import java.io.File;
import java.io.IOException;

/**
 * Command line jar that allows the user to edit and view a json with vendors
 */
public class VendorManager {
    public static void main(String[] args){
        //create json if it doesn't exist
        File vendorsFile = new File("json/vendors.json");
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
                default -> System.out.println("Unknown argument " + args[0] + ". Use --help to see supported arguments");
            }
        }
    }

    private static void removeAllVendors() {
        throw new UnsupportedOperationException();
    }

    private static void printVendors() {
        throw new UnsupportedOperationException();
    }

    private static void editVendor(String[] args) {
        throw new UnsupportedOperationException();
    }

    private static void removeVendor(String[] args) {
        throw new UnsupportedOperationException();
    }

    private static void addVendor(String[] args) {
        throw new UnsupportedOperationException();
    }

    private static void showHelp(){
        throw new UnsupportedOperationException();
    }

}
