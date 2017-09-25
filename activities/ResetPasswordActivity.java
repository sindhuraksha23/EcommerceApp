package com.rakshasindhu.shoppinglistlayout.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();

    private EditText reset_phone;
    private EditText reset_old_pass;
    private EditText reset_new_pass;
    private Button submit_reset;
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset_phone = (EditText)findViewById(R.id.editText_phone);
        reset_old_pass =(EditText)findViewById(R.id.editText_old_password);
        reset_new_pass=(EditText)findViewById(R.id.editText_new_password);

        submit_reset =(Button)findViewById(R.id.button_reset_password);

        submit_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_phone = reset_phone.getText().toString().trim();
                String user_old_pass = reset_old_pass.getText().toString().trim();
                String user_new_pass = reset_new_pass.getText().toString().trim();


                if(!(user_old_pass.equals(""))&& !(user_new_pass.equals(""))&&(user_old_pass.length()>=6)&&(user_new_pass.length()>=6))
                {
                    String restPassUrl = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_reset_pass.php?" +
                            "&mobile="+user_phone+"&password="+user_old_pass+"&newpassword="+user_new_pass;

                    resetPass(restPassUrl);
                    Intent back_to_login = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                    startActivity(back_to_login);
                }
                else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Please Enter Valid Password of 6 or more Characters", Toast.LENGTH_LONG).show();
                    }


            }
        });
    }

    private void resetPass(String url){
        String tag_string_reg = "rest_pass_request";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResetPass", response.toString());
                        try {
                            jsonResponse ="";
                            JSONArray msg_array = response.getJSONArray("msg");
                            String msg = msg_array.getString(0);
                            jsonResponse+=msg;

                            Toast.makeText(ResetPasswordActivity.this,jsonResponse,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_string_reg);
    }
}
