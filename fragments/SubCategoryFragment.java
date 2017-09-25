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
import com.rakshasindhu.shoppinglistlayout.adapters.SubCategoryAdapter;
import com.rakshasindhu.shoppinglistlayout.model.SubCategory;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by Raksha Sindhu on 8/29/2017.
 */

public class SubCategoryFragment extends Fragment {
    SubCategory[] subcategories;

    private final String TAG = "SubCategory_Fragment";
    private static final String tmpurl = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_sub_category.php?Id=";
    private static String url;

    private SubCategory subcategory;

    private RecyclerView subcategoryRecyclerView;
    private SubCategoryAdapter subcategoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SubCategory> subcategoryList;
    private int productId;
    private int subCategoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.category_fragment, container, false);

        subCategoryId = getArguments().getInt("categoryId");
        url = new String(tmpurl + subCategoryId);

        requestSubCategoryData();

        subcategoryRecyclerView = view.findViewById(R.id.recycler_view_category);
        subcategoryRecyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        subcategoryRecyclerView.setLayoutManager(layoutManager);
        subcategoryList = new ArrayList<>();


        return view;
    }

    private void requestSubCategoryData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());

                try {
                    if (subcategoryList.isEmpty()) {
                        JSONArray contacts = jsonObject.getJSONArray("SubCategory");
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            String ID = c.getString("Id");
                            String SubCatagoryName = c.getString("SubCatagoryName");
                            String SubCatagoryDiscription = c.getString("SubCatagoryDiscription");
                            String CatagoryImage = c.getString("CatagoryImage");

                            subcategory = new SubCategory(ID,SubCatagoryName,SubCatagoryDiscription,CatagoryImage);
                            subcategoryList.add(subcategory);

                            subcategoryAdapter = new SubCategoryAdapter(getContext(),subcategoryList);
                            subcategoryRecyclerView.setAdapter(subcategoryAdapter);

                            subcategoryAdapter.setOnItemClickListener(new SubCategoryAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, String data)
                                {

                                    productId=Integer.valueOf(data);
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("itemId", productId);

                                    Fragment productFragment = new ProductsFragment();
                                    productFragment.setArguments(bundle);
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    fragmentTransaction.replace(R.id.container, productFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            });

                        }
                    }
                } catch (Exception e) {
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


