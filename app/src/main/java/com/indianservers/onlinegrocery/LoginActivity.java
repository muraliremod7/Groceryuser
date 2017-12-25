package com.indianservers.onlinegrocery;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.SecureRandom;

import es.dmoral.toasty.Toasty;
import model.AlertDialogManager;
import services.ConnectionDetector;
import services.SessionManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText email, password;
    private Button LoginButton;
    private TextView clicktoreg;
    private String Phone,Pin;
    private SessionManager session;
    private SharedPreferences.Editor editor;
    private ConnectionDetector connection;
    private FirebaseAuth auth;
    private AlertDialogManager alert = new AlertDialogManager();
    public static String teamid,sskey;
    private ProgressDialog progressBar;

    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = (RelativeLayout)findViewById(R.id.snackbarlogin);
        email = (EditText)findViewById(R.id.emailId);
        password = (EditText)findViewById(R.id.passWord);
        LoginButton = (Button)findViewById(R.id.loginbutton);
        LoginButton.setOnClickListener(this);
        clicktoreg = (TextView)findViewById(R.id.clicktoRegister);
        clicktoreg.setOnClickListener(this);

        session = new SessionManager(LoginActivity.this);
        connection = new ConnectionDetector(LoginActivity.this);
        LinearLayout loginrelative = (LinearLayout) findViewById(R.id.loginrelative);
        loginrelative.setAlpha(0.9f);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Please Wait .....");
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbutton:
                progressBar.show();
                final String loginemail = email.getText().toString();
                final String loginpassword = password.getText().toString();

                if (TextUtils.isEmpty(loginemail)) {
                    Toasty.info(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(loginpassword)) {
                    Toasty.info(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth = FirebaseAuth.getInstance();
                //authenticate user
                auth.signInWithEmailAndPassword(loginemail, loginpassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    progressBar.dismiss();
                                    setSnackBar(layout,"Check Your Email and Password");
                                    // there was an error
                                } else {
                                    progressBar.dismiss();
                                    session.createLoginSession(loginemail,loginpassword);
                                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    editor = settings.edit();
                                    editor.putString("password", loginpassword);
                                    editor.putString("email",loginemail);
                                    String key = loginemail.replace("@","1");
                                    String uniqkey = key.replace(".","2");
                                    editor.putString("uid", uniqkey);
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // Add new Flag to start new Activity
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Toasty.success(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                break;
            case R.id.clicktoRegister:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
    public static void setSnackBar(View coordinatorLayout, String snackTitle) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
