package com.indianservers.onlinegrocery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import adapter.AddressAdapter;
import adapter.PlaceOrderAdapter;
import adapter.PlaceOrderCommonClass;


public class MyOrdersFragment extends Fragment implements View.OnClickListener{
   private RecyclerView recyclerView;
    private Button button;
    private ProgressDialog progressDialog;
    private PlaceOrderAdapter orderAdapter;
    private SharedPreferences sskey;
    private String profileuid;
    private Firebase firebase;
    private ArrayList<PlaceOrderCommonClass> commonClasses;
    public MyOrdersFragment() {
        // Required empty public constructor
    }
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.orderslistview);
        button = (Button)view.findViewById(R.id.startShoppingNow);
        button.setOnClickListener(this);
        commonClasses = new ArrayList<>();
        orderAdapter = new PlaceOrderAdapter(getActivity(),commonClasses);
        recyclerView.setAdapter(orderAdapter);

        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        profileuid = sskey.getString("uid","0");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        Firebase.setAndroidContext(getContext());
        firebase  = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"PlacedOrders"+"/"+profileuid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    profileData();

                }else{
                    progressDialog.dismiss();
                    View forgotLayout = getActivity().findViewById(R.id.ordersStart);
                    forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getActivity()));
                    forgotLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().findViewById(R.id.ordersrecycler).setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return view;
    }
    private void profileData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        refreshdata();
    }
    public void refreshdata() {
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void getupdates(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            PlaceOrderCommonClass aClass = new PlaceOrderCommonClass();
            aClass.setUid(ds.getValue(PlaceOrderCommonClass.class).getUid());
            aClass.setOrderid(ds.getValue(PlaceOrderCommonClass.class).getOrderid());
            aClass.setOrderdate(ds.getValue(PlaceOrderCommonClass.class).getOrderdate());
            aClass.setOrderitems(ds.getValue(PlaceOrderCommonClass.class).getOrderitems());
            aClass.setOrdertime(ds.getValue(PlaceOrderCommonClass.class).getOrdertime());
            aClass.setStatus(ds.getValue(PlaceOrderCommonClass.class).getStatus());
            aClass.setOrderpayableamount(ds.getValue(PlaceOrderCommonClass.class).getOrderpayableamount());
            commonClasses.add(aClass);
            progressDialog.dismiss();
        }
        orderAdapter = new PlaceOrderAdapter(getActivity(),commonClasses);
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        orderAdapter.notifyDataSetChanged();

        orderAdapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ordersfile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                default:
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startShoppingNow:
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
