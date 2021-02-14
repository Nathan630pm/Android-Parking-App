package com.nathan630pm.nk_final_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private TextView TVCreateAccount;
    private EditText ETEmail;
    private EditText ETPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private Context context;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.ETEmail = findViewById(R.id.ETEmail);
        this.ETEmail.setText("Nathan630pm@outlook.com");
        this.ETPassword = findViewById(R.id.ETPassword);


        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.progressBar = findViewById(R.id.progressBar);

        this.TVCreateAccount = findViewById(R.id.TVCreateAccount);
        this.TVCreateAccount.setOnClickListener(this);
        this.btnSignIn.setOnClickListener(this);



        this.userViewModel = UserViewModel.getInstance();

        this.userViewModel.getUserRepository().signInStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("SUCCESS")){
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                    goToMain();
                }

                if(status.equals("LOGOUT")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Successfully Logged out.", Toast.LENGTH_LONG).show();
                }

                else if(status.equals("FAILURE")){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Failed.", Toast.LENGTH_LONG).show();
                }

                else if(status.equals("LOADING")){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
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
        Boolean didComplete = this.userViewModel.validateUser(email, password);

        if(!didComplete){
            Toast.makeText(getApplicationContext(), "Make sure to enter all fields!", Toast.LENGTH_LONG).show();
        }

    }
}