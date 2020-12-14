package com.github.v0id20.pizzaapp;

public class Pizza extends Dish {

    private String[] size;


    public Pizza(String name, String desc, double price,  int resourceId) {
        super(name, desc, price, resourceId);
    }

    private void setSize(){
        size = new String[]{"Small", "Medium", "Large"};
    }

    public String[] getSize() {
        return size;
    }

}
