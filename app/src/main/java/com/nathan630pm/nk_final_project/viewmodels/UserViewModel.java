package com.nathan630pm.nk_final_project.viewmodels;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.repositories.UserRepository;

public class UserViewModel  extends ViewModel {
    private static final String TAG = "UserViewModel";
    private static final UserViewModel ourInstance = new UserViewModel();
    private final UserRepository userRepository = new UserRepository();


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

}
