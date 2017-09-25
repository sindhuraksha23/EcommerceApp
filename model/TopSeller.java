package com.rakshasindhu.shoppinglistlayout.model;

/*
 * Created by Raksha Sindhu on 8/30/2017.
 */

public class TopSeller {
    public final String SellerId;
    public final String SellerName;
    public final String SellerDeal;
    public final String SellerRating;
    public final String SellerLogo;
    public TopSeller(String id, String name, String deal, String rating,String SellerLogo){
        this.SellerId = id;
        this.SellerName = name;
        this.SellerDeal = "Deal: " + deal;
        this.SellerRating = "Rating: " + rating;
        this.SellerLogo = SellerLogo;
    }

}
