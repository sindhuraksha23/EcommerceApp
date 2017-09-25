package com.rakshasindhu.shoppinglistlayout.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by Raksha Sindhu on 8/31/2017.
 */

public class SQL extends SQLiteOpenHelper {

    private static final String DATABASE="ProductsDatabase";
    public static final String TABLE="ProductsTable";
    public static final String KEY_ID="Product_ID";
    public static final String NAME="Product_Name";
    public static final String PRICE="Product_Price";
    public static final String QUANTITY="Product_Quantity";
    public static final String IMAGE="Product_Image";

    private static final int VERSION =1;

    public SQL (Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + PRICE + " TEXT," + QUANTITY + " TEXT," + IMAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
