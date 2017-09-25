package com.rakshasindhu.shoppinglistlayout.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgot_pass_phone;
    private Button submit_button_forgot;

    private String global_phone_value;
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_pass_phone = (EditText)findViewById(R.id.editText_userphone);
        submit_button_forgot = (Button)findViewById(R.id.button_submit_phone);

        submit_button_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String registered_phone = forgot_pass_phone.getText().toString().trim();
                global_phone_value = registered_phone;

                if((!registered_phone.isEmpty())&&(registered_phone.length()==10)){
                    String forgot_pass_url = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_fogot_pass.php?"+
                            "&mobile="+registered_phone;
                    submitPhoneNumber(forgot_pass_url);
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter Valid Mobile of 10 digit", Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    private void submitPhoneNumber(String url){
        String  tag_string_req = "string_req_forgot";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                try{
                    jsonResponse="";
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject result = response.getJSONObject(i);
                        String UserPassword = result.getString("UserPassword");
                        jsonResponse+= UserPassword;

                    }
                    Toast.makeText(ForgotPasswordActivity.this,"Your Password is: "+jsonResponse,Toast.LENGTH_LONG).show();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_string_req);
    }
}
