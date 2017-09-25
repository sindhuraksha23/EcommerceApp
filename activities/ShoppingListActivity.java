package com.rakshasindhu.shoppinglistlayout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rakshasindhu.shoppinglistlayout.R;
import com.rakshasindhu.shoppinglistlayout.fragments.CategoryFragment;

public class ShoppingListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container,categoryFragment);
        ft.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            Intent reset_password_intent = new Intent(ShoppingListActivity.this,ResetPasswordActivity.class);
            startActivity(reset_password_intent);
            return true;
        }
        if (id == R.id.action_logout) {
            Intent logout_intent = new Intent(ShoppingListActivity.this,LoginActivity.class);
            startActivity(logout_intent);
            return true;
        }
        if(id == R.id.button_cart){
            Intent visit_cart_intent = new Intent(ShoppingListActivity.this,MyCartActivity.class);
            startActivity(visit_cart_intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@SuppressWarnings("NullableProblems") MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent home_intent = new Intent(ShoppingListActivity.this,ShoppingListActivity.class);
            startActivity(home_intent);

        } else if (id == R.id.nav_bag) {
            Intent visit_cart_intent = new Intent(ShoppingListActivity.this,MyCartActivity.class);
            startActivity(visit_cart_intent);

        } else if (id == R.id.nav_history) {

            Intent order_history_intent = new Intent(ShoppingListActivity.this,OrderHistoryActivity.class);
            startActivity(order_history_intent);

        } else if (id == R.id.nav_checkout) {

            Intent go_to_cart = new Intent(ShoppingListActivity.this,MyCartActivity.class);
            startActivity(go_to_cart);

        }else if(id == R.id.nav_top_sellers){
            Intent top_seller_intent = new Intent(ShoppingListActivity.this,TopSellerActivity.class);
            startActivity(top_seller_intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
