package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
    public static int int_items = 12;
    AppCompatActivity activity;
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

        AppCompatActivity activity = (AppCompatActivity) getActivity();

            /**
             *Set an Apater for the View Pager
             */
            viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
            /**
             * Now , this is a workaround ,
             * The setupWithViewPager dose't works without the runnable .
             * Maybe a Support Library Bug .
             */

            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });

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
              case 7 : return new BabyCareFragment();
              case 8 : return new NurseryFragment();
              case 9 : return new DeosAndPerfumesFragment();
              case 10: return new VitaminCFragment();
              case 11: return new PatanjaliFragment();
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
                    return "Foodgrains,Oil";
                case 2:
                    return "Rice Bags";
                case 3:
                    return "Beverages";
                case 4:
                    return "Branded Foods";
                case 5:
                    return "Beauty & Hygiene";
                case 6:
                    return "Household";
                case 7:
                    return "BabyCare";
                case 8:
                    return "Nursery";
                case 9:
                    return "Deos And Perfumes";
                case 10:
                    return "Health Products";
                case 11:
                    return "Patanjali";
            }
                return null;
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
