package com.indianservers.onlinegrocery;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import adapter.GridAdapter;
import model.CenterRepository;
import model.ProductCommonClass;


public class VitaminCFragment extends Fragment {
    private ArrayList<ProductCommonClass> arrayList = new ArrayList<ProductCommonClass>();
    private GridAdapter gridAdapter;
    public static SharedPreferences sskey;
    private RecyclerView gridview;
    String SSkey;
    private Firebase firebase;
    private ProgressDialog mProgressDialog;

    public VitaminCFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VitaminCFragment newInstance(String param1, String param2) {
        VitaminCFragment fragment = new VitaminCFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vitamin_c, container, false);
        gridview = (RecyclerView) view.findViewById(R.id.vitamincgridview);
        gridview.setAlpha(0.9f);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...");
        timerDelayRemoveDialog(15 * 1000, mProgressDialog);
        mProgressDialog.show();
        Firebase.setAndroidContext(getContext());
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/VitaminC");
        refreshdata();
        gridAdapter = new GridAdapter(getActivity(),arrayList);
        gridview.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        return view;
    }
    public  void refreshdata() {
        firebase.child("VitaminC").orderByChild("ppid").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
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
                    final String puid = ((TextView)view.findViewById(R.id.pId)).getText().toString();
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                gridAdapter.getFilter().filter(query);
                gridAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                gridAdapter.getFilter().filter(query);
                gridAdapter.notifyDataSetChanged();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
