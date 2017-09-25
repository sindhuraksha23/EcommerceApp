package com.rakshasindhu.shoppinglistlayout.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText phone;
    private EditText password;
    private TextView forgot_pwd;
    private TextView Join_now;
    private Button Sign_in;
    private Button Sign_in_Google;
    private Button Sign_in_fb;
    private SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
         * initializing ids for all view elements
         * EditText,TextView and Buttons
         */
        phone =(EditText)findViewById(R.id.editText_phone);
        password=(EditText)findViewById(R.id.editText_password);

        forgot_pwd =(TextView)findViewById(R.id.textView_forgotPwd);
        Join_now=(TextView)findViewById(R.id.textView_JoinNow);

        Sign_in =(Button)findViewById(R.id.btn_signin);
        Sign_in_Google = (Button)findViewById(R.id.btn_gmail);
        Sign_in_fb=(Button)findViewById(R.id.btn_facebook);

        spref = getSharedPreferences("file5", Context.MODE_PRIVATE);

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_phone = phone.getText().toString().trim();
                String input_password = password.getText().toString().trim();



                if(input_password.equals("") ||input_phone.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Empty Fields", Toast.LENGTH_LONG).show();

                }
                else if(!(input_password.isEmpty()) && (!input_phone.isEmpty()))
                {
                    if (input_phone.length() == 10)
                    {
                        if (input_password.length() >= 6)
                        {
                            String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_login.php?" +
                                    "mobile=" + input_phone + "&password=" + input_password;

                            SharedPreferences.Editor editPef = spref.edit();
                            editPef.putString("input_phone",input_phone);
                            editPef.apply();

                            SignIn(url);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Password Should be 6 or more Characters", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Mobile Should be 10 digit Number", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Empty Fields", Toast.LENGTH_LONG).show();
                }


            }
        });


        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_pass_intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgot_pass_intent);
            }
        });

        Join_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up_Intent = new Intent(LoginActivity.this,RegistrationScreenActivity.class);
                startActivity(sign_up_Intent);
            }
        });

    }

    private void SignIn(String url){

        String tag_string_req_home = "string_req";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Result:"+response);
                if(response.contains("success")){
                    Toast.makeText(LoginActivity.this,"Successfully Logged in",Toast.LENGTH_LONG).show();
                    Intent category_intent = new Intent(LoginActivity.this, ShoppingListActivity.class);
                    startActivity(category_intent);
                } else if(response.contains("Mobile Number not register"))
                {
                    Toast.makeText(LoginActivity.this,"Mobile Number not register",Toast.LENGTH_LONG).show();
                }
                else if(response.contains("incorrect password"))
                {
                    Toast.makeText(LoginActivity.this,"incorrect password",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req_home);

    }

}
