package com.indianservers.onlinegrocery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import adapter.GridViewAdapter;
import model.CenterRepository;
import model.PharmacyModel;
import model.PharmacyModelOne;

public class PharmacyActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout uploadPrescription,refillOrder;
    private EditText presname,presorder;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private SharedPreferences sskey;
    public String uid;
    private StorageReference storageReference;
    private static final String SAVED_INSTANCE_URI = "uri";

    static List<String> strings = new ArrayList<String>();
    Uri uri;
    private String imageUrl;
    private String prescriptionName;
    Firebase firebase;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sskey = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uid = sskey.getString("uid","0");
        storageReference = FirebaseStorage.getInstance().getReference();
//        uploadPrescription = (LinearLayout)findViewById(R.id.uploadprec);
//        uploadPrescription.setOnClickListener(this);
//        refillOrder = (LinearLayout)findViewById(R.id.refillorder);
//        refillOrder.setOnClickListener(this);
        progressDialog = new ProgressDialog(PharmacyActivity.this);
        Firebase.setAndroidContext(PharmacyActivity.this);
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
//            case R.id.uploadprec:
//                selectImage();
//                break;
//            case R.id.refillorder:
//                startActivity(new Intent(PharmacyActivity.this,ProceedPharmacyActivity.class));
//                break;
        }
    }
    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        builder.setTitle("Add Your Prescription");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(PharmacyActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AlertDialog.Builder builder = new AlertDialog.Builder(PharmacyActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_edittext, null);
        builder.setView(dialogView);
        builder.setTitle("Prescription");
        final AlertDialog alertDialog = builder.create();
        presname = (EditText)dialogView.findViewById(R.id.getlorderName);
        presorder = (EditText)dialogView.findViewById(R.id.getlorderdesc);
        final TextView textView = (TextView)dialogView.findViewById(R.id.textpres);
        presname.setHint("Enter Prescription Name");
        presorder.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        presorder.setHint("Enter Presc Order Details");
        final  Button cancel = (Button)dialogView.findViewById(R.id.cancelOrder);
        final  Button confirm = (Button)dialogView.findViewById(R.id.confirmOrder);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescriptionName = presname.getText().toString();
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == SELECT_FILE){
                        onSelectFromGalleryResult(data);
                    }

                    else if (requestCode == REQUEST_CAMERA){
                        onCaptureImageResult(data);
                    }

                }
                alertDialog.dismiss();
                presorder.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
    private void onCaptureImageResult(Intent data) {
        progressDialog.setMessage("Prescription Uploading.....");
        progressDialog.show();
        File f = null;
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"KiranaKart"+"/");
        destination.mkdir();
        f = new File(destination,prescriptionName+ ".png");

        try {
            FileOutputStream strm = new FileOutputStream(f);
            thumbnail.compress(Bitmap.CompressFormat.PNG, 80, strm);
            strm.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try{

            uri = Uri.fromFile(f);
            StorageReference filepath = storageReference.child("PharmacyPhotos").child(uid).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUrl = taskSnapshot.getDownloadUrl().toString();
                    PharmacyModelOne modelOne = new PharmacyModelOne();
                    modelOne.setUid(uid);
                    modelOne.setPresName(prescriptionName);
                    modelOne.setPresDesc(presorder.getText().toString());
                    modelOne.setPresImageUrl(imageUrl);
                    firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/"+uid);
                    firebase.push().setValue(modelOne);
                    progressDialog.dismiss();
                    Intent intent = new Intent(PharmacyActivity.this,ProceedPharmacyActivity.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e){

        }

    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        File f = null;
        Bitmap bm=null;
        if (data != null) {
            try {
                progressDialog.setMessage("Prescription Uploading ...");
                progressDialog.show();
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"KiranaKart"+"/");
                destination.mkdir();
                f = new File(destination, prescriptionName+ ".png");
                FileOutputStream strm = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 80, strm);
                strm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                Uri uri = Uri.fromFile(f);
                StorageReference filepath = storageReference.child("PharmacyPhotos").child(uid).child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageUrl = taskSnapshot.getDownloadUrl().toString();
                        PharmacyModelOne modelOne = new PharmacyModelOne();
                        modelOne.setUid(uid);
                        modelOne.setPresName(prescriptionName);
                        modelOne.setPresDesc(presorder.getText().toString());
                        modelOne.setPresImageUrl(imageUrl);
                        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Pharmacy"+"/"+uid);
                        firebase.push().setValue(modelOne);
                        progressDialog.dismiss();
                        startActivity(new Intent(PharmacyActivity.this,ProceedPharmacyActivity.class));
                    }
                });
            }catch (Exception e){

            }
        }
    }
}
