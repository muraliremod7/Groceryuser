package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.indianservers.onlinegrocery.fragment.ContactUsFragment;
import com.indianservers.onlinegrocery.fragment.DeliveryAddressFragment;


import java.math.BigDecimal;
import java.math.BigInteger;

import services.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    private SharedPreferences data;
    SessionManager sessionManager;
    private TextView checkOutAmount, itemCountTextView;
    private Toolbar toolbar;
    private ImageView checkoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        toolbar.setTitle("Kirana Mart");
        sessionManager = new SessionManager(MainActivity.this);
        checkoutImage = (ImageView)findViewById(R.id.checkoutmain);
        checkoutImage.setOnClickListener(this);

        /**
         *Setup the DrawerLayout and NavigationView
         */
        data = PreferenceManager.getDefaultSharedPreferences(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
        View headerView = mNavigationView.getHeaderView(0);

        TextView textView = (TextView)headerView.findViewById(R.id.useremail);
        String email = data.getString("email","0");
        if(sessionManager.checkLogin()||email.equals("0"))
            finish();
        textView.setText(email);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the Categoriesfragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.replace(R.id.containerView,new MainFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                FragmentTransaction mFragmentTransaction;
                if (menuItem.getItemId() == R.id.navhome) {
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new MainFragment()).commit();
                }

                if (menuItem.getItemId() == R.id.navcategories) {
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new Categoriesfragment()).commit();

                }
                if (menuItem.getItemId() == R.id.navpharmacy) {
                    Intent intent = new Intent(MainActivity.this,PharmacyActivity.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId() == R.id.deliveryAddress){
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new DeliveryAddressFragment()).commit();
                }
                if(menuItem.getItemId() == R.id.navmyprofile){
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new MyAccount()).commit();
                }
                if(menuItem.getItemId() == R.id.navmyorders){
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new MyOrdersFragment()).commit();
                }
                if(menuItem.getItemId() == R.id.navcontactus){
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.containerView,new ContactUsFragment()).commit();
                }
                if (menuItem.getItemId() == R.id.navsignout) {
                    showAlertDialog1(MainActivity.this,"Confirm Logout",false);
                }


                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }
    public void showAlertDialog1(Context context, String message, final Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle).create();
        // Setting Dialog Message
        alertDialog.setMessage("Confirm Logout");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                sessionManager.logoutUser();
                finish();
                return;

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.cancel();
                return;
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homefragmentmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.cart:
                Intent intent = new Intent(this, CheckOutActivity.class);
                startActivity(intent);
            default:

        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                })
                .show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkoutmain:
                Intent intent = new Intent(this, CheckOutActivity.class);
                startActivity(intent);
                break;
        }
    }
}