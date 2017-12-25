package com.indianservers.onlinegrocery;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import model.ProductCommonClass;
import model.ProfileCommonClass;
import services.SessionManager;

public class MyAccount extends Fragment implements View.OnClickListener{
    private List<ProductCommonClass> arrayList = new ArrayList<ProductCommonClass>();
    public String profileuid,profileName, profileEmail, profilePassword;
    private EditText profilenameedit,profileemailedit,profilemobileedit;
    private TextView profilenametext,profileemailtext,profilemobiletext;
    private SharedPreferences sskey;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private ImageView profilePic,capturepic;
    private Firebase firebase;
    private static String uuid;
    private Button save,updateprofile,updatePassword;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    public MyAccount() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Firebase.setAndroidContext(getContext());
        profileuid = sskey.getString("uid","0");
        profileName = sskey.getString("name", "0");
        profileEmail = sskey.getString("email","0");
        profilePassword = sskey.getString("password","0");
        profilenameedit = (EditText)view.findViewById(R.id.profileName);
        profileemailedit = (EditText)view.findViewById(R.id.profileEmail);
        profilemobileedit = (EditText)view.findViewById(R.id.profileMobile);

        profileemailedit.setText(profileEmail);

        profilenametext = (TextView)view.findViewById(R.id.profilenametext);
        profileemailtext = (TextView)view.findViewById(R.id.profileemailtext);
        profilemobiletext = (TextView)view.findViewById(R.id.profilemobiletext);

        save = (Button)view.findViewById(R.id.saveData);
        save.setOnClickListener(this);
        updateprofile = (Button)view.findViewById(R.id.profilechangeprofile);
        updateprofile.setOnClickListener(this);
        updatePassword = (Button)view.findViewById(R.id.profilechangepassword);
        updatePassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        profilePic = (ImageView)view.findViewById(R.id.profilepic);
        capturepic = (ImageView)view.findViewById(R.id.capture);
        capturepic.setOnClickListener(this);
        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String previouslyEncodedImage = sharedPrefs.getString("imagePreferance", "");
            if(previouslyEncodedImage==""){
            }else{
                profilePic.setImageBitmap(decodeBase64(previouslyEncodedImage));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Accounts"+"/"+profileuid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    profileData();

                }
                else{
                    progressDialog.dismiss();
                    View forgotLayout = getActivity().findViewById(R.id.profileedit);
                    forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getActivity()));
                    forgotLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().findViewById(R.id.profiledata).setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return view;
    }

    private void profileData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Accounts"+"/"+profileuid);
        refreshdata();
    }
    public void refreshdata() {
       firebase.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               getupdates(dataSnapshot);
           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               getupdates(dataSnapshot);
           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(FirebaseError firebaseError) {

           }
       });
    }

    private void getupdates(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
                uuid = ds.getKey();
                profilenametext.setText(ds.getValue(ProfileCommonClass.class).getProfileName());
                profileemailtext.setText(ds.getValue(ProfileCommonClass.class).getProfileEmail());
                profilemobiletext.setText(ds.getValue(ProfileCommonClass.class).getProfileMobile());
                progressDialog.dismiss();
            }

    }

    private void savedata(String name, String email, String mobile) {
        ProfileCommonClass commonClass = new ProfileCommonClass();
        commonClass.setProfileName(name);
        commonClass.setProfileEmail(email);
        commonClass.setProfileMobile(mobile);
        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Accounts"+"/"+profileuid+"/"+profileuid);
        firebase.push().child("").setValue(commonClass);
        editor = sskey.edit();
        editor.putString("name",name);
        editor.commit();
        View forgotLayout = getActivity().findViewById(R.id.profileedit);
        forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getActivity()));
        forgotLayout.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.profiledata).setVisibility(View.VISIBLE);
            }
        }, 500);
        profileData();
        Toasty.success(getContext(),"Profile Saved",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveData:
                String name = profilenameedit.getText().toString();
                String email = profileemailedit.getText().toString();
                String mobile = profilemobileedit.getText().toString();
                if(name.equals("")||email.equals("")||mobile.equals("")){
                    Toasty.info(getContext(),"Enter All fields",Toast.LENGTH_SHORT).show();
                }else{
                    savedata(name,email,mobile);
                }
                break;
            case R.id.profilechangeprofile:
                showAlertDialog();
                break;
            case R.id.profilechangepassword:
                updatepassword();
                break;
            case R.id.capture:
                selectImage();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profilePic.setImageBitmap(thumbnail);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("imagePreferance", encodeTobase64(bm));
        editor.commit();
        String previouslyEncodedImage = sharedPrefs.getString("imagePreferance", "");
        Bitmap bitmap = getResizedBitmap(decodeBase64(previouslyEncodedImage), 500);
        profilePic.setImageBitmap(bitmap);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    private void updatepassword() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_password, null);
        builder.setView(dialogView);
        builder.setTitle("Update Password");
        final AlertDialog alertDialog = builder.create();
        final EditText profileoldpass = (EditText)dialogView.findViewById(R.id.profileoldpassword);
        final EditText profilenewpass = (EditText)dialogView.findViewById(R.id.profilenewpassword);
        final EditText profilenewconpass = (EditText)dialogView.findViewById(R.id.profilenewconpassword);

        Button save = (Button)dialogView.findViewById(R.id.updatepassword);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(profilenewpass.getText().toString().equals(profilenewconpass.getText().toString())){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(profileEmail, profilePassword);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(profilenewconpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toasty.success(getContext(),"Password updated",Toast.LENGTH_SHORT).show();
                                                    SessionManager sessionManager = new SessionManager(getContext());
                                                    sessionManager.logoutUser();
                                                } else {
                                                    Toasty.error(getContext(),"Error password not updated",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toasty.error(getContext(),"Error auth failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    alertDialog.dismiss();
                }else{
                    alertDialog.dismiss();
                    Toasty.error(getContext(),"New Password and Confirm Password are not matche",Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.show();

    }


    private void showAlertDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.profile_layoutt, null);
            builder.setView(dialogView);
            builder.setTitle("Update Profile");
            final AlertDialog alertDialog = builder.create();
            final EditText profilenameedit = (EditText)dialogView.findViewById(R.id.profileNameal);
            final EditText profileemailedit = (EditText)dialogView.findViewById(R.id.profileEmailal);
            final EditText profilemobileedit = (EditText)dialogView.findViewById(R.id.profileMobileal);

        profilenameedit.setText(profilenametext.getText().toString());
        profileemailedit.setText(profileemailtext.getText().toString());
        profilemobileedit.setText(profilemobiletext.getText().toString());

        Button save = (Button)dialogView.findViewById(R.id.saveDataal);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileCommonClass commonClass = new ProfileCommonClass();
                commonClass.setProfileName(profilenameedit.getText().toString());
                commonClass.setProfileEmail(profileemailedit.getText().toString());
                commonClass.setProfileMobile(profilemobileedit.getText().toString());

                firebase.child(profileuid).child(uuid).setValue(commonClass);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
}
