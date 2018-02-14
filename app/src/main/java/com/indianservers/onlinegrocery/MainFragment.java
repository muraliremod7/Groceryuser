package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.AlertDialogManager;
import services.ConnectionDetector;

/**
 * Created by Ratan on 7/29/2015.
 */
public class MainFragment extends Fragment implements HomeFragment.OnFragmentInteractionListener,Categoriesfragment.OnFragmentInteractionListener,CheckoutFragment.OnFragmentInteractionListener{
    ArrayList<String> actorsList;

    FragmentManager mFragmentManager;
    FragmentTransaction fragmentTransaction;
    AppCompatActivity activity;
    public static SharedPreferences sskey;
    private ConnectionDetector detector;
    SharedPreferences item;
    String SSkey;
    public Handler handler = new Handler();
    public static AHBottomNavigation bottomNavigation;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    //    String length = "0";
    SharedPreferences.Editor editor;
    private AlertDialogManager manager;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout,container,false);
        detector = new ConnectionDetector(getActivity());
        manager = new AlertDialogManager();


        bottomNavigation = (AHBottomNavigation) view.findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.buttoncolor);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Categorys", R.drawable.category, R.color.buttoncolor);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Checkout", R.drawable.checkout, R.color.buttoncolor);

// Add items
        bottomNavigationItems.clear();
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigation.addItems(bottomNavigationItems);
// Set background color
        bottomNavigation.setAccentColor(Color.parseColor("#FF9BAE19"));
        bottomNavigation.setInactiveColor(Color.parseColor("#B7B7B7"));
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Fragment selectedFragment = null;
                if(position==0){
                    selectedFragment = HomeFragment.newInstance();
                }if(position==1){
                    selectedFragment = Categoriesfragment.newInstance();
                }
                if(position==2){
                    Intent intent = new Intent(getActivity(),CheckOutActivity.class);
                    startActivity(intent);
                }
                try{
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                }catch (NullPointerException e){

                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setting custom colors for notification
                AHNotification notification = new AHNotification.Builder()
                        .setText(":)")
                        .build();

            }
        }, 3000);
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();
        mFragmentManager = getActivity().getSupportFragmentManager();
        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SSkey = sskey.getString("sskey","0");
        actorsList = new ArrayList<String>();

        if(detector.isNetworkOn(getActivity())){
        }
        else {
            manager.showAlertDialog(getActivity(),"No Internet Connection","You Dont Have Internet Connection",false);
        }
        return view;
    }







    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void onBackPressed() {

    }
}
