package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //widgets
    private EditText ev_email, ev_password;
    private Button b_sign_in, b_registration, b_guest_log_in;
    private ProgressBar progressBar;
    private Button buttonForgetPassword;
    //for popup box
    private EditText email_pop_up;
    private Button proceed_button;


    //variables
    private MainViewModel  mainViewModel;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ev_email = findViewById(R.id.ev_email_login);
        ev_password = findViewById(R.id.ev_password_registration    );
        b_sign_in = findViewById(R.id.b_sign_in);
        b_guest_log_in = findViewById(R.id.button_guest_log_in);
        b_registration  = findViewById(R.id.b_register_login);
        progressBar = findViewById(R.id.progressBar_login);
        buttonForgetPassword = findViewById(R.id.button_forget_password);
        progressBar.setVisibility(View.GONE);

        b_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on register button clicked
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: Starting Registration activity.");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        b_guest_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // guest login
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth
                        .getInstance()
                        .signInAnonymously()
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    toastMessage("Error Authenticating");
                                    return;
                                }else {
                                    toastMessage("Logging in.");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                    finish();

                                }
                            }
                        });
            }
        });

        b_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On sign in button clicked
                progressBar.setVisibility(View.VISIBLE);

                String email = ev_email.getText().toString();
                String password = ev_password.getText().toString();
                if(email.isEmpty()){
                    ev_email.setError("Please Enter Email Id!");
                    ev_email.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }else if(password.isEmpty()){
                    ev_password.setError("Please Enter Password!");
                    ev_password.requestFocus();
                    progressBar.setVisibility(View.GONE);


                }
                else if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All Fields are Empty.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                else if(!email.isEmpty() && !password.isEmpty()){
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                            if(!task.isSuccessful()){
                                toastMessage("Login Error!!!");
                            }
                            else{
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                toastMessage("Signing in Please Wait..");
                                finish();
                            }
                        }
                    });

                }else{
                    toastMessage("Error occurred!");
                }
            }
        });

        auth = FirebaseAuth.getInstance(); // initializing  the FirebaseAuth

        authStateListener  = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){

                    Log.d(TAG, "onAuthStateChanged: Logging in");
                    Log.d(TAG, "onAuthStateChanged: Starting MainActivity...");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        buttonForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                final AlertDialog dialog;
                View v = getLayoutInflater().inflate(R.layout.layout_forget_password, null);
                proceed_button = v.findViewById(R.id.button_forget_password_pop_up);
                email_pop_up = v.findViewById(R.id.editView_email_forget_password);
                builder.setView(v);
                dialog = builder.create();

                proceed_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(email_pop_up.getText().toString().trim().isEmpty()){
                            Toast.makeText(LoginActivity.this, "Enter valid Email address", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainViewModel.resetPassword(email_pop_up.getText().toString());
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}