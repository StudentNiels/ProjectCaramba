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
    //style
    private final String color;
    private final boolean hasDashedLine;

    public ProductDetailsTableData(String name, CheckBox checkBox, String color, Boolean hasDashedLine) {
        this.name = name;
        this.checkboxToggleVisible = checkBox;
        this.color = color;
        this.hasDashedLine = hasDashedLine;
    }

    public Integer getMonthValue(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return janValue;
            case 2:
                return febValue;
            case 3:
                return marValue;
            case 4:
                return aprValue;
            case 5:
                return mayValue;
            case 6:
                return junValue;
            case 7:
                return julValue;
            case 8:
                return augValue;
            case 9:
                return septValue;
            case 10:
                return octValue;
            case 11:
                return novValue;
            case 12:
                return decValue;
            default:
                throw new IllegalArgumentException();
        }
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
            case 1:
                this.janValue = value;
                break;
            case 2:
                this.febValue = value;
                break;
            case 3:
                this.marValue = value;
                break;
            case 4:
                this.aprValue = value;
                break;
            case 5:
                this.mayValue = value;
                break;
            case 6:
                this.junValue = value;
                break;
            case 7:
                this.julValue = value;
                break;
            case 8:
                this.augValue = value;
                break;
            case 9:
                this.septValue = value;
                break;
            case 10:
                this.octValue = value;
                break;
            case 11:
                this.novValue = value;
                break;
            case 12:
                this.decValue = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

}
