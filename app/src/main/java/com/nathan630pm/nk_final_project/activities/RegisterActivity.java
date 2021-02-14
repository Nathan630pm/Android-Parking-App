package com.nathan630pm.nk_final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private Button BCreateAcc;
    private EditText ETEmail;
    private EditText ETPassword;
    private EditText ETConfirmPassword;
    private EditText ETName;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.ETEmail = findViewById(R.id.ETEmail);
        this.ETPassword = findViewById(R.id.ETPassword);
        this.ETConfirmPassword = findViewById(R.id.ETConfirmPassword);
        this.ETName = findViewById(R.id.ETName);
        this.BCreateAcc = findViewById(R.id.BCreateAcc);
        this.BCreateAcc.setOnClickListener(this);

        this.userViewModel = UserViewModel.getInstance();


    }

    @Override
    public void onClick(View view) {
        if (view != null){
            switch (view.getId()){
                case R.id.BCreateAcc: {
                    if (this.validateData()){
                        //save data to database
                        this.saveUserToDB();
                        //go to main activity
                        this.goToMain();
                    }
                }
                default: break;
            }
        }
    }

    private void saveUserToDB(){
        User newUser = new User();

        newUser.setEmail(this.ETEmail.getText().toString());
        newUser.setName(this.ETName.getText().toString());

        this.userViewModel.addUser(this.ETEmail.getText().toString(), this.ETPassword.getText().toString(), newUser);
    }
    private void goToMain(){
        this.finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private Boolean validateData(){
        if (this.ETEmail.getText().toString().isEmpty()){
            this.ETEmail.setError("Please enter email");
            return false;
        }
        // add a check for valid email address format
        if (this.ETPassword.getText().toString().isEmpty()){
            this.ETPassword.setError("Password cannot be empty");
            return false;
        }
        if (this.ETConfirmPassword.getText().toString().isEmpty()){
            this.ETConfirmPassword.setError("Please provide confirm password");
            return false;
        }
        if (!ETPassword.getText().toString().equals(this.ETConfirmPassword.getText().toString())){
            this.ETPassword.setError("Both passwords must be same");
            this.ETConfirmPassword.setError("Both passwords must be same");
            return false;
        }
        return true;
    }
}