package com.indianservers.onlinegrocery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import adapter.GridAdapter;
import model.CenterRepository;
import model.ProductCommonClass;
import model.ProfileCommonClass;
import model.entities.Product;

/**
 * Created by Ratan on 7/29/2015.
 */
public class Beverages extends Fragment {
    private ArrayList<ProductCommonClass> arrayList = new ArrayList<ProductCommonClass>();
    private GridAdapter gridAdapter;
    public static SharedPreferences sskey;
    private RecyclerView gridview;
    String SSkey;
    private Firebase firebase;
    private ProgressDialog mProgressDialog;
    public Beverages() {

    }
    public static Beverages newInstance() {
        Beverages fragment = new Beverages();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vegetables_layout,container,false);
        gridview = (RecyclerView) view.findViewById(R.id.vegetablesgrid);
        gridview.setAlpha(0.9f);
        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SSkey = sskey.getString("sskey","0");
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...");
        timerDelayRemoveDialog(15*1000,mProgressDialog);
        mProgressDialog.show();
        Firebase.setAndroidContext(getContext());
        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Beverages");
        refreshdata();
        gridAdapter = new GridAdapter(getActivity(),arrayList);
        gridview.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        return view;
    }
    public  void refreshdata() {
        firebase.child("Beverages").orderByChild("ppid").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
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
            CenterRepository.getCenterRepository().setListOfProductsInShoppingList(arrayList);
            mProgressDialog.dismiss();
            gridAdapter = new GridAdapter(getActivity(), arrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    getActivity().getBaseContext());
            gridview.setLayoutManager(linearLayoutManager);
            gridview.setHasFixedSize(true);
            gridview.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();

            gridAdapter.SetOnItemClickListener(new GridAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    final String prname = ((TextView)view.findViewById(R.id.productname)).getText().toString();
                    final String prprice = ((TextView)view.findViewById(R.id.vendoreprice)).getText().toString();
                    final String prdprice = ((TextView)view.findViewById(R.id.pdiscprice)).getText().toString();
                    final String prquantity = ((TextView)view.findViewById(R.id.productquantity)).getText().toString();
                    final String prmeasure = ((TextView)view.findViewById(R.id.cartmeasure)).getText().toString();
                    final String prdesc = ((TextView)view.findViewById(R.id.productDesc)).getText().toString();
                    final String primageUrl = ((TextView)view.findViewById(R.id.primageUrl)).getText().toString();
                    final String puid = ((TextView)view.findViewById(R.id.pruid)).getText().toString();
                    final String ppid = ((TextView)view.findViewById(R.id.ppid)).getText().toString();
                    final String prpq = ((TextView)view.findViewById(R.id.prpq)).getText().toString();
                    final String prfinalQunatity = ((TextView)view.findViewById(R.id.quantity)).getText().toString();
                    Intent intent = new Intent(getContext(),SingleItemActivity.class);
                    intent.putExtra("pname",prname);
                    intent.putExtra("pprice",prprice);
                    intent.putExtra("pdprice",prdprice);
                    intent.putExtra("pquantity",prquantity);
                    intent.putExtra("pmeaure",prmeasure);
                    intent.putExtra("pdesc",prdesc);
                    intent.putExtra("imgurl",primageUrl);
                    intent.putExtra("puid",puid);
                    intent.putExtra("quantity",prfinalQunatity);
                    intent.putExtra("prpq",prpq);
                    intent.putExtra("ppid",ppid);
                    startActivity(intent);
                }
            });
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
