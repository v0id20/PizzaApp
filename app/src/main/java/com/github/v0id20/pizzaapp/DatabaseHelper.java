package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "Menu";
    private static final int DB_VERSION = 5;
    int currentOrderId;

    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";
    public final static String COLUMN_PRICE = "PRICE";
    public final static String COLUMN_SIZE = "SIZE";
    public final static String COLUMN_IMAGE_REOURCE_ID = "IMAGE_RESOURCE_ID";
    public final static String COLUMN_PRICE_SMALL = "PRICE_SMALL";
    public final static String COLUMN_PRICE_MEDIUM = "PRICE_MEDIUM";
    public static final String COLUMN_PRICE_LARGE = "PRICE_LARGE";

    PizzaAppApplication application ;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        application = (PizzaAppApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int oldVersion = db.getVersion();
        updateDatabse(db, oldVersion);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabse(db, oldVersion);
    }


    private void insertEntry(String name, String description, double price, String size, int id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("SIZE", size);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PIZZA", null, values);
    }
    private void insertEntry(String name, String description, double price, int id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PASTA", null, values);
    }

    private void insertEntry(String name, String description, double priceSmall, double priceMedium, double priceLarge, int id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE_SMALL", priceSmall);
        values.put("PRICE_MEDIUM", priceMedium);
        values.put("PRICE_LARGE", priceLarge);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PIZZA", null, values);
    }

    private void updateDatabse(SQLiteDatabase db, int oldVersion){

        if (oldVersion<1) {
            db.execSQL("CREATE TABLE PIZZA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"NAME TEXT, "
                    +"DESCRIPTION TEXT, "
                    +"PRICE NUMERIC,"
                    +"SIZE TEXT,"
                    +"IMAGE_RESOURCE_ID NUMERIC);");
            db.execSQL("CREATE TABLE PASTA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"NAME TEXT, "
                    +"DESCRIPTION TEXT, "
                    +"PRICE NUMERIC,"
                    +"IMAGE_RESOURCE_ID NUMERIC);");

            insertEntry("Margherita", "Buffalo mozzarella, tomato, fresh basil, fresh tomato", 16, "Medium", R.drawable.pizza_margherita_buffala_amirali_mirhashemian, db);
            insertEntry("Funghi", "Mozzarella cheese, mushrooms, cooked ham", 14.50, "Medium", R.drawable.pizza_funghi_kelvin_theseira_unsplash, db);
            insertEntry("Ham and cheese", "ham and cheese obviously", 15.80, "Medium", 0, db);

        }  else if (oldVersion<2) {
            db.execSQL("CREATE TABLE BASKET (ORDER_ID INTEGER, "
                    +"PRODUCT_ID TEXT,"
                    +"NAME TEXT, "
                    +"QUANTITY NUMERIC,"
                    +"PRICE NUMERIC);");
        } else if (oldVersion<3){
            //add new pasta dishes to menu
            insertEntry("Bolognese", "Indulge yourself into exquisite mix of freshly made baba's recipe spaghetti and meaty sauce", 14,  R.drawable.nerfee_mirandilla_unsplash, db);
            insertEntry("Tagliatelle pollo e funghi", "Tagliatelle pasta with silent chicken & wild born mushrooms\n" +
                    "bathing in a divine creamy sauce", 14.50, R.drawable.karolina_kolodziejczak_unsplash, db);
            insertEntry("Spaghetti al pesto", "Spaghetti with traditional ligurian basil pesto made with home grown pine nuts", 13.40, R.drawable.amirali_mirhashemian_unsplash, db);
            insertEntry("Spaghetti carbonara",  "Smoked pancetta, Italian cream, egg,\n" +
                    " Parmesan. Made specially to drown you in cheese", 12.85, R.drawable.pinar_kucuk_unsplash, db);
            insertEntry("Penne al l’arrabbiata", "Our specialty. Hot and fresh penne pasta tossed in our homemade seducing arrabbiata sauce with red chilli\n" +
                    "& fresh herb.", 11.30, R.drawable.pixzolo_photography_unsplash, db);

            //add new pizzas to menu
            insertEntry("Hawaiian", "Hawaiian Pizza with Pineapples", 12.9,"Medium", R.drawable.pizza_hawaiian_chad_montano_unsplash, db);
            insertEntry("Zucchini pizza", "Vegeterian", 11.75,"Medium", R.drawable.pizza_zucchini_dilyara_garifullina, db);
            insertEntry("Pepperoni", "Hot and spicy pepperoni and Mozzarella cheese", 13.2,"Medium", R.drawable.pizza_pepperoni_carissa_gan, db);

            //delete one pizza from menu
            db.delete("PIZZA", "NAME = ?", new String[] {"Ham and cheese"});

        } else if (oldVersion<4) {
            db.execSQL("ALTER TABLE PIZZA ADD COLUMN " + COLUMN_PRICE_MEDIUM + "  NUMERIC");
            db.execSQL("ALTER TABLE PIZZA ADD COLUMN " + COLUMN_PRICE_LARGE + "  NUMERIC");
            db.execSQL("ALTER TABLE PIZZA RENAME COLUMN PRICE TO "+ COLUMN_PRICE_SMALL);

            Cursor c = db.query(PizzaAppApplication.PIZZA_TABLE,new String[] {COLUMN_NAME, COLUMN_PRICE_SMALL},null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int indexPrice = c.getColumnIndex(COLUMN_PRICE_SMALL);
                double price = c.getDouble(indexPrice);
                int indexName = c.getColumnIndex(COLUMN_NAME);
                String name = c.getString(indexName);
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_PRICE_MEDIUM, price*1.2);
                cv.put(COLUMN_PRICE_LARGE, price*1.4);
                db.update(PizzaAppApplication.PIZZA_TABLE, cv,"NAME = ?", new String[] {name});
                c.moveToNext();
            }
        }

    }
}