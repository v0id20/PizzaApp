package com.github.v0id20.pizzaapp.dishinfo;

import com.github.v0id20.pizzaapp.model.Dish;
import com.github.v0id20.pizzaapp.model.Pasta;
import com.github.v0id20.pizzaapp.model.Pizza;

public interface DishInfoPresenterInterface {

    interface View{
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
