package com.nathan630pm.nk_final_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

//Created By: Nathan Kennedy, Student ID: 101333351

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "LoginActivity";
    private TextView TVCreateAccount;
    private EditText ETEmail;
    private EditText ETPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SwitchCompat swtRemember;
    private Boolean rememberMe = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        editor.clear();


        this.swtRemember = (SwitchCompat) findViewById(R.id.swtRemember);

        swtRemember.setOnCheckedChangeListener(this);






        this.ETEmail = findViewById(R.id.ETEmail);
        this.ETPassword = findViewById(R.id.ETPassword);


        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.btnSignIn.setEnabled(true);
        this.progressBar = findViewById(R.id.progressBar);

        this.TVCreateAccount = findViewById(R.id.TVCreateAccount);
        this.TVCreateAccount.setOnClickListener(this);
        this.btnSignIn.setOnClickListener(this);



        this.userViewModel = UserViewModel.getInstance();

        String savedEmail = sharedPreferences.getString(context.getString(R.string.saved_email), "");
        Log.d(TAG, "onCreate: SHARED PREFERENCE EMAIL: " + savedEmail);


//        editor.putString(context.getString(R.string.saved_email), savedEmail).apply();

        if(!savedEmail.equals("") && savedEmail != null) {
            userViewModel.getUserRepository().savedUserGetUser(savedEmail);

            Log.d(TAG, "onCreate: LOGGIN IN USER " + savedEmail);



        }

        this.userViewModel.getUserRepository().signInStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("SUCCESS")){
                    btnSignIn.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    if(rememberMe){
                        editor = sharedPreferences.edit();
//                    editor.clear();
                        editor.putString(context.getString(R.string.saved_email), ETEmail.getText().toString());
                        editor.apply();
                        editor.commit();
                    }

                    finish();
                    goToMain();
                }

                if (status.equals("SUCCESS_SAVE")) {
                    finish();
                    goToMain();
                }

                if(status.equals("LOGOUT")) {
                    btnSignIn.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Successfully Logged out.", Toast.LENGTH_LONG).show();
                }

                else if(status.equals("FAILURE")){
                    btnSignIn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Failed.", Toast.LENGTH_LONG).show();
                }

                else if(status.equals("LOADING")){
                    btnSignIn.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        this.rememberMe = b;
        Log.d(TAG, "onCheckedChanged: RememberMe: " + b);
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.TVCreateAccount: {
                    Intent signUpIntent = new Intent(this, RegisterActivity.class);
                    startActivity(signUpIntent);
                    break;
                }
                case R.id.btnSignIn: {
                    //verify the user
                    this.btnSignIn.setEnabled(false);
                    this.validateLogin();


                    break;
                }
                default: break;
            }
        }
    }





    private void goToMain() {


        this.finish();
        Intent mainIntent = new Intent(this, com.nathan630pm.nk_final_project.activities.MainActivity.class);
        startActivity(mainIntent);
    }

    private void validateLogin() {
        String email = this.ETEmail.getText().toString();
        String password = this.ETPassword.getText().toString();

        //ask the view model to validate user
        Boolean didComplete = this.userViewModel.validateUser(email.toLowerCase(), password);

        if(!didComplete){
            Toast.makeText(getApplicationContext(), "Make sure to enter all fields!", Toast.LENGTH_LONG).show();
            btnSignIn.setEnabled(true);
        }

    }
}