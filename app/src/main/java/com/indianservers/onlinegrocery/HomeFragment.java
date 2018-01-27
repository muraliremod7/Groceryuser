package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CollapsingToolbarLayout collapsingToolbarLayout;
    private SliderLayout mDemoSlider;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Toolbar toolbar;
    private SharedPreferences sskey;
    private SharedPreferences.Editor editor;
    private ImageView imageView, singlecheckout;
    HashMap<String, String> HashMapForURL ;
    HashMap<String, Integer> HashMapForLocalRes ;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);
        //Call this method if you want to add images from URL .
        AddImagesUrlOnline();

        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());


        LinearLayout fruitsandvegetables = (LinearLayout) view.findViewById(R.id.fandv);
        fruitsandvegetables.setOnClickListener(this);

        LinearLayout foodgrains = (LinearLayout) view.findViewById(R.id.foodgrains);
        foodgrains.setOnClickListener(this);

        LinearLayout bakery = (LinearLayout) view.findViewById(R.id.bakery);
        bakery.setOnClickListener(this);

        LinearLayout beverages = (LinearLayout) view.findViewById(R.id.beverges);
        beverages.setOnClickListener(this);

        LinearLayout brandedfood = (LinearLayout) view.findViewById(R.id.brandedfood);
        brandedfood.setOnClickListener(this);

        LinearLayout beauty = (LinearLayout) view.findViewById(R.id.beauty);
        beauty.setOnClickListener(this);

        LinearLayout household = (LinearLayout) view.findViewById(R.id.household);
        household.setOnClickListener(this);

        LinearLayout gourment = (LinearLayout) view.findViewById(R.id.gourment);
        gourment.setOnClickListener(this);

        LinearLayout eggs = (LinearLayout) view.findViewById(R.id.eggs);
        eggs.setOnClickListener(this);
        ImageView pharmacy = (ImageView)view.findViewById(R.id.pharmacy);
        pharmacy.setOnClickListener(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbarLayout != null){
            collapsingToolbarLayout.setTitle("Grocery");
            //collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);
            //collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);


        }

        for(String name : HashMapForURL.keySet()){

            TextSliderView textSliderView = new TextSliderView(getContext());

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
        mDemoSlider.addOnPageChangeListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {
        slider.getView();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put("Beverges", "http://www.softcron.com/images/gro.png");
        HashMapForURL.put("Fruits&Vegetables", "https://ak6.picdn.net/shutterstock/videos/16051936/thumb/1.jpg");
        HashMapForURL.put("Pharmacy", "http://www.voile35.com/wp-content/uploads/2017/03/ONLINE-Pharmacy.png");
        HashMapForURL.put("Amazing Offers", "http://dealsrightnow.net:5555/offers/-728.png");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fandv:
                editor = sskey.edit();
                editor.putString("categoryType","FruitsVegetables");
                editor.commit();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.containerView, new ItemsFragment());
                transaction.commit();
                break;
            case R.id.foodgrains:
                editor = sskey.edit();
                editor.putString("categoryType","FoodgrainsOilMasala");
                editor.commit();
                FragmentManager fg = getFragmentManager();
                FragmentTransaction fgt = fg.beginTransaction();
                fgt.replace(R.id.containerView, new ItemsFragment());
                fgt.commit();
                break;
            case R.id.bakery:
                editor = sskey.edit();
                editor.putString("categoryType","RiceBags");
                editor.commit();
                FragmentManager fgbk = getFragmentManager();
                FragmentTransaction fgtbk = fgbk.beginTransaction();
                fgtbk.replace(R.id.containerView, new ItemsFragment());
                fgtbk.commit();
                break;
            case R.id.beverges:
                editor = sskey.edit();
                editor.putString("categoryType","Beverages");
                editor.commit();
                FragmentManager fgbe = getFragmentManager();
                FragmentTransaction fgtbe = fgbe.beginTransaction();
                fgtbe.add(R.id.containerView, new ItemsFragment());
                fgtbe.commit();
                break;
            case R.id.brandedfood:
                editor = sskey.edit();
                editor.putString("categoryType","BrandedFoods");
                editor.commit();
                FragmentManager fgbf = getFragmentManager();
                FragmentTransaction fgtbf = fgbf.beginTransaction();
                fgtbf.add(R.id.containerView, new ItemsFragment());
                fgtbf.commit();
                break;
            case R.id.beauty:
                editor = sskey.edit();
                editor.putString("categoryType","BeautyHygiene");
                editor.commit();
                FragmentManager fgbu = getFragmentManager();
                FragmentTransaction fgtbu = fgbu.beginTransaction();
                fgtbu.add(R.id.containerView, new ItemsFragment());
                fgtbu.commit();
                break;
            case R.id.household:
                editor = sskey.edit();
                editor.putString("categoryType","Household");
                editor.commit();
                FragmentManager fgh = getFragmentManager();
                FragmentTransaction fgth = fgh.beginTransaction();
                fgth.replace(R.id.containerView, new ItemsFragment());
                fgth.commit();
                break;
            case R.id.gourment:
                editor = sskey.edit();
                editor.putString("categoryType","GourmetWorldFood");
                editor.commit();
                FragmentManager fgg = getFragmentManager();
                FragmentTransaction fgtg = fgg.beginTransaction();
                fgtg.add(R.id.containerView, new ItemsFragment());
                fgtg.commit();
                break;
            case R.id.eggs:
                editor = sskey.edit();
                editor.putString("categoryType","Nursery");
                editor.commit();
                FragmentManager fgeg = getFragmentManager();
                FragmentTransaction fgteg = fgeg.beginTransaction();
                fgteg.add(R.id.containerView, new ItemsFragment());
                fgteg.commit();
                break;
            case R.id.pharmacy:
                Intent intent = new Intent(getActivity(),PharmacyActivity.class);
                startActivity(intent);
                break;

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
