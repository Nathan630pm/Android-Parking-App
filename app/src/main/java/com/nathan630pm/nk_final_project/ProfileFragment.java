package com.nathan630pm.nk_final_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathan630pm.nk_final_project.activities.MainActivity;
import com.nathan630pm.nk_final_project.activities.RegisterActivity;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.repositories.UserRepository;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.util.ArrayList;

//Created By: Nathan Kennedy, Student ID: 101333351


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private View v;

    private UserViewModel userViewModel;


    private EditText ETName;
    private EditText ETEmail;
    private EditText ETPlateNo;
    private EditText ETPhone;
    private User userObject;
    private ProgressBar progressBar;
    private Button BEditProfile;
    private Button BDeleteProfile;

    private LayoutInflater alertInflater;
    private View dialogView;
    private Context context;

    private AlertDialog addDialog;



    private Boolean editToggle = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userViewModel = UserViewModel.getInstance();

        this.userViewModel.getUserRepository().deleteProfileComplete.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    ((MainActivity)getActivity()).test();
                }
            }
        });


        this.userViewModel.getUserRepository().userObject.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                 userObject = user;

                ETName.setText(userObject.getName());
                ETEmail.setText(userObject.getEmail());
                ETPlateNo.setText(userObject.getCarPlateNumber());
                ETPhone.setText(userObject.getContactNumber());

                alertInflater = getLayoutInflater();
                dialogView = alertInflater.inflate(R.layout.delete_user_dialog, null);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        v = inflater.inflate(R.layout.fragment_profile, container, false);



        this. ETName = v.findViewById(R.id.ETName);
        this. ETEmail = v.findViewById(R.id.ETEmail);
        this. ETPlateNo = v.findViewById(R.id.ETPlateNo);
        this. ETPhone = v.findViewById(R.id.ETPhone);
        this.progressBar = v.findViewById(R.id.progressBar);
        this.BEditProfile = v.findViewById(R.id.BEditProfile);
        this.BDeleteProfile = v.findViewById(R.id.BDeleteProfile);
        BEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        BDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        getUserData();

        context = v.getContext();




        return v;
    }

    private void showDialog() {

//        if(addDialog != null){
//            ((ViewGroup) addDialog.getCurrentFocus()).removeView(addDialog.getCurrentFocus());
//
//        }




        addDialog = new AlertDialog.Builder(context)
                .setTitle("Are You Sure?")
                .setMessage("Please Confirm your password:")
                .setView(dialogView)
                .setPositiveButton("DELETE PROFILE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //read the given task info
                        EditText ETConfirmPassword = dialogView.findViewById(R.id.ETConfirmPassword);
                        Log.d(TAG, "New Task Info: " + ETConfirmPassword.getText().toString());

                        deleteUser(ETConfirmPassword.getText().toString());



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();

        addDialog.show();

    }

    public void getUserData(){


    }


    private void deleteUser(String password) {
        if(password.equals("") || password == null){

        }else {
            userViewModel.deleteUser(userObject.getEmail(), password, this.getContext());
        }

    }



    public void editProfile() {
        if(editToggle){
            editToggle = false;

            BDeleteProfile.setVisibility(View.GONE);

            ETName.setEnabled(false);
            ETEmail.setEnabled(false);
            ETPhone.setEnabled(false);
            ETPlateNo.setEnabled(false);

            BEditProfile.setText("EDIT PROFILE");

            userViewModel.updateUser(ETName.getText().toString(), ETEmail.getText().toString(), ETPhone.getText().toString(), ETPlateNo.getText().toString());

        }else {
            editToggle = true;

            userViewModel.getUserRepository().makeSureInfoUpToDate();


            BDeleteProfile.setVisibility(View.VISIBLE);

            ETName.setEnabled(true);
            ETEmail.setEnabled(true);
            ETPhone.setEnabled(true);
            ETPlateNo.setEnabled(true);

            BEditProfile.setText("SAVE PROFILE");
        }
    }

}