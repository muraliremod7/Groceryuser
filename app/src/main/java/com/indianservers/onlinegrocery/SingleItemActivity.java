package com.indianservers.onlinegrocery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.CenterRepository;
import model.OrdersCommonClass;
import model.ProductCommonClass;

public class SingleItemActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView productName, productPrice, prodctDescription;
    private ImageView ProductImage;
    private Button singleac;
    private TextView singleQunatity;
    private TextView singleIncrease,singleDecrease;
    public static SharedPreferences sskey,item;
    private String Pname, Pid, PPrice, Pdesc,Pimage;
    private ArrayAdapter<String> prodquAdapter;
    private static String Quantity,ppid,ProductMeasure,Productpr;
    public String FinalPrice;
    String category;
    String uid;
    private List<ProductCommonClass> classes = new ArrayList<>();
    private Firebase firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"listOfItems"+"/"+uid);
    OrdersCommonClass commonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kirana Mart");

        sskey = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uid = sskey.getString("uid", "0");

        ProductImage = (ImageView) findViewById(R.id.singleprimage);
        productName = (TextView) findViewById(R.id.singlepname);
        productPrice = (TextView) findViewById(R.id.singlepdiscprice);
        prodctDescription = (TextView)findViewById(R.id.singleproductDescription);
        singleQunatity = (TextView)findViewById(R.id.singleQuantity);
        singleDecrease = (TextView)findViewById(R.id.singleDecrese);
        singleDecrease.setOnClickListener(this);
        singleIncrease = (TextView)findViewById(R.id.singleIncrease);
        singleIncrease.setOnClickListener(this);
        singleac = (Button) findViewById(R.id.singleaddtoCart);
        singleac.setOnClickListener(this);
        classes = CenterRepository.getCenterRepository().getListInCart();

        final Intent intent = getIntent();
        Pname = intent.getStringExtra("pname");
        PPrice = intent.getStringExtra("pdprice");
        Pdesc = intent.getStringExtra("pdesc");
        Pid = intent.getStringExtra("puid");
        Quantity = intent.getStringExtra("quantity");
        ProductMeasure = intent.getStringExtra("pmeaure");
        ppid = intent.getStringExtra("ppid");
        Pimage = intent.getStringExtra("imgurl");
        singleQunatity.setText(intent.getStringExtra("quantity"));
        Glide.with(this)
                .load(intent.getStringExtra("imgurl"))
                .into(ProductImage);
    }

//    private void finalPrice() {
//        // Getting Quantity Value from Edittext......
//        int pQ = Integer.parseInt(Productqu);
//        float pP = Float.parseFloat(Productpr);
//        if(Quantity.equals("KG")){
//            FinalPrice = String.valueOf(pQ*pP);
//            productPrice.setText(FinalPrice);
//        }
//         else if(Quantity.equals("GMS")){
//            FinalPrice = String.valueOf((pQ*pP)/1000);
//            productPrice.setText(FinalPrice);
//        }
//        else if(Quantity.equals("PCS")){
//            FinalPrice = String.valueOf(pQ*pP);
//            productPrice.setText(FinalPrice);
//        }
//        else if(Quantity.equals("DOZENS")){
//            FinalPrice = String.valueOf((pQ*12)*pP);
//            productPrice.setText(FinalPrice);
//        }
//        else if(Quantity.equals("BUNDLE")){
//            FinalPrice = String.valueOf(pQ*pP);
//            productPrice.setText(FinalPrice);
//
//        }
//
//
//
//        //After Selection Measure multiplication through Measure......
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homefragmentmenu, menu);
        MenuItem itemCart = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        item =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String itemlength = item.getString("Length","0");
        setBadgeCount(getApplicationContext(), icon, itemlength);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.cart:
                Intent intent = new Intent(this, CheckOutActivity.class);
                startActivity(intent);
                break;
            default:

        }
        return false;
    }
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.singleIncrease:
                int i = Integer.parseInt(Quantity);
                    i++;
                    singleQunatity.setText(String.valueOf(i));
                    Quantity = String.valueOf(i);
                break;
            case R.id.singleDecrese:
                int j = Integer.parseInt(Quantity);
                if(j==0){

                }else{
                    j--;
                    singleQunatity.setText(String.valueOf(j));
                    Quantity = String.valueOf(j);
                }

                break;
            case R.id.singleaddtoCart:

                commonClass = new OrdersCommonClass();
                commonClass.setPrpid(ppid);
                commonClass.setPruid(Pid);
                commonClass.setPrName(Pname);
                int finalprice = Integer.parseInt(PPrice)*Integer.parseInt(Quantity);
                commonClass.setPrPrice(String.valueOf(finalprice));
                commonClass.setPimage(Pimage);
                commonClass.setPrQunatity(Quantity);
                commonClass.setPrMeasure(ProductMeasure);
                if(Quantity.equals("0")){
                    Toast.makeText(SingleItemActivity.this,"You didn't Select item",Toast.LENGTH_SHORT).show();

                }else{
                    firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"CartItems"+"/"+uid);
                    firebase.child(uid).orderByChild(Pid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){

                                firebase.child(uid).push().setValue(commonClass);
                                Toast.makeText(SingleItemActivity.this,"Item Added To Cart",Toast.LENGTH_SHORT).show();
                            }else {
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    String key = ds.getKey();
                                    String puid = ds.getValue(OrdersCommonClass.class).getPruid();
                                    if(puid.equals(Pid)){
                                        firebase.child(uid).child(key).setValue(commonClass);
                                    }
                                }
                            }



                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                break;
        }
    }
}
