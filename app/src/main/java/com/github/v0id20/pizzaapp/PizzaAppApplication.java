package com.github.v0id20.pizzaapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class PizzaAppApplication extends Application {

    private ArrayList<Dish> pizzaList;
    private ArrayList<Dish> pastaList;
    private ArrayList<Basket> orderList;

    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public void onCreate() {
        super.onCreate();

        pizzaList = new ArrayList<>();
        orderList = new ArrayList<>();
        pastaList = new ArrayList<>();


        SQLiteOpenHelper helper = new DatabaseHelper(this);
        try {
            db = helper.getReadableDatabase();
            cursor = db.query("PIZZA", new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "SIZE", "IMAGE_RESOURCE_ID"}, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int indexName = cursor.getColumnIndex("NAME");
                String name = cursor.getString(indexName);
                int indexImageId = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
                int imageResourceId = cursor.getInt(indexImageId);

                int indexDescription = cursor.getColumnIndex("DESCRIPTION");
                String description = cursor.getString(indexDescription);
                int indexPrice = cursor.getColumnIndex("PRICE");
                double price = cursor.getDouble(indexPrice);
                int indexSize = cursor.getColumnIndex("SIZE");
                String size = cursor.getString(indexSize);


                pizzaList.add(new Pizza(name, description, price, size, imageResourceId));
                cursor.moveToNext();

            }


//            String[] column = new String[]{ "(SELECT max(_id) FROM BASKET) AS max" };
//
//            cursor = db.query("BASKET", column, null, null, null , null , null);
//            if (cursor.moveToFirst()) {
//                int idColumnIndex = cursor.getColumnIndex("ORDER_ID");
//                currentOrderId = cursor.getInt(idColumnIndex)+1;
//            } else {
//                currentOrderId = 1;
//            }
//            cursor.close();

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable when retrieving pizzas", Toast.LENGTH_SHORT);
            toast.show();
        }


        try {
            cursor = db.query("PASTA", new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "IMAGE_RESOURCE_ID"}, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int indexName = cursor.getColumnIndex("NAME");
                String name = cursor.getString(indexName);
                int indexImageId = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
                int imageResourceId = cursor.getInt(indexImageId);
                int indexDescription = cursor.getColumnIndex("DESCRIPTION");
                String description = cursor.getString(indexDescription);
                int indexPrice = cursor.getColumnIndex("PRICE");
                double price = cursor.getDouble(indexPrice);


                pastaList.add(new Pasta(name, description, price, imageResourceId));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Dish> getPizzaList() {
        return pizzaList;
    }

    public ArrayList<Basket> getOrderList() {
        return orderList;
    }

    public ArrayList<Dish> getPastaList() {
        return pastaList;
    }
}
