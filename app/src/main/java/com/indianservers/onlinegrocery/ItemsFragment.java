package com.indianservers.onlinegrocery;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import adapter.GridAdapter;
import model.CenterRepository;
import model.ProductCommonClass;


public class ItemsFragment extends Fragment{
    private ArrayList<ProductCommonClass> arrayList = new ArrayList<ProductCommonClass>();
    private SharedPreferences sskey;
    private String type;
    private RecyclerView allitemsList;
    private ProgressDialog mProgressDialog;
    private Firebase firebase;
    private GridAdapter gridAdapter;
    private ImageView imageView, singlecheckout;
    Toolbar toolbarbe;

    public ItemsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        type = sskey.getString("categoryType","0");
        toolbarbe = (Toolbar)getActivity().findViewById(R.id.maintoolbar);
        toolbarbe.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.itemfragtoo);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        allitemsList = (RecyclerView)view.findViewById(R.id.allitemsGridview);
        allitemsList.setAlpha(0.9f);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Firebase.setAndroidContext(getContext());

        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+type);
        refreshdata();
        return view;
    }
    public  void refreshdata() {
        firebase.child(type).orderByChild("ppid").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
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
            allitemsList.setLayoutManager(linearLayoutManager);
            allitemsList.setAdapter(gridAdapter);
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

        }
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

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                gridAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gridAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                toolbarbe.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStackImmediate() ;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
