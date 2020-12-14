package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "Menu";
    private static int DB_VERSION = 3;
    int currentOrderId;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabse(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateDatabse(db);
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

    private void updateDatabse(SQLiteDatabase db){

        if (db.getVersion()<1) {
            db.execSQL("DROP TABLE IF EXISTS PIZZA");
            db.execSQL("DROP TABLE IF EXISTS PASTA");

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



            insertEntry("Margharita", "Buffalo mozzarella, tomato, fresh basil, fresh tomato", 16, "Medium", 0, db);
            insertEntry("Funghi", "Mushrooms and cheese", 14.50, "Medium", 0, db);
            insertEntry("Ham and cheese", "ham and cheese obviously", 15.80, "Medium", 0, db);


        } else if(DB_VERSION<=2) {
            db.execSQL("DROP TABLE IF EXISTS BASKET");
            db.execSQL("CREATE TABLE BASKET (ORDER_ID INTEGER, "
                    +"PRODUCT_ID TEXT,"
                    +"NAME TEXT, "
                    +"QUANTITY NUMERIC,"
                    +"PRICE NUMERIC);");
        } else {

            insertEntry("Bolognese", "Indulge yourself into exquisite mix of freshly made baba's recipe spaghetti and meaty sauce", 14,  R.drawable.nerfee_mirandilla_unsplash, db);
            insertEntry("Tagliatelle pollo e funghi", "Tagliatelle pasta with silent chicken & wild born mushrooms\n" +
                    "bathing in a divine creamy sauce", 14.50, R.drawable.karolina_kolodziejczak_unsplash, db);
            insertEntry("Spaghetti al pesto", "Spaghetti with traditional ligurian basil pesto made with home grown pine nuts", 13.40, R.drawable.amirali_mirhashemian_unsplash, db);
            insertEntry("Spaghetti carbonara",  "Smoked pancetta, Italian cream, egg,\n" +
                    " Parmesan. Made specially to drown you in cheese", 12.85, R.drawable.pinar_kucuk_unsplash, db);
            insertEntry("Penne al l’arrabbiata", "Our specialty. Hot and fresh penne pasta tossed in our homemade seducing arrabbiata sauce with red chilli\n" +
                    "& fresh herb.", 11.30, R.drawable.pixzolo_photography_unsplash, db);


            insertEntry("Hawaiian", "Hawaiian Pizza with Pineapples", 12.9,"Medium", R.drawable.pizza_hawaiian_chad_montano_unsplash, db);
            insertEntry("Zucchini pizza", "Vegeterian", 11.75,"Medium", R.drawable.pizza_zucchini_dilyara_garifullina, db);
            insertEntry("Pepperoni", "Hot and spicy pepperoni and Mozzarella cheese", 13.2,"Medium", R.drawable.pizza_pepperoni_carissa_gan, db);


            ContentValues cv = new ContentValues();
            cv.put("IMAGE_RESOURCE_ID", R.drawable.pizza_funghi_kelvin_theseira_unsplash);
            cv.put("DESCRIPTION", "Mozzarella cheese, mushrooms, cooked ham");
            db.update("PIZZA", cv, "NAME = ?", new String[] {"Funghi"} );

            cv.clear();
            cv.put("IMAGE_RESOURCE_ID", R.drawable.pizza_margherita_buffala_amirali_mirhashemian);
            cv.put("NAME", "Margherita");
            cv.put("DESCRIPTION", "Buffalo mozzarella, tomato, fresh basil, fresh tomato");
            int i2 = db.update("PIZZA", cv, "NAME = ?", new String[] {"Margharita"} );
            Log.i("DATABASE UPDATE: UPDATING ROW", Integer.toString(i2));


            int i3 = db.delete("PIZZA", "NAME = ?", new String[] {"Ham and cheese"});
            Log.i("DATABASE UPDATE: deleting row", Integer.toString(i3));

        }

    }
}
