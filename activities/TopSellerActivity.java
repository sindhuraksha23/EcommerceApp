package com.rakshasindhu.shoppinglistlayout.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.adapters.TopSellerAdapter;
import com.rakshasindhu.shoppinglistlayout.model.TopSeller;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopSellerActivity extends AppCompatActivity {

    private final String TAG = TopSellerActivity.class.getSimpleName();

    private static final String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_top_sellers.php?";

    private final ArrayList<TopSeller> topSellerArrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private TopSellerAdapter topSellerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_seller);

        recyclerView = (RecyclerView)findViewById(R.id.top_seller_recycler_content);
        recyclerView.setHasFixedSize(false);

        objRequestMethod();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void objRequestMethod() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());

                try{
                    JSONArray contacts = jsonObject.getJSONArray("Top Sellers");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("SellerId");
                        String name = c.getString("SellerName");
                        String deal = c.getString("SellerDeal");
                        String rating = c.getString("SellerRating");
                        String sellerlogo = c.getString("SellerLogo");

                        final TopSeller topSeller = new TopSeller(id, name, deal, rating,sellerlogo);
                        topSellerAdapter = new TopSellerAdapter(getApplicationContext(), topSellerArrayList);
                        recyclerView.setAdapter(topSellerAdapter);
                        topSellerArrayList.add(topSeller);

                    }

                }catch (Exception e){
                    //noinspection ThrowablePrintedToSystemOut
                    System.out.println(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "ERROR" + volleyError.getMessage());
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


}
