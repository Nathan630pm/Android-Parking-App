package com.nathan630pm.nk_final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Created By: Nathan Kennedy, Student ID: 101333351

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private Button BCreateAcc;
    private EditText ETEmail;
    private EditText ETPassword;
    private EditText ETConfirmPassword;
    private EditText ETName;
    private EditText ETPlateNo;
    private EditText ETPhone;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.ETEmail = findViewById(R.id.ETEmail);
        this.ETPassword = findViewById(R.id.ETPassword);
        this.ETConfirmPassword = findViewById(R.id.ETConfirmPassword);
        this.ETName = findViewById(R.id.ETName);
        this.ETPlateNo = findViewById(R.id.ETPlateNo);
        this.ETPhone = findViewById(R.id.ETPhone);
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
                        this.validateLogin();
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
        newUser.setContactNumber(this.ETPhone.getText().toString());
        newUser.setCarPlateNumber(this.ETPlateNo.getText().toString());

        this.userViewModel.addUser(this.ETEmail.getText().toString(), this.ETPassword.getText().toString(), newUser);
    }
    private void goToMain(){
        this.finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void validateLogin() {
        String email = this.ETEmail.getText().toString();
        String password = this.ETPassword.getText().toString();

        //ask the view model to validate user
        Boolean didComplete = this.userViewModel.validateUser(email.toLowerCase(), password);

        if(!didComplete){
            Toast.makeText(getApplicationContext(), "Make sure to enter all fields!", Toast.LENGTH_LONG).show();
        }
        else {
            goToMain();
        }

    }

    private Boolean validateData(){

        this.ETName.setError(null);
        this.ETEmail.setError(null);
        this.ETPassword.setError(null);
        this.ETConfirmPassword.setError(null);
        this.ETPlateNo.setError(null);
        this.ETPhone.setError(null);

        if (this.ETName.getText().toString().isEmpty()){
            this.ETName.setError("Please enter your name");
            return false;
        }

        if (this.ETEmail.getText().toString().isEmpty()){
            this.ETEmail.setError("Please enter email");
            return false;
        }
        else {
            Boolean validEmail = isEmailValid(ETEmail.getText().toString());

            if(!validEmail){
                this.ETEmail.setError("Email is not in valid format!");
            }
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

        if (this.ETPlateNo.getText().toString().isEmpty()){
            this.ETPlateNo.setError("Please provide a car plate number");
            return false;
        }

        if (this.ETPhone.getText().toString().isEmpty()){
            this.ETPhone.setError("Please provide a Phone number");
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}