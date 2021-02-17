package com.nathan630pm.nk_final_project.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.repositories.UserRepository;

//Created By: Nathan Kennedy, Student ID: 101333351

public class UserViewModel  extends ViewModel {
    private static final String TAG = "UserViewModel";
    private static final UserViewModel ourInstance = new UserViewModel();
    private final UserRepository userRepository = new UserRepository();

    public MutableLiveData<User> userObject = new MutableLiveData<User>();


    public static UserViewModel getInstance() {return ourInstance;}

    private UserViewModel() {

    }

    public void logout(){
        userRepository.logout();
    }

    public void addUser(String email, String password, User user) {this.userRepository.addUser(email, password, user);}

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public boolean validateUser(String email, String password) {
        if(!email.isEmpty() || !password.isEmpty()) {
            this.userRepository.getUser(email, password);
            return true;
        }
        else {
            return false;
        }
    }

    public User getUserObject() {
        return userRepository.returnUserObject();
    }

    public void updateUser(String name, String email, String phone, String plateNo) {
        userRepository.updateUser(name, email, phone, plateNo);
    }

    public void deleteUser(String email, String password, Context context){
        userRepository.deleteUser(email, password, context);
    }

}
