package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.BasketItem;
import com.github.v0id20.pizzaapp.model.DatabaseHelper;
import com.github.v0id20.pizzaapp.model.Dish;
import com.github.v0id20.pizzaapp.model.Pasta;
import com.github.v0id20.pizzaapp.model.Pizza;
import com.github.v0id20.pizzaapp.orderhistory.OrderHistoryItem;

import java.util.ArrayList;

public class DataManager {
    public static String PIZZA_TABLE = "PIZZA";
    public static String PASTA_TABLE = "PASTA";
    public static String ORDER_HISTORY_TABLE = "ORDER_HISTORY";
    private Context mContext;

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private int dbVersion;
    private Cursor cursor;

    private ArrayList<Dish> pizzaList;
    private ArrayList<Dish> pastaList;
    private ArrayList<OrderHistoryItem> orderHistory;

    private Basket basket;
    private int currentOrderId;

    public DataManager(Context context){
        mContext = context;
    }

    public void readDatabase(){
        pizzaList = new ArrayList<>();
        pastaList = new ArrayList<>();
        orderHistory = new ArrayList<>();

        basket = new Basket();

        helper = new DatabaseHelper(mContext);
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
        String[] query = new String[]{"ORDER_ID", "DATE", DatabaseHelper.COLUMN_NAME, "QUANTITY",
                DatabaseHelper.COLUMN_PRICE};
        try {
            cursor = db.query(ORDER_HISTORY_TABLE, query, null, null, null, null, "ORDER_ID DESC");
            cursor.moveToFirst();
            outerloop:
            while (!cursor.isAfterLast()) {
                int idIndex = cursor.getColumnIndex("ORDER_ID");
                int orderId = cursor.getInt(idIndex);
                int dateIndex = cursor.getColumnIndex("DATE");
                long dateInmillis = cursor.getLong(dateIndex);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                String name = cursor.getString(nameIndex);
                int quantityIndex = cursor.getColumnIndex("QUANTITY");
                int quantity = cursor.getInt(quantityIndex);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
                double price = cursor.getDouble(priceIndex);


                for (OrderHistoryItem i : orderHistory) {
                    if (i.getOrderId() == orderId) {
                        i.getOrderList().add(new BasketItem(name, quantity, price));
                        i.setTotalToPay(i.getTotalToPay()+price*quantity);
                        cursor.moveToNext();
                        continue outerloop;
                    }
                }

                Log.i("ITS OUTER LOOP",
                        "current order id " + orderId + " item and quantity" + name + " " + price);
                ArrayList<BasketItem> newList = new ArrayList<>();
                newList.add(new BasketItem(name, quantity, price));
                orderHistory.add(new OrderHistoryItem(orderId, newList, dateInmillis, quantity*price));
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
            Log.i("current order id ", "" + currentOrderId);

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


    public boolean submitOrder(ArrayList<BasketItem> orders){
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            ArrayList<BasketItem> currentBasketToHistory = new ArrayList<>();
            long time = System.currentTimeMillis();

            for (int i = 0; i < orders.size(); i++) {
                ContentValues orderValues = new ContentValues();
                orderValues.put("ORDER_ID", currentOrderId);
                orderValues.put("DATE", System.currentTimeMillis());
                orderValues.put("NAME", orders.get(i).getName());
                orderValues.put("QUANTITY", orders.get(i).getQuantity());
                orderValues.put("PRICE", orders.get(i).getPrice());
                db.insert("ORDER_HISTORY", null, orderValues);
                currentBasketToHistory.add(new BasketItem(orders.get(i).getName(),orders.get(i).getQuantity(),orders.get(i).getPrice()));
            }
            OrderHistoryItem ohi = new OrderHistoryItem(currentOrderId, currentBasketToHistory , time, 0);
            ohi.countTotal();
            orderHistory.add(0,ohi);
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void incrementCurrentOrderId(){
        currentOrderId++;
    }

    public ArrayList<Dish> getPizzaList() {
        return pizzaList;
    }

    public ArrayList<Dish> getPastaList() {
        return pastaList;
    }

    public ArrayList<OrderHistoryItem> getOrderHistory() {
        return orderHistory;
    }

    public int getCurrentOrderId() {
        return currentOrderId;
    }

    public Basket getBasket() {
        return basket;
    }

}
