package com.indianservers.onlinegrocery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import adapter.CheckoutListviewAdapter;
import model.CenterRepository;
import model.OrdersCommonClass;


public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView checkoutlistview;
    public CheckoutListviewAdapter listCheckoutAdapter;
    public static SharedPreferences sskey;
    public String SSkey;
    public ArrayList<OrdersCommonClass> commonClasses = new ArrayList<>();
    private ProgressDialog progressDialog;
    public static TextView finalprice;
    private int itemsTotalCost=0;
    private Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        setContentView(R.layout.activity_check_out);
        checkoutlistview = (RecyclerView)findViewById(R.id.checkoutitems);
        finalprice = (TextView)findViewById(R.id.totalAmount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirana Mart");
        sskey = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SSkey = sskey.getString("uid","0");
        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        Button checkout = (Button)findViewById(R.id.checkoutplaceorder);
        checkout.setOnClickListener(this);
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"CartItems"+"/"+SSkey+"/"+SSkey);
        refresh();

    }
    private void refresh() {
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        OrdersCommonClass aClass = new OrdersCommonClass();
                        aClass.setPruid(data.getKey());
                        aClass.setPrName(data.getValue(OrdersCommonClass.class).getPrName());
                        aClass.setPrPrice(data.getValue(OrdersCommonClass.class).getPrPrice());
                        itemsTotalCost = itemsTotalCost+Integer.parseInt(data.getValue(OrdersCommonClass.class).getPrPrice());
                        aClass.setPrQunatity(data.getValue(OrdersCommonClass.class).getPrQunatity());
                        aClass.setPimage(data.getValue(OrdersCommonClass.class).getPimage());
                        aClass.setPrMeasure(data.getValue(OrdersCommonClass.class).getPrMeasure());
                        commonClasses.add(aClass);
                    }
                    CenterRepository.getCenterRepository().setListofAddress(commonClasses);
                    listCheckoutAdapter = new CheckoutListviewAdapter(CheckOutActivity.this,commonClasses);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            getApplicationContext());
                    checkoutlistview.setLayoutManager(linearLayoutManager);
                    checkoutlistview.setHasFixedSize(true);
                    checkoutlistview.setAdapter(listCheckoutAdapter);
                    listCheckoutAdapter.notifyDataSetChanged();
                    finalprice.setText("Rs. "+String.valueOf(itemsTotalCost));

                }
                else {
                    progressDialog.dismiss();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View dialogView = inflater.inflate(R.layout.start_shopping, null);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    Button button = (Button)dialogView.findViewById(R.id.StartShopping);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CheckOutActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkoutplaceorder:
                startActivity(new Intent(CheckOutActivity.this,PlaceOrderActivity.class));
                finish();

                break;
        }
    }
}
