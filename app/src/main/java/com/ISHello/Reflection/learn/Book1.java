package com.ISHello.Reflection.learn;

public class Book1 {
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String pricePrivate() {
        return "pricePrivate";
    }
}
