package com.caramba.ordertool.scenes.displayModels;

import javafx.scene.control.CheckBox;

/**
 * Model used to display the chart data in the product overview
 */
//getters are used by javafx, but IntelliJ doesn't recognize this, so we suppress the warnings
@SuppressWarnings("unused")
public class ProductDetailsTableData {
    private final CheckBox checkboxToggleVisible;
    private final String name;
    //style
    private final String color;
    private final boolean hasDashedLine;
    //the values need to be stored in separate fields like this in order for javafx to place them in a table
    private Integer janValue;
    private Integer febValue;
    private Integer marValue;
    private Integer aprValue;
    private Integer mayValue;
    private Integer junValue;
    private Integer julValue;
    private Integer augValue;
    private Integer septValue;
    private Integer octValue;
    private Integer novValue;
    private Integer decValue;

    public ProductDetailsTableData(String name, CheckBox checkBox, String color, Boolean hasDashedLine) {
        this.name = name;
        this.checkboxToggleVisible = checkBox;
        this.color = color;
        this.hasDashedLine = hasDashedLine;
    }

    public Integer getMonthValue(int monthNumber) {
        return switch (monthNumber) {
            case 1 -> janValue;
            case 2 -> febValue;
            case 3 -> marValue;
            case 4 -> aprValue;
            case 5 -> mayValue;
            case 6 -> junValue;
            case 7 -> julValue;
            case 8 -> augValue;
            case 9 -> septValue;
            case 10 -> octValue;
            case 11 -> novValue;
            case 12 -> decValue;
            default -> throw new IllegalArgumentException();
        };
    }

    public Integer getJanValue() {
        return janValue;
    }

    public Integer getFebValue() {
        return febValue;
    }

    public Integer getMarValue() {
        return marValue;
    }

    public Integer getAprValue() {
        return aprValue;
    }

    public Integer getMayValue() {
        return mayValue;
    }

    public Integer getJunValue() {
        return junValue;
    }

    public Integer getJulValue() {
        return julValue;
    }

    public Integer getAugValue() {
        return augValue;
    }

    public Integer getSeptValue() {
        return septValue;
    }

    public Integer getOctValue() {
        return octValue;
    }

    public Integer getNovValue() {
        return novValue;
    }

    public Integer getDecValue() {
        return decValue;
    }

    public String getName() {
        return name;
    }

    public CheckBox getCheckboxToggleVisible() {
        return checkboxToggleVisible;
    }

    public String getColor() {
        return color;
    }

    public boolean hasDashedLine() {
        return hasDashedLine;
    }

    public void setValue(int monthToSet, Integer value) {
        switch (monthToSet) {
            case 1 -> this.janValue = value;
            case 2 -> this.febValue = value;
            case 3 -> this.marValue = value;
            case 4 -> this.aprValue = value;
            case 5 -> this.mayValue = value;
            case 6 -> this.junValue = value;
            case 7 -> this.julValue = value;
            case 8 -> this.augValue = value;
            case 9 -> this.septValue = value;
            case 10 -> this.octValue = value;
            case 11 -> this.novValue = value;
            case 12 -> this.decValue = value;
            default -> throw new IllegalArgumentException();
        }
    }

}
