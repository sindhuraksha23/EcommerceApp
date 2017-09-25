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
import com.rakshasindhu.shoppinglistlayout.adapters.CategoryAdapter;
import com.rakshasindhu.shoppinglistlayout.model.Category;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private final String TAG = "Category_Fragment";
    private static final String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_category.php";

    private Category category;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Category> categoryList;
    private int currentId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.category_fragment,container,false);

        categoryRecyclerView = view.findViewById(R.id.recycler_view_category);
        categoryRecyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryList = new ArrayList<>();

        requestCategoryData();

        return view;
    }

    private void requestCategoryData()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, jsonObject.toString());

                try{
                    if (categoryList.isEmpty()){
                        JSONArray contacts = jsonObject.getJSONArray("Category");
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String ID = c.getString("Id");
                            String name = c.getString("CatagoryName");
                            String description = c.getString("CatagoryDiscription");
                            String image = c.getString("CatagoryImage");

                            category = new Category(ID, name, description,image);

                            categoryList.add(category);
                            categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
                            categoryRecyclerView.setAdapter(categoryAdapter);

                            categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, String data)
                                {

                                currentId=Integer.valueOf(data);
                                Bundle bundle=new Bundle();
                                bundle.putInt("categoryId", currentId);

                                Fragment subcategoryFragment = new SubCategoryFragment();
                                subcategoryFragment.setArguments(bundle);
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container,subcategoryFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                                }
                            });

                        }

                    }

                }catch (Exception e){
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
