package com.indianservers.onlinegrocery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.indianservers.onlinegrocery.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import adapter.GridAdapter;
import model.AllCommonClass;
import model.ProductCommonClass;

public class BabyCareFragment extends Fragment {
    private List<ProductCommonClass> arrayList = new ArrayList<>();
    GridAdapter gridAdapter;
    public static SharedPreferences sskey;
    RecyclerView gridview;
    String SSkey;
    private Firebase firebase;
    private ProgressDialog mProgressDialog;

    public BabyCareFragment() {
        // Required empty public constructor
    }

    public static BabyCareFragment newInstance() {
        BabyCareFragment cakesDairy = new BabyCareFragment();
        return cakesDairy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baby_care, container, false);
        gridview = (RecyclerView) view.findViewById(R.id.babycaregridview);
        gridview.setAlpha(0.9f);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...");
        timerDelayRemoveDialog(15 * 1000, mProgressDialog);
        mProgressDialog.show();
        Firebase.setAndroidContext(getContext());
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/" + "BabyCare");
        refreshdata();
        return view;
    }

    public  void refreshdata() {
        firebase.child("BabyCare").orderByChild("ppid").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void getupdates(DataSnapshot dataSnapshot){

        arrayList.clear();

        for(DataSnapshot ds :dataSnapshot.getChildren()){

            ProductCommonClass d= new ProductCommonClass();
            d.setPuid(ds.getKey());
            d.setPpid(ds.getValue(ProductCommonClass.class).getPpid());
            d.setProductName(ds.getValue(ProductCommonClass.class).getProductName());
            d.setProductPrice(ds.getValue(ProductCommonClass.class).getProductPrice());
            d.setProductdPrice(ds.getValue(ProductCommonClass.class).getProductdPrice());
            d.setProductDesc(ds.getValue(ProductCommonClass.class).getProductDesc());
            d.setProductImage(ds.getValue(ProductCommonClass.class).getProductImage());
            d.setProductQuantity(ds.getValue(ProductCommonClass.class).getProductQuantity());
            d.setProductMeasureType(ds.getValue(ProductCommonClass.class).getProductMeasureType());
            d.setPrqu(ds.getValue(ProductCommonClass.class).getPrqu());
            arrayList.add(d);

        }
        if(arrayList.size()>0)
        {
            mProgressDialog.dismiss();
            gridAdapter = new GridAdapter(getActivity(), arrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    getActivity().getBaseContext());
            gridview.setLayoutManager(linearLayoutManager);
            gridview.setHasFixedSize(true);
            gridview.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();
//            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String prname = ((TextView)view.findViewById(R.id.productname)).getText().toString();
//                    String prprice = ((TextView)view.findViewById(R.id.vendoreprice)).getText().toString();
//                    String prdprice = ((TextView)view.findViewById(R.id.pdiscprice)).getText().toString();
//                    String prquantity = ((TextView)view.findViewById(R.id.productquantity)).getText().toString();
//                    String prmeasure = ((TextView)view.findViewById(R.id.cartmeasure)).getText().toString();
//                    String prdesc = ((TextView)view.findViewById(R.id.productDesc)).getText().toString();
//                    String primageUrl = ((TextView)view.findViewById(R.id.primageUrl)).getText().toString();
//                    Intent intent = new Intent(getContext(),SingleItemActivity.class);
//                    intent.putExtra("pname",prname);
//                    intent.putExtra("pprice",prprice);
//                    intent.putExtra("pdprice",prdprice);
//                    intent.putExtra("pquantity",prquantity);
//                    intent.putExtra("pmeaure",prmeasure);
//                    intent.putExtra("pdesc",prdesc);
//                    intent.putExtra("imgurl",primageUrl);
//                    startActivity(intent);
//                }
//            });

        }else
        {
            gridAdapter = new GridAdapter(getActivity(),arrayList);
            try{
                gridview.setAdapter(gridAdapter);
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
    public void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
    }
}
