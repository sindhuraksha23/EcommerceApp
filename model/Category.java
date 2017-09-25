package com.rakshasindhu.shoppinglistlayout.model;

public class Category {

    public final String ID;
    public final String CatagoryName;
    public final String CatagoryDiscription;
    public final String CatagoryImage;

    public Category(String ID, String name, String description,String image) {
        this.ID = ID;
        this.CatagoryName = name;
        this.CatagoryDiscription = "Description: " + description;
        this.CatagoryImage = image;
    }
}
