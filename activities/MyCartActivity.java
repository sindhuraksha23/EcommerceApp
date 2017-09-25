package com.rakshasindhu.shoppinglistlayout.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.adapters.MyCartAdapter;
import com.rakshasindhu.shoppinglistlayout.db.SQL;
import com.rakshasindhu.shoppinglistlayout.model.MyCart;
import com.rakshasindhu.shoppinglistlayout.net.AppController;
import com.rakshasindhu.shoppinglistlayout.util.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.rakshasindhu.shoppinglistlayout.db.SQL.IMAGE;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.KEY_ID;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.NAME;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.PRICE;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.QUANTITY;
import static com.rakshasindhu.shoppinglistlayout.db.SQL.TABLE;

@SuppressWarnings("ALL")
public class MyCartActivity extends AppCompatActivity {
    private  String TAG = MyCartActivity.class.getSimpleName();

    private final ArrayList<MyCart> myCartArrayList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MyCartAdapter myCartAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button checkout;

    private SQL sqlHelper;
    private SQLiteDatabase db;

    private float total_bill = 0;

    private String order_phone_number;
    private SharedPreferences spref;

    /*
     * Environment configurations for PayPal
     */
    private static final int PAYPAL_REQUEST_CODE = 123;
    private static final PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        sqlHelper = new SQL(this);
        db = sqlHelper.getReadableDatabase();

        recyclerView = (RecyclerView)findViewById(R.id.mycart_recycler_view);

        recyclerView.setHasFixedSize(false);

        checkout = (Button)findViewById(R.id.button_final_checkout);

        getCartItemsFromDB();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        spref = getSharedPreferences("file5", Context.MODE_PRIVATE);
        order_phone_number = spref.getString("input_phone","");
        Log.d("logged_in_phone",order_phone_number+"");


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyCartActivity.this, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
                startService(intent);

                getPayment();

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.button_checkout) {

            getPayment();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPayment(){

        String mPaymentAmount = Float.toString(total_bill);
        Log.d("bill",mPaymentAmount);

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(mPaymentAmount),"USD","payment",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent paymentIntent = new Intent(MyCartActivity.this, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(paymentIntent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null){
                    try{
                        String paymentDetails = paymentConfirmation.toJSONObject().toString();
                        Toast.makeText(this, "Payment Details: "+paymentDetails, Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(this, "JSONException", Toast.LENGTH_SHORT).show();}
                }
            }
        }
    }

    private void getCartItemsFromDB(){

            Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE,null);
            cursor.moveToFirst();

            if(cursor.getCount()>0) {

                String Name = (cursor.getString(cursor.getColumnIndex(NAME)));

                do {

                    String Product_ID = cursor.getString(cursor.getColumnIndex(KEY_ID));
                    String Product_Name = cursor.getString(cursor.getColumnIndex(NAME));
                    String Product_QTY = cursor.getString(cursor.getColumnIndex(QUANTITY));
                    String Product_Price = cursor.getString(cursor.getColumnIndex(PRICE));
                    String Product_Image = cursor.getString(cursor.getColumnIndex(IMAGE));

                    total_bill += Float.valueOf(Product_Price);

                    MyCart myCart = new MyCart(Product_ID, Product_Name, Product_QTY, Product_Price, Product_Image);

                    String place_order_url = "http://rjtmobile.com/ansari/shopingcart/androidapp/orders.php?" +
                            "&item_id=" + Product_ID + "&item_names=" + Product_Name + "&item_quantity=" + Product_QTY
                            + "&final_price=" + Product_Price + "&mobile=" + order_phone_number;

                    placeOrder(place_order_url);

                    myCartArrayList.add(myCart);

                } while (cursor.moveToNext());

                if (myCartArrayList.size() > 0) {

                    myCartAdapter = new MyCartAdapter(this, myCartArrayList);
                    recyclerView.setAdapter(myCartAdapter);
                } else {
                    ArrayList<MyCart> myCartArrayList = new ArrayList<>();
                    myCartAdapter = new MyCartAdapter(this, myCartArrayList);
                    recyclerView.setAdapter(myCartAdapter);
                }
            }

        }

        private void placeOrder(String place_order_url){

            String tag_string_req_order = "string_req_place_order";
            final StringRequest stringRequest = new StringRequest(Request.Method.GET,place_order_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("place_order","Result:"+response);
                    if(response.contains("Order Confirmed")){
                       // Toast.makeText(MyCartActivity.this,"Your order is confirmed",Toast.LENGTH_LONG).show();

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req_order);
        }
}
