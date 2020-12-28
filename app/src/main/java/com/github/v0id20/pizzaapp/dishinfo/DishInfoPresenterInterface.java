package com.github.v0id20.pizzaapp.dishinfo;

import com.github.v0id20.pizzaapp.Dish;
import com.github.v0id20.pizzaapp.Pasta;
import com.github.v0id20.pizzaapp.Pizza;

public interface DishInfoPresenterInterface {

    public interface View{
        void showDish(Dish dish);
        void showPizza(Pizza pizza);
        void showPasta(Pasta pasta);
        void setQuantity(int quantity);
        void setPrice(double price);
        void setButtonText(String text);
        void close();
    }

    interface Presenter{
        void start();
        void changeQuantity(boolean add);
        void onSizeSelected(int checkedId);
        void addToOrder();

    }
}
