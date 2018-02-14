package com.indianservers.onlinegrocery;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.indianservers.onlinegrocery.fragment.ContactUsFragment;
import com.indianservers.onlinegrocery.fragment.DeliveryAddressFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import services.SessionManager;

public class MainActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;

    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    private SharedPreferences data;
    SessionManager sessionManager;
    private TextView checkOutAmount, itemCountTextView;
    private Toolbar toolbar;
    private ImageView checkoutImage;
    private Handler mHandler;
    // tags used to attach the fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_CATEGORY= "Categories";
    private static final String TAG_PHARMACY= "Pharmacy";
    private static final String TAG_MYACCOUNT = "MyAccount";
    private static final String TAG_ORDERS = "My Orders";
    private static final String TAG_ADDRESS = "Delivery Address";
    private static final String TAG_CONTACT = "Contact Us";
    private static final String TAG_SIGNOUT = "SignOut";
    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private boolean shouldLoadHomeFragOnBackPres = false;
    public MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Kirana Mart");
        searchView = (MaterialSearchView) findViewById(R.id.search_view);


        sessionManager = new SessionManager(MainActivity.this);
        mHandler = new Handler();
        /**
         *Setup the DrawerLayout and NavigationView
         */
        data = PreferenceManager.getDefaultSharedPreferences(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        View headerView = mNavigationView.getHeaderView(0);

        TextView textView = (TextView) headerView.findViewById(R.id.useremail);
        String email = data.getString("email", "0");
        if (sessionManager.checkLogin() || email.equals("0"))
            finish();
        textView.setText(email);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the Categoriesfragment as the first Fragment
         */
        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            loadHomeFragment();
        }
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawerLayout.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.containerView, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        mDrawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                MainFragment homeFragment = new MainFragment();
                return homeFragment;
            case 1:
                // photos
                Categoriesfragment photosFragment = new Categoriesfragment();
                return photosFragment;
            case 2:
                // movies fragment
                startActivity(new Intent(MainActivity.this,PharmacyActivity.class));
                return new HomeFragment();
            case 3:
                // notifications fragment
                MyAccount notificationsFragment = new MyAccount();
                return notificationsFragment;
            case 4:
                // settings fragment
                MyOrdersFragment settingsFragment = new MyOrdersFragment();
                return settingsFragment;
            case 5:
                // settings fragment
                DeliveryAddressFragment deliveryAddressFragment = new DeliveryAddressFragment();
                return deliveryAddressFragment;
            case 6:
                // settings fragment
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                return contactUsFragment;
            case 7:
                showAlertDialog1(MainActivity.this,"Confirm Logout",false);
            default:
                return new HomeFragment();
        }
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navhome:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.navcategories:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CATEGORY;
                        break;
                    case R.id.navpharmacy:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PHARMACY;
                        break;
                    case R.id.navmyprofile:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MYACCOUNT;
                        break;
                    case R.id.navmyorders:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ORDERS;
                        break;
                    case R.id.deliveryAddress:
                        // launch new intent instead of loading fragment
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_ADDRESS;
                       break;
                    case R.id.navcontactus:
                        // launch new intent instead of loading fragment
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_CONTACT;
                        break;
                    case R.id.navsignout:
                        // launch new intent instead of loading fragment
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_SIGNOUT;
                       break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    private void selectNavMenu() {
        mNavigationView.getMenu().getItem(navItemIndex).setChecked(true);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.homefragmentmenu, menu);
        MenuItem item = menu.findItem(R.id.actionn_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                startActivity(new Intent(MainActivity.this,CheckOutActivity.class));
                break;
            case R.id.actionn_search:

                break;
        }
        return super.onOptionsItemSelected(item);
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


//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitByBackKey();
//        }
//            else{
//                getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }else {
            super.onBackPressed();
        }
        if(shouldLoadHomeFragOnBackPress){
            exitByBackKey();
        }
    }

}