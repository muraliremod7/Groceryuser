package com.indianservers.onlinegrocery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import adapter.CheckoutAdapter;
import adapter.CheckoutListviewAdapter;
import model.OrdersCommonClass;

public class OrderSummuryActivity extends AppCompatActivity {
    private RecyclerView checkoutlistview;
    public CheckoutListviewAdapter listCheckoutAdapter;
    private Firebase firebase;
    public ArrayList<OrdersCommonClass> commonClasses = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summury);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkoutlistview = (RecyclerView)findViewById(R.id.productsrecycler);
        Firebase.setAndroidContext(getApplicationContext());
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/" + "AdminTable" + "/" + "CartItemsByid" + "/" + id);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        OrdersCommonClass aClass = new OrdersCommonClass();
                        aClass.setPruid(data.getKey());
                        aClass.setPrMeasure(data.getValue(OrdersCommonClass.class).getPrMeasure());
                        aClass.setPrQunatity(data.getValue(OrdersCommonClass.class).getPrQunatity());
                        aClass.setPrName(data.getValue(OrdersCommonClass.class).getPrName());
                        aClass.setPrPrice(data.getValue(OrdersCommonClass.class).getPrPrice());
                        aClass.setPrQunatity(data.getValue(OrdersCommonClass.class).getPrQunatity());
                        aClass.setPimage(data.getValue(OrdersCommonClass.class).getPimage());
                        aClass.setPrMeasure(data.getValue(OrdersCommonClass.class).getPrMeasure());
                        commonClasses.add(aClass);
                    }
                    listCheckoutAdapter = new CheckoutListviewAdapter(OrderSummuryActivity.this,commonClasses);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            getApplicationContext());
                    checkoutlistview.setLayoutManager(linearLayoutManager);
                    checkoutlistview.setHasFixedSize(true);
                    checkoutlistview.setAdapter(listCheckoutAdapter);

                    listCheckoutAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
