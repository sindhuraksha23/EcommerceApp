package com.rakshasindhu.shoppinglistlayout.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.adapters.OrderAdapter;
import com.rakshasindhu.shoppinglistlayout.model.Order;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class OrderHistoryActivity extends AppCompatActivity {

    private final String TAG = "Order History";
    private String ORDER_URL;

    private final ArrayList<Order> orderArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @SuppressWarnings("unused")
    private int currentId;

    private String order_phone_number;
    private SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        spref = getSharedPreferences("file5", Context.MODE_PRIVATE);
        order_phone_number = spref.getString("input_phone","");
        Log.d("logged_in_phone",order_phone_number+"");

        ORDER_URL = "http://rjtmobile.com/ansari/shopingcart/androidapp/order_history.php?"+"&mobile="+order_phone_number;
        retrieveOrderHistory(ORDER_URL);

        recyclerView = (RecyclerView) findViewById(R.id.order_recycler_content);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        orderAdapter = new OrderAdapter(this, orderArrayList);
        recyclerView.setAdapter(orderAdapter);



        orderAdapter.setOnItemClickListener(new OrderAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                currentId = Integer.valueOf(data);
                registerForContextMenu(view);

            }
        });


    }

    private void retrieveOrderHistory(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject historyObj = new JSONObject(response);
                            JSONArray ordersJsonArray = historyObj.getJSONArray("Order History");

                            for (int i = 0; i < ordersJsonArray.length(); i++) {
                                JSONObject c = ordersJsonArray.getJSONObject(i);
                                String ID = c.getString("OrderID");

                                String ProductName = c.getString("ItemName");
                                String Quantity = c.getString("ItemQuantity");
                                String Prize = c.getString("FinalPrice");

                                if (c.getString("OrderStatus").equals("1")){
                                    String Status = "Order  Confirm";
                                    final Order order = new Order(ID, ProductName, Quantity, Prize, Status);
                                    orderArrayList.add(order);
                                    orderAdapter.notifyData(orderArrayList);
                                }
                                else if (c.getString("OrderStatus").equals("2")){
                                    String Status = "Order Dispatch";
                                    final Order cty = new Order(ID, ProductName, Quantity, Prize, Status);
                                    orderArrayList.add(cty);
                                    orderAdapter.notifyData(orderArrayList);
                                }
                                else if (c.getString("OrderStatus").equals("3")){
                                    String Status = "Order On the Way";
                                    final Order cty = new Order(ID, ProductName, Quantity, Prize, Status);
                                    orderArrayList.add(cty);
                                    orderAdapter.notifyData(orderArrayList);
                                }
                                else {
                                    String Status = "Order Delivered";
                                    final Order cty = new Order(ID, ProductName, Quantity, Prize, Status);
                                    orderArrayList.add(cty);
                                    orderAdapter.notifyData(orderArrayList);
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}
