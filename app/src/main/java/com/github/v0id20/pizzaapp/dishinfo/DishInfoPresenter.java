package com.github.v0id20.pizzaapp.dishinfo;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.v0id20.pizzaapp.Basket;
import com.github.v0id20.pizzaapp.BasketItem;
import com.github.v0id20.pizzaapp.Dish;
import com.github.v0id20.pizzaapp.Pasta;
import com.github.v0id20.pizzaapp.Pizza;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class DishInfoPresenter implements DishInfoPresenterInterface.Presenter {
    private final int id;
    private final String dishType;
    private final Basket applicationBasket;
    private final PizzaAppApplication application;
    private final DishInfoPresenterInterface.View view;
    private final Dish currentDish;

    private int quantityToOrder = 1;
    private double productPrice;
    private String productName;

    public Dish initCurrentDish() {
        Dish d;
        if (dishType.equals(DishInfoActivity.EXTRA_DISH_VALUE_PIZZA)) {
            d = application.getPizzaList().get(id);
        } else {
            d = application.getPastaList().get(id);
        }
        productName = d.getName();
        productPrice = d.getPrice();
        return d;
    }

    public DishInfoPresenter(DishInfoPresenterInterface.View view, int id, String type, Basket basket, PizzaAppApplication application) {
        this.id = id;
        this.dishType = type;
        applicationBasket = basket;
        this.view = view;
        this.application = application;
        currentDish = initCurrentDish();
    }

    @Override
    public void start() {
        if (dishType.equals(DishInfoActivity.EXTRA_DISH_VALUE_PIZZA)) {
            Pizza pizza = (Pizza) application.getPizzaList().get(id);
            view.showPizza(pizza);
        } else {
            Pasta pasta = (Pasta) application.getPastaList().get(id);
            view.showPasta(pasta);
        }
    }

    @Override
    public void changeQuantity(boolean add) {

        if (add) {

            quantityToOrder += 1;
        } else {
            if (quantityToOrder > 1) {
                quantityToOrder -= 1;
            }
        }
        view.setQuantity(quantityToOrder);
        String text = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
        view.setButtonText(text);
    }

    @Override
    public void onSizeSelected(int checkedId) {
        Pizza pizza = ((Pizza) currentDish);
        switch (checkedId) {
            case R.id.size_small:
                view.setPrice(pizza.getPriceSmall());
                productPrice = pizza.getPriceSmall();
                break;
            case R.id.size_medium:
                view.setPrice(pizza.getPriceMedium());
                productPrice = pizza.getPriceMedium();
                break;
            case R.id.size_large:
                view.setPrice(pizza.getPriceLarge());
                productPrice = pizza.getPriceLarge();
                break;
            default:
                break;
        }
        String buttonText = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
        view.setButtonText(buttonText);
    }

    @Override
    public void addToOrder() {
        BasketItem basketItem;
        ArrayList<BasketItem> basket = applicationBasket.basketList;
       // ArrayList<BasketItem> basketWithTotal = (ArrayList<BasketItem>) basket.clone();
        if (basket.size() > 0) {
            for (int i = 0; i < basket.size(); i++) {
                if (basket.get(i).getName().equals(productName)) {
                    if (basket.get(i).getPrice() == productPrice) {
                        applicationBasket.addExistingBasketItem(i, quantityToOrder);
                        view.close();
                        return;
                    } else {
                        continue;
                    }
                }
            }
        }
        basketItem = new BasketItem(productName, quantityToOrder, productPrice);
        applicationBasket.addNewBasketItem(basketItem);
        view.close();
    }
}
