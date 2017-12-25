package com.indianservers.onlinegrocery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.indianservers.onlinegrocery.fragment.PlaceOrderFragment;

public class PlaceOrderActivity extends AppCompatActivity {
    private Firebase firebase;
    public static String datee,timee;
    FragmentPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Firebase.setAndroidContext(getApplicationContext());

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return PlaceOrderFragment.newInstance(1, "Address");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PlaceOrderFragment.newInstance(0, "Time Slot");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return PlaceOrderFragment.newInstance(2, "Place order");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Time Slot";
            }
            if(position==1){
                return "Address";
            }
            if(position==2){
                return "Place Order";
            }
            return (CharSequence) PlaceOrderFragment.newInstance(position,"");

        }

    }
}
