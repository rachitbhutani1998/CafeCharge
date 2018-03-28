package com.cafedroid.android.cafecharge;

import android.content.Intent;

/**
 * Created by rachit on 09/03/18.
 */

public class FoodItem {
    private String name;
    private Double price;
    private Integer quantity;
    private boolean isSelected;

    public FoodItem(String name, Double price, Integer quantity){
        this.name=name;
        this.price=price;
        this.quantity=quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    Double getPrice() {
        return price;
    }

    void increaseQuantity(){
        this.quantity+=1;
    }
}
