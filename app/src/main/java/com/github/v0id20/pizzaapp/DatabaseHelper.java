package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Menu";
    private static final int DB_VERSION = 5;

    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";
    public final static String COLUMN_PRICE = "PRICE";
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
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void insertEntry(String name, String description, double price, String size, String id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("SIZE", size);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PIZZA", null, values);
    }

    private void insertEntry(String name, String description, double price, String id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PASTA", null, values);
    }

    private void insertEntry(String name, String description, double priceSmall, double priceMedium, double priceLarge, String id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE_SMALL", priceSmall);
        values.put("PRICE_MEDIUM", priceMedium);
        values.put("PRICE_LARGE", priceLarge);
        values.put("IMAGE_RESOURCE_ID", id);
        db.insert("PIZZA", null, values);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion<1) {
            db.execSQL("CREATE TABLE PIZZA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"NAME TEXT, "
                    +"DESCRIPTION TEXT, "
                    +"PRICE NUMERIC,"
                    +"SIZE TEXT,"
                    +"IMAGE_RESOURCE_ID TEXT);");
            db.execSQL("CREATE TABLE PASTA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +"NAME TEXT, "
                    +"DESCRIPTION TEXT, "
                    +"PRICE NUMERIC,"
                    +"IMAGE_RESOURCE_ID TEXT);");

            db.execSQL("CREATE TABLE BASKET (ORDER_ID INTEGER, "
                    +"PRODUCT_ID TEXT,"
                    +"NAME TEXT, "
                    +"QUANTITY NUMERIC,"
                    +"PRICE NUMERIC);");

            //add new pasta dishes to menu
            insertEntry("Bolognese", "Indulge yourself into exquisite mix of freshly made baba's recipe spaghetti and meaty sauce", 14,  "nerfee_mirandilla_unsplash", db);
            insertEntry("Tagliatelle pollo e funghi", "Tagliatelle pasta with silent chicken & wild born mushrooms\n" +
                    "bathing in a divine creamy sauce", 14.50, "karolina_kolodziejczak_unsplash", db);
            insertEntry("Spaghetti al pesto", "Spaghetti with traditional ligurian basil pesto made with home grown pine nuts", 13.40, "amirali_mirhashemian_unsplash", db);
            insertEntry("Spaghetti carbonara",  "Smoked pancetta, Italian cream, egg,\n" +
                    " Parmesan. Made specially to drown you in cheese", 12.85, "pinar_kucuk_unsplash", db);
            insertEntry("Penne al l’arrabbiata", "Our specialty. Hot and fresh penne pasta tossed in our homemade seducing arrabbiata sauce with red chilli\n" +
                    "& fresh herb.", 11.30,  "pixzolo_photography_unsplash", db);

            //add new pizzas to menu
            insertEntry("Hawaiian", "Hawaiian Pizza with Pineapples", 12.9,"Medium",  "pizza_hawaiian_chad_montano_unsplash", db);
            insertEntry("Zucchini pizza", "Vegeterian", 11.75,"Medium",  "pizza_zucchini_dilyara_garifullina", db);
            insertEntry("Pepperoni", "Hot and spicy pepperoni and Mozzarella cheese", 13.2,"Medium", "pizza_pepperoni_carissa_gan", db);
            insertEntry("Margherita", "Buffalo mozzarella, tomato, fresh basil, fresh tomato", 16, "Medium", "pizza_margherita_buffala_amirali_mirhashemian", db);
            insertEntry("Funghi", "Mozzarella cheese, mushrooms, cooked ham", 14.50, "Medium", "pizza_funghi_kelvin_theseira_unsplash", db);
        }
        if (oldVersion<2) {
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
        if (oldVersion<3){
            db.execSQL("CREATE TABLE ORDER_HISTORY (ORDER_ID INTEGER, "
                    +"DATE NUMERIC,"
                    +"PRODUCT_ID NUMERIC,"
                    +"NAME TEXT, "
                    +"QUANTITY NUMERIC,"
                    +"PRICE NUMERIC);");
        }
    }
}