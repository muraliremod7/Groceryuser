package com.indianservers.onlinegrocery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import adapter.GridViewAdapter;
import model.CenterRepository;
import model.GridviewModel;
import model.PharmacyModel;
import model.PharmacyModelOne;

public class ProceedPharmacyActivity extends AppCompatActivity implements View.OnClickListener{
    private GridViewAdapter adapter;
    private GridView gridView;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    private Button addmore,proceed;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private SharedPreferences sskey;
    private String profileuid;
    private Firebase firebase;
    Uri uri;
    File f = null;
    private StorageReference storageReference;
    private String prescriptionName;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ArrayList<PharmacyModelOne> gridviewModels = new ArrayList<>();
    private ArrayList<PharmacyModel> pharmacyModels = new ArrayList<>();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(getApplicationContext());
        sskey = PreferenceManager.getDefaultSharedPreferences(this);
        profileuid = sskey.getString("uid","0");
        Firebase.setAndroidContext(ProceedPharmacyActivity.this);
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
                    Collections.reverse(gridviewModels);
                    adapter = new GridViewAdapter(ProceedPharmacyActivity.this, gridviewModels);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
//                    .show();
//        } else {
//            // Locate the image folder in your SD Card
//            file = new File(Environment.getExternalStorageDirectory(),"KiranaKart");
//            // Create a new folder if no folder named SDImageTutorial exist
//            file.mkdirs();
//        }
//    try{
//        if (file.isDirectory()) {
//            listFile = file.listFiles();
//            // Create a String array for FilePathStrings
//            FilePathStrings = new String[listFile.length];
//            // Create a String array for FileNameStrings
//            FileNameStrings = new String[listFile.length];
//
//            for (int i = 0; i < listFile.length; i++) {
//                // Get the path of the image file
//                GridviewModel gridviewModel = new GridviewModel();
//                gridviewModel.setImage(listFile[i].getAbsolutePath());
//                gridviewModel.setImagename(listFile[i].getName());
//                gridviewModels.add(gridviewModel);
//            }
//        }
//
//    }catch (NullPointerException e){
//
//    }

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
                        if (!Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            Toast.makeText(ProceedPharmacyActivity.this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // Locate the image folder in your SD Card
                            file = new File(Environment.getExternalStorageDirectory(),"KiranaKart");
                            // Create a new folder if no folder named SDImageTutorial exist
                            file.mkdirs();
                        }

                        if (file.isDirectory()) {
                            listFile = file.listFiles();
                            FileNameStrings = new String[listFile.length];



                            for (int i = 0; i < listFile.length; i++) {
                                // Get the path of the image file
                                Bitmap bmp = BitmapFactory.decodeFile(listFile[i].getAbsolutePath());

                            }
                        }
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
                        String strDate = mdformat.format(calendar.getTime());

                        PharmacyModel pharmacyModel = new PharmacyModel();
                        pharmacyModel.setOrdername(editText.getText().toString());
                        pharmacyModel.setOrderpid(profileuid);
                        pharmacyModel.setImageUrl(CenterRepository.getCenterRepository().getList());
                        pharmacyModel.setDate(strDate);
                        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/");
                        firebase.push().setValue(pharmacyModel);
                        Toast.makeText(ProceedPharmacyActivity.this,"Your Order Has Been Placed",Toast.LENGTH_SHORT).show();
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
