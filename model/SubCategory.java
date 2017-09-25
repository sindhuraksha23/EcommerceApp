package com.rakshasindhu.shoppinglistlayout.model;

/*
 * Created by Raksha Sindhu on 8/29/2017.
 */

public class SubCategory {
    public final String ID;
    public final String SubCatagoryName;
    public final String SubCatagoryDiscription;
    public final String CatagoryImage;
    public SubCategory(String ID, String SubCatagoryName, String SubCatagoryDiscription,String CategoryImage){
        this.ID = ID;
        this.SubCatagoryName = SubCatagoryName;
        this.SubCatagoryDiscription = "Category Description: " + SubCatagoryDiscription;
        this.CatagoryImage = CategoryImage;
    }
}
