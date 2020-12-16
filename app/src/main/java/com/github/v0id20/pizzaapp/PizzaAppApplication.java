package com.github.v0id20.pizzaapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class PizzaAppApplication extends Application {

    public static String PIZZA_TABLE = "PIZZA";
    public static String PASTA_TABLE = "PASTA";

    private ArrayList<Dish> pizzaList;
    private ArrayList<Dish> pastaList;
    private Basket basket;
    private ArrayList<BasketItem> orderList;
    private int basketItemCount;

    private SQLiteOpenHelper helper;
    SQLiteDatabase db;
    int dbVersion;
    Cursor cursor;

    @Override
    public void onCreate() {
        super.onCreate();

        pizzaList = new ArrayList<>();
        orderList = new ArrayList<>();
        pastaList = new ArrayList<>();

        helper = new DatabaseHelper(this);
        try {
            db = helper.getReadableDatabase();
            dbVersion = db.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }


        retrievePizzaRow(pizzaList);

//        try {
//
//            db.getVersion();
//            cursor = db.query(PIZZA_TABLE, new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "SIZE", "IMAGE_RESOURCE_ID"}, null, null, null, null, null);
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                int indexName = cursor.getColumnIndex("NAME");
//                String name = cursor.getString(indexName);
//                int indexImageId = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
//                int imageResourceId = cursor.getInt(indexImageId);
//
//                int indexDescription = cursor.getColumnIndex("DESCRIPTION");
//                String description = cursor.getString(indexDescription);
//                int indexPrice = cursor.getColumnIndex("PRICE");
//                double price = cursor.getDouble(indexPrice);
//                int indexSize = cursor.getColumnIndex("SIZE");
//                String size = cursor.getString(indexSize);
//
//
//                pizzaList.add(new Pizza(name, description, price, imageResourceId));
//                cursor.moveToNext();
//
//            }
//
//
//        } catch (SQLiteException e) {
//            Toast toast = Toast.makeText(this, "Database unavailable when retrieving pizzas", Toast.LENGTH_SHORT);
//            toast.show();
//        }

        retrievePastaRows(pastaList);

//
//        try {
//            cursor = db.query(PASTA_TABLE, new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "IMAGE_RESOURCE_ID"}, null, null, null, null, null);
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                int indexName = cursor.getColumnIndex("NAME");
//                String name = cursor.getString(indexName);
//                int indexImageId = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
//                int imageResourceId = cursor.getInt(indexImageId);
//                int indexDescription = cursor.getColumnIndex("DESCRIPTION");
//                String description = cursor.getString(indexDescription);
//                int indexPrice = cursor.getColumnIndex("PRICE");
//                double price = cursor.getDouble(indexPrice);
//
//
//                pastaList.add(new Pasta(name, description, price, imageResourceId));
//                cursor.moveToNext();
//            }
//
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public ArrayList<Dish> getPizzaList() {
        return pizzaList;
    }

    public ArrayList<BasketItem> getOrderList() {
        return orderList;
    }

    public ArrayList<Dish> getPastaList() {
        return pastaList;
    }

    public int getBasketItemCount() {
        return basketItemCount;
    }

    public void setBasketItemCount(int basketItemCount) {
        this.basketItemCount = basketItemCount;
    }

    public Basket getBasket() {
        return basket;
    }


    private void retrievePizzaRow(ArrayList<Dish> list) {
        String[] query;
        if (dbVersion>3){
            query = new String[]{"_id", "NAME", "DESCRIPTION", DatabaseHelper.COLUMN_PRICE_SMALL, DatabaseHelper.COLUMN_PRICE_MEDIUM, DatabaseHelper.COLUMN_PRICE_LARGE, "SIZE", "IMAGE_RESOURCE_ID"};
        } else {
            query = new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "SIZE", "IMAGE_RESOURCE_ID"};
        }

            try {

                cursor = db.query(PIZZA_TABLE, query, null, null, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Pizza newPizza = new Pizza();
                    list.add((Pizza)getDishData(cursor,newPizza));
//                    int indexName = cursor.getColumnIndex("NAME");
//                    String name = cursor.getString(indexName);
//                    int indexImageId = cursor.getColumnIndex("IMAGE_RESOURCE_ID");
//                    int imageResourceId = cursor.getInt(indexImageId);
//                    int indexDescription = cursor.getColumnIndex("DESCRIPTION");
//                    String description = cursor.getString(indexDescription);
//                    int indexPrice = cursor.getColumnIndex("PRICE");
//                    double price = cursor.getDouble(indexPrice);
//
//                    if (tableName.equals(PIZZA_TABLE)) {
//                        int indexSize = cursor.getColumnIndex("SIZE");
//                        String size = cursor.getString(indexSize);
//                        pizzaList.add(new Pizza(name, description, price, imageResourceId));
//                    } else {
//                        pastaList.add(new Pasta(name, description, price, imageResourceId));
//                    }

                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    private void retrievePastaRows(ArrayList<Dish> list) {
        String[] query = new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "IMAGE_RESOURCE_ID"};
        try {
            cursor = db.query(PASTA_TABLE, query, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Pasta newPasta = new Pasta();
                list.add((Pasta) getDishData(cursor, newPasta));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Dish getDishData(Cursor c, Dish d) {
        Dish dish;
        double price;

        int indexName = c.getColumnIndex(DatabaseHelper.COLUMN_NAME);
        String name = c.getString(indexName);
        int indexDescription = c.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
        String description = c.getString(indexDescription);
        int indexImageId = c.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_REOURCE_ID);
        int imageResourceId = c.getInt(indexImageId);


        if (d instanceof Pizza && dbVersion>3){
            int indexPriceSmall = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_SMALL);
            double priceSmall = c.getDouble(indexPriceSmall);
            int indexPriceMedium = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_MEDIUM);
            double priceMedium = c.getDouble(indexPriceMedium);
            int indexPriceLarge = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_LARGE);
            double priceLarge = c.getDouble(indexPriceLarge);
            dish = new Pizza(name, description,priceSmall,priceMedium, priceLarge, imageResourceId);
        } else {
            int indexPrice = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
            price  = c.getDouble(indexPrice);
            if (d instanceof Pizza) {
                dish = new Pizza(name, description, price, imageResourceId);
            } else {
                dish = new Pasta(name, description, price, imageResourceId);
            }
        }

        return dish;

    }
}
