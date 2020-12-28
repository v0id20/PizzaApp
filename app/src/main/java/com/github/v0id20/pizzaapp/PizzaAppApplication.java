package com.github.v0id20.pizzaapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

public class PizzaAppApplication extends Application {

    public static String PIZZA_TABLE = "PIZZA";
    public static String PASTA_TABLE = "PASTA";
    public static String ORDER_HISTORY_TABLE = "ORDER_HISTORY";

    private ArrayList<Dish> pizzaList;
    private ArrayList<Dish> pastaList;

    public ArrayList<OrderHistoryItem> getOrderHistory() {
        return orderHistory;
    }

    private ArrayList<OrderHistoryItem> orderHistory;
    private Basket basket;
    private ArrayList<BasketItem> orderList;
    private int basketItemCount;

    public int currentOrderId;

    private SQLiteOpenHelper helper;
    SQLiteDatabase db;
    int dbVersion;
    Cursor cursor;

    @Override
    public void onCreate() {
        super.onCreate();

        basket = new Basket();
        pizzaList = new ArrayList<>();
        orderList = new ArrayList<>();
        pastaList = new ArrayList<>();
        orderHistory = new ArrayList<>();
        basket.basketList = new ArrayList<>();
        basket.totals = new BasketItem("Total", 0, 0, BasketItem.TYPE_TOTAL);

        helper = new DatabaseHelper(this);
        try {
            db = helper.getReadableDatabase();
            dbVersion = db.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        retrievePizzaRow(pizzaList);
        retrievePastaRows(pastaList);
        retrieveOrderHistory(orderHistory);
    }

    public ArrayList<Dish> getPizzaList() {
        return pizzaList;
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

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    private void retrievePizzaRow(ArrayList<Dish> list) {
        String[] query;
        if (dbVersion > 3) {
            query = new String[]{"_id", "NAME", "DESCRIPTION", DatabaseHelper.COLUMN_PRICE_SMALL,
                    DatabaseHelper.COLUMN_PRICE_MEDIUM, DatabaseHelper.COLUMN_PRICE_LARGE, "SIZE",
                    "IMAGE_RESOURCE_ID"};
        } else {
            query = new String[]{"_id", "NAME", "DESCRIPTION", "PRICE", "SIZE",
                    "IMAGE_RESOURCE_ID"};
        }
        try {

            cursor = db.query(PIZZA_TABLE, query, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Pizza newPizza = new Pizza();
                list.add((Pizza) getDishData(cursor, newPizza));
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
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void retrieveOrderHistory(ArrayList<OrderHistoryItem> orderHistory) {
        String[] query = new String[]{"ORDER_ID", DatabaseHelper.COLUMN_NAME, "QUANTITY",
                DatabaseHelper.COLUMN_PRICE};
        try {
            cursor = db.query(ORDER_HISTORY_TABLE, query, null, null, null, null, "ORDER_ID DESC");
            cursor.moveToFirst();
            outerloop:
            while (!cursor.isAfterLast()) {
                int idIndex = cursor.getColumnIndex("ORDER_ID");
                int orderId = cursor.getInt(idIndex);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                String name = cursor.getString(nameIndex);
                int quantityIndex = cursor.getColumnIndex("QUANTITY");
                int quantity = cursor.getInt(quantityIndex);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
                double price = cursor.getDouble(priceIndex);


                for (OrderHistoryItem i : orderHistory) {
                    if (i.orderId == orderId) {
                        i.getOrderList().add(new BasketItem(name, quantity, price));
                        i.setTotalToPay(i.getTotalToPay()+price*quantity);
                        cursor.moveToNext();
                        continue outerloop;
                    }
                }

                Log.i("ITS INNER LOOP",
                        "current order id " + orderId + " item and quantity" + name + " " + price);
                ArrayList<BasketItem> newList = new ArrayList<BasketItem>();
                newList.add(new BasketItem(name, quantity, price));
                orderHistory.add(new OrderHistoryItem(orderId, newList, quantity*price));
                cursor.moveToNext();
            }

            String[] column = new String[]{"(SELECT max(ORDER_ID) FROM ORDER_HISTORY) AS max"};
            Cursor cursor = db.query("ORDER_HISTORY", column, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex("max");
                currentOrderId = cursor.getInt(idColumnIndex) + 1;
            } else {
                currentOrderId = 1;
            }
            Log.i("current orderid ", "" + currentOrderId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
        String imageResourceId = c.getString(indexImageId);


        if (d instanceof Pizza && dbVersion > 3) {
            int indexPriceSmall = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_SMALL);
            double priceSmall = c.getDouble(indexPriceSmall);
            int indexPriceMedium = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_MEDIUM);
            double priceMedium = c.getDouble(indexPriceMedium);
            int indexPriceLarge = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE_LARGE);
            double priceLarge = c.getDouble(indexPriceLarge);
            dish = new Pizza(name, description, priceSmall, priceMedium, priceLarge,
                    imageResourceId);
        } else {
            int indexPrice = c.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
            price = c.getDouble(indexPrice);
            if (d instanceof Pizza) {
                dish = new Pizza(name, description, price, imageResourceId);
            } else {
                dish = new Pasta(name, description, price, imageResourceId);
            }
        }

        return dish;
    }
}
