package com.rakshasindhu.shoppinglistlayout.fragments;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.db.SQL;
import com.rakshasindhu.shoppinglistlayout.model.MyCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.rakshasindhu.shoppinglistlayout.db.SQL.IMAGE;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.KEY_ID;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.NAME;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.PRICE;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.QUANTITY;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.TABLE;

/*
 * Created by Raksha Sindhu on 8/30/2017.
 */

public class ProductDetailsFragment extends Fragment {

    public String TAG = ProductDetailsFragment.class.getSimpleName();

    private ImageView product_image;
    private TextView tview_pid;
    private TextView tview_pname;
    private TextView tview_price;
    private TextView tview_pdescription;
    private Button add_to_cart;
    private TextView add_quantity;
    private TextView subtract_quantity;
    private TextView item_quantity;

    private int productId;
    private String Product_name;
    private String Product_Price;
    private String Product_Description;
    private String Product_Image;
    private String Product_Quantity;

    private SQL SqlHelper;
    private SQLiteDatabase db;

    private int quantity;
    private String order_quantity;
    private int updated_price;

    ArrayList<MyCart> myCartArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_detail_items, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            productId = bundle.getInt("categoryId");
            Product_name = bundle.getString("ProductName");
            Product_Price =bundle.getString("Price");
            Product_Description =bundle.getString("Discription");
            Product_Image =bundle.getString("Image");
            Product_Quantity = bundle.getString("Quantity");
        }


        updated_price = Integer.valueOf(Product_Price);
        quantity = Integer.valueOf(Product_Quantity);

        tview_pid =view.findViewById(R.id.current_pid);
        tview_pname =view.findViewById(R.id.current_product_name);
        tview_price =view.findViewById(R.id.current_product_price);
        tview_pdescription =view.findViewById(R.id.current_product_description);
        product_image =view.findViewById(R.id.current_product_image);

        add_to_cart = view.findViewById(R.id.button_add_to_cart);

        add_quantity =view.findViewById(R.id.add_item);
        subtract_quantity = view.findViewById(R.id.remove_item);
        item_quantity = (EditText)view.findViewById(R.id.item_current_quantity);

        SqlHelper = new SQL(getContext());
        db = SqlHelper.getWritableDatabase();

        quantity = Integer.parseInt(item_quantity.getText().toString());
        Log.d("quantity",quantity+"");

        add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                item_quantity.setText(quantity+"");
                //noinspection UnnecessaryBoxing
                updated_price = Integer.valueOf(quantity)*Integer.valueOf(Product_Price);

            }
        });

        subtract_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(quantity>=2) {

                    quantity--;
                    item_quantity.setText(quantity + "");
                   //noinspection UnnecessaryBoxing
                   updated_price = Integer.valueOf(Product_Price) * Integer.valueOf(quantity);
                }else{
                   Toast.makeText(getContext(),"Minimum Quantity should be 1",Toast.LENGTH_LONG).show();
               }

            }
        });

        order_quantity = item_quantity.getText().toString();

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put(NAME,Product_name);
                cv.put(KEY_ID,productId);
                cv.put(PRICE,updated_price+"");
                cv.put(QUANTITY,quantity);
                cv.put(IMAGE,Product_Image);

                Toast.makeText(getContext(),Product_name + " added to the cart",Toast.LENGTH_LONG).show();

                db.insert(TABLE,null,cv);
            }
        });


        tview_pid.setText("Product ID : "+productId);
            tview_pname.setText(Product_name);
            tview_price.setText(Product_Price+"/-");
            tview_pdescription.setText(Product_Description);

                Picasso.with(getContext())
                        .load(Product_Image)
                        .into(product_image);


        return  view;
    }

}
