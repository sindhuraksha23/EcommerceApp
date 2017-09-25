package com.rakshasindhu.shoppinglistlayout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.adapters.ProductsAdapter;
import com.rakshasindhu.shoppinglistlayout.model.Products;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/29/2017.
 */

public class ProductsFragment extends Fragment {

    private final String TAG = ProductsFragment.class.getSimpleName();
    private static final String tmpurl = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_product.php?Id=";
    private String url;

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Products> productsArrayList;
    private Products products;

    private int currentId;
    private int categoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.category_fragment,container,false);

        categoryId = getArguments().getInt("itemId");
        url = new String(tmpurl + categoryId);

        productsRecyclerView = view.findViewById(R.id.recycler_view_category);
        productsRecyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        productsRecyclerView.setLayoutManager(layoutManager);
        productsArrayList = new ArrayList<>();

        requestProductsData();

        return view;
    }

    private void requestProductsData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Log.d(TAG, jsonObject.toString());

                try{
                    JSONArray contacts = jsonObject.getJSONArray("Product");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        final String ID = c.getString("Id");

                        final String ProductName = c.getString("ProductName");
                        String Quantity = c.getString("Quantity");
                        final String Price = c.getString("Prize");
                        final String Discription = c.getString("Discription");
                        final String image = c.getString("Image");

                        products = new Products(ID, ProductName, Quantity, Price, Discription,image);
                        productsArrayList.add(products);
                        productsAdapter = new ProductsAdapter(getContext(),productsArrayList);
                        productsRecyclerView.setAdapter(productsAdapter);

                    }
                    productsAdapter.setOnItemClickListener(new ProductsAdapter.OnRecyclerViewItemClickListener(){

                        @Override
                        public void onItemClick(View view, String data) {

                            currentId=Integer.valueOf(data);
                            Bundle bundle=new Bundle();
                            for(Products p:productsArrayList) {
                                if(p.ID.equals(data)) {
                                    bundle.putInt("categoryId", currentId);
                                    bundle.putString("ProductName", p.ProductName);
                                    bundle.putString("Price", p.Price);
                                    bundle.putString("Discription", p.Description);
                                    bundle.putString("Quantity",p.Quantity);
                                    bundle.putString("Image", p.Image);

                                    Log.d("Description:", p.Description);
                                }
                            }

                            Fragment productDetailsFragment = new ProductDetailsFragment();
                            productDetailsFragment.setArguments(bundle);
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container,productDetailsFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }catch (Exception e){
                    //noinspection ThrowablePrintedToSystemOut
                    System.out.println(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "ERROR" + volleyError.getMessage());
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
