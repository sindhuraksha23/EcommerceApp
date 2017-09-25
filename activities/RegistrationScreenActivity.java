package com.rakshasindhu.shoppinglistlayout.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.net.AppController;

public class RegistrationScreenActivity extends AppCompatActivity {

    private EditText Person_Name;
    private EditText Email;
    private EditText Mobile;
    private EditText Password;
    private CheckBox display_password;
    private Button register;
    private Button already_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        /*
         * Initializing view elements
         */

        Person_Name = (EditText)findViewById(R.id.editText_Name);
        Email = (EditText)findViewById(R.id.editText_Email);
        Mobile = (EditText)findViewById(R.id.editText_mobile);
        Password = (EditText)findViewById(R.id.editText_pswd);

        display_password =(CheckBox)findViewById(R.id.checkBox_show_pwd);


        register =(Button)findViewById(R.id.button_register);
        already_member =(Button)findViewById(R.id.button_Sign_in);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_Name = Person_Name.getText().toString().trim();
                String input_Email = Email.getText().toString().trim();
                String input_Mobile = Mobile.getText().toString().trim();
                String input_Password = Password.getText().toString().trim();

                /*
                 * Validation checks for login credentials
                 */


                if(!(input_Name.equals(""))&& !(input_Email.equals(""))&&!(input_Mobile.equals(""))&&!(input_Password.equals("")))
                {
                    if(input_Email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
                    {
                        if(input_Mobile.length() == 10)
                        {
                            if(input_Password.length()>=6)
                            {
                                String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_reg.php?" +
                                        "name=" + input_Name + "&email=" + input_Email + "&mobile=" + input_Mobile + "&password=" + input_Password;
                                register_user(url);

                                Person_Name.setText("");
                                Email.setText("");
                                Mobile.setText("");
                                Password.setText("");

                            }
                            else
                            {
                                Toast.makeText(RegistrationScreenActivity.this, "Password Should be 6 or more Characters", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegistrationScreenActivity.this,"Mobile Should be 10 digit Number" , Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegistrationScreenActivity.this,"Please Enter Valid Email" , Toast.LENGTH_LONG).show();
                    }

                }

                else if(input_Name.equals("")|| input_Email.equals("")||input_Mobile.equals("")||input_Password.equals(""))
                {
                    Toast.makeText(RegistrationScreenActivity.this,"Please Enter Empty Fields",Toast.LENGTH_LONG).show();

                }

                /*
                 * After inserting data in database clear all textboxes
                 */

            }
        });

        display_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(!ischecked){
                    Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else //noinspection ConstantConditions
                    if(ischecked){
                    Password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }
            }
        });

        already_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_to_signin = new Intent(RegistrationScreenActivity.this,LoginActivity.class);
                startActivity(back_to_signin);
            }
        });
    }

    private void register_user(String URL){
        String  tag_string_req = "string_req";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Result Response:",response);

                if(response.contains("successfully registered")){

                    Toast.makeText(RegistrationScreenActivity.this,response+"You can Login Now",Toast.LENGTH_LONG).show();
                    Intent back_to_login = new Intent(RegistrationScreenActivity.this,LoginActivity.class);
                    startActivity(back_to_login);

                }
                else{
                    Toast.makeText(RegistrationScreenActivity.this,response,Toast.LENGTH_LONG).show();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }
}
