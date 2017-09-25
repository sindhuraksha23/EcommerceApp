package com.rakshasindhu.shoppinglistlayout.model;

/*
 * Created by Raksha Sindhu on 8/31/2017.
 */

public class MyCart {

    public final String ID;
    public final String ProductName;
    public final String Quantity;
    public final String Price;
    public final String Image;

    public MyCart(String ID, String ProductName, String Quantity, String Price,String Image) {
        this.ID = ID;
        this.ProductName = ProductName;
        this.Quantity = Quantity;
        this.Price = Price;
        this.Image =Image;
    }
}
