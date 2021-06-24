package com.caramba.ordertool.scenes.displayModels;

import com.caramba.ordertool.Supplier;
import com.caramba.ordertool.SupplierList;

/**
 * model used by the main product overview table in the product view
 */
//getters are used by javafx, but IntelliJ doesn't recognize this, so we suppress the warnings
@SuppressWarnings("unused")
public class DisplayProduct {
    private final String internalId;
    private final String productNum;
    private final String description;
    private final int quantity;
    private final String supplierNames;

    public DisplayProduct(String internalId, String productNum, String description, int quantity, SupplierList suppliersSelling) {
        this.internalId = internalId;
        this.productNum = productNum;
        this.description = description;
        this.quantity = quantity;

        //Make a string of the names of the suppliers
        StringBuilder supplierNames = new StringBuilder();
        if (suppliersSelling.getSuppliers().isEmpty()) {
            supplierNames = new StringBuilder("Unknown supplier");
        } else {
            int i = 1;
            for (Supplier s : suppliersSelling.getSuppliers().values()) {
                //add a comma before every name except the first
                if (i > 1) {
                    supplierNames.append(" ,");
                }
                supplierNames.append(s.getName());
                i++;
            }
        }
        this.supplierNames = supplierNames.toString();
    }

    public String getInternalId() {
        return internalId;
    }

    public String getProductNum() {
        return productNum;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierNames() {
        return supplierNames;
    }
}
