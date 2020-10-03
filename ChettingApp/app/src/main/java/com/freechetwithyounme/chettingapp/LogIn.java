package com.freechetwithyounme.chettingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    private EditText mlog_user, mlog_password;
    private Button mlogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private long backPressTime;

    private static boolean pressBackAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lon_in);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        setTitle("Sign In");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        mlog_user= findViewById(R.id.log_username);
        mlog_password= findViewById(R.id.log_password);
        mlogin= findViewById(R.id.log_in);
        TextView goRegister = findViewById(R.id.logIN_register);
        progressDialog= new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();

        CheckConnection();

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LogIn.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void CheckConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ConnectionInfo= cm.getActiveNetworkInfo();

        if (ConnectionInfo!= null){
            try {
                mlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email= mlog_user.getText().toString();
                        String password= mlog_password.getText().toString();
                        if (TextUtils.isEmpty(email)){
                            mlog_user.setError("Enter email");
                        } else if(TextUtils.isEmpty(password)){
                            mlog_password.setError("Enter password");
                        } else {
                            progressDialog.setTitle("Sign in...");
                            progressDialog.setMessage("please wait while we create your account");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()){
                                        Intent intent= new Intent(LogIn.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Please Try again",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            Log.d("Virtual_Keyboard_Error:",e.getMessage());
                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(this, "Log in Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        /*if (backPressTime +2000>System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "press again for exit", Toast.LENGTH_SHORT).show();
        }
        backPressTime= System.currentTimeMillis();*/

        if (!pressBackAgain){
            Toast.makeText(this, "press again for exit", Toast.LENGTH_SHORT).show();
            pressBackAgain= true;
        } else {
            Intent intent= new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        new CountDownTimer(3000, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                pressBackAgain= false;
            }
        }.start();
    }
}
