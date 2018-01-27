package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import java.util.ArrayList;

/**
 * Created by Ratan on 7/27/2015.
 */
public class Categoriesfragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 10;
    AppCompatActivity activity;
    public Handler handler = new Handler();
    public AHBottomNavigation bottomNavigation;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    // TODO: Rename and change types and number of parameters
    public static Categoriesfragment newInstance() {
        Categoriesfragment fragment = new Categoriesfragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate categories_layout and setup Views.
         */
            View x =  inflater.inflate(R.layout.categories_layout,container,false);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        collapsingToolbarLayout = (CollapsingToolbarLayout) x.findViewById(R.id.homecollapsing_toolbar);
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle("Grocery");
            //collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);
            //collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        bottomNavigation = (AHBottomNavigation) x.findViewById(R.id.cabottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.buttoncolor);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Categorys", R.drawable.category, R.color.buttoncolor);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Checkout", R.drawable.checkout, R.color.buttoncolor);

// Add items
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigation.addItems(bottomNavigationItems);
// Set background color
        bottomNavigation.setAccentColor(Color.parseColor("#FF9BAE19"));
        bottomNavigation.setInactiveColor(Color.parseColor("#B7B7B7"));
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
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
                    transaction.replace(R.id.caframe_layout, selectedFragment);
                    transaction.commit();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
        try{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Setting custom colors for notification
                    AHNotification notification = new AHNotification.Builder()
                            .setText(":)")
                            .build();

                }
            }, 3000);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        return x;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    class MyAdapter extends FragmentStatePagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
          switch (position){
              case 0 : return new FruitsAndVegetablesFragment();
              case 1 : return new FoodgrainsAndMasalaFragment();
              case 2 : return new RiceBagsFragment();
              case 3 : return new Beverages();
              case 4 : return new BrandedFoods();
              case 5 : return new BeautyAndHygenieFragment();
              case 6 : return new HouseholdFragment();
              case 7 : return new GourmentworldfoodFragment();
              case 8 : return new NurseryFragment();
              case 9 : return new BabyCareFragment();
          }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Fruits & Vegetables";
                case 1 :
                    return "Foodgrains,Oil & Masala";
                case 2:
                    return "Bakery, Cakes & Dairy";
                case 3:
                    return "Beverages";
                case 4:
                    return "Branded Foods";
                case 5:
                    return "Beauty & Hygiene";
                case 6:
                    return "Household";
                case 7:
                    return "Gourmet & World Food";
                case 8:
                    return "Eggs,Meat & Fish";
                case 9:
                    return "BabyCare";
            }
                return null;
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
