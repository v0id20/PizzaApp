package com.github.v0id20.pizzaapp;

import android.app.Application;

public class PizzaAppApplication extends Application {

    private DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        dataManager = new DataManager(this);
        dataManager.readDatabase();
    }


    public DataManager getDataManager() {
        return dataManager;
    }

}
