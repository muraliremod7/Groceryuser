package com.indianservers.onlinegrocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import adapter.GridItemView;
import adapter.GridViewAdapter;
import es.dmoral.toasty.Toasty;
import model.CenterRepository;
import model.PharmacyModel;
import model.PharmacyModelOne;

public class ProceedPharmacyActivity extends AppCompatActivity implements View.OnClickListener{
    private GridViewAdapter adapter;
    private GridView gridView;
    private ArrayList<Integer> positions = new ArrayList<>();
    private File[] listFile;
    private Button addmore,proceed;
    private SharedPreferences sskey;
    private String profileuid;
    private Firebase firebase;
    private ArrayList<String> selectedStrings;
    ProgressDialog progressDialog;
    private ArrayList<PharmacyModelOne> gridviewModels = new ArrayList<>();
    private ArrayList<PharmacyModelOne> finalorders = new ArrayList<>();
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_pharmacy);
        gridView = (GridView)findViewById(R.id.gridview);
        addmore = (Button)findViewById(R.id.addmore);
        addmore.setOnClickListener(this);
        proceed = (Button)findViewById(R.id.proceed);
        proceed.setOnClickListener(this);
        selectedStrings = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(getApplicationContext());
        sskey = PreferenceManager.getDefaultSharedPreferences(this);
        profileuid = sskey.getString("uid","0");
        Firebase.setAndroidContext(ProceedPharmacyActivity.this);
        progressDialog = new ProgressDialog(ProceedPharmacyActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/"+profileuid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        PharmacyModelOne modelOne = new PharmacyModelOne();
                        modelOne.setUid(ds.getKey());
                        modelOne.setPresName(ds.getValue(PharmacyModelOne.class).getPresName());
                        modelOne.setPresDesc(ds.getValue(PharmacyModelOne.class).getPresDesc());
                        modelOne.setPresImageUrl(ds.getValue(PharmacyModelOne.class).getPresImageUrl());
                        gridviewModels.add(modelOne);
                    }
                    progressDialog.dismiss();
                    Collections.reverse(gridviewModels);
                    adapter = new GridViewAdapter(ProceedPharmacyActivity.this, gridviewModels);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            int selectedIndex = adapter.selectedPositions.indexOf(position);
                            if (selectedIndex > -1) {
                                adapter.selectedPositions.remove(selectedIndex);
                                ((GridItemView) v).display(false);
                                selectedStrings.remove(String.valueOf(parent.getItemAtPosition(position)));
                                    finalorders.remove(selectedIndex);
                            } else {
                                PharmacyModelOne pharmacyModelOne = new PharmacyModelOne();
                                pharmacyModelOne.setUid(((TextView)v.findViewById(R.id.pharmacysingleuid)).getText().toString());
                                pharmacyModelOne.setPresName(((TextView)v.findViewById(R.id.pharmacysingletextview)).getText().toString());
                                pharmacyModelOne.setPresDesc(((TextView)v.findViewById(R.id.pharmacysinglepresdata)).getText().toString());
                                pharmacyModelOne.setPresImageUrl(((TextView)v.findViewById(R.id.pharmacysingleimageurl)).getText().toString());
                                finalorders.add(pharmacyModelOne);
                                adapter.selectedPositions.add(position);
                                ((GridItemView) v).display(true);
                                selectedStrings.add(String.valueOf(parent.getItemAtPosition(position)));
                            }
                        }
                    });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addmore:
                Intent intent = new Intent(ProceedPharmacyActivity.this,PharmacyActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.proceed:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProceedPharmacyActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.single_edittext, null);
                builder.setView(dialogView);
                builder.setTitle("Confirm Order");
                final AlertDialog alertDialog = builder.create();
                final EditText editText = (EditText)dialogView.findViewById(R.id.getlorderName);
                final  Button cancel = (Button)dialogView.findViewById(R.id.cancelOrder);
                final  Button confirm = (Button)dialogView.findViewById(R.id.confirmOrder);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
                        String strDate = mdformat.format(calendar.getTime());
                        if(finalorders.size()==0){
                            Toasty.error(ProceedPharmacyActivity.this,"Your Order is not proceed select minimum One Prescription",Toast.LENGTH_SHORT).show();
                        }else {
                            for(int i = 0;i<finalorders.size();i++){
                                PharmacyModelOne pharmacyModelOne = new PharmacyModelOne();
                                pharmacyModelOne.setUid(finalorders.get(i).getUid());
                                pharmacyModelOne.setPresName(finalorders.get(i).getPresName());
                                pharmacyModelOne.setPresDesc(finalorders.get(i).getPresDesc());
                                pharmacyModelOne.setPresImageUrl(finalorders.get(i).getPresImageUrl());
                                pharmacyModelOne.setDate(strDate);
                                pharmacyModelOne.setStatus("Initiated");
                                pharmacyModelOne.setPid(profileuid);
                                firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/"+"Admin"+"/"+editText.getText().toString());
                                firebase.push().setValue(pharmacyModelOne);

                            }
                            finalorders.clear();
                            selectedStrings.clear();
                            adapter.selectedPositions.clear();
                            Toasty.success(ProceedPharmacyActivity.this,"Your Order Has Been Placed",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

                break;
        }
    }
}
