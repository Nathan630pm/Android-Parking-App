package com.nathan630pm.nk_final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

//Created By: Nathan Kennedy, Student ID: 101333351

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController nav;

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private UserViewModel userViewModel;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        editor.clear();

        this.userViewModel = UserViewModel.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        nav = Navigation.findNavController(this, R.id.fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.profileFragment, R.id.addParkingFragment2, R.id.viewParkingFragment2).setDrawerLayout(drawerLayout).build();

        NavigationUI.setupWithNavController(navigationView, nav);
        NavigationUI.setupWithNavController(bottomNavigationView, nav);

        NavigationUI.setupActionBarWithNavController(this, nav, appBarConfiguration);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void test() {
        userViewModel.logout();
        userViewModel.getUserRepository().signInStatus.setValue("LOGOUT");
        finish();
        Intent mainIntent = new Intent(this, com.nathan630pm.nk_final_project.activities.LoginActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {

            editor = sharedPreferences.edit();
//                    editor.clear();
            editor.putString(context.getString(R.string.saved_email), "");
            editor.apply();
            editor.commit();

            userViewModel.logout();
            userViewModel.getUserRepository().signInStatus.setValue("LOGOUT");
            this.finish();
            Intent mainIntent = new Intent(this, com.nathan630pm.nk_final_project.activities.LoginActivity.class);
            startActivity(mainIntent);
        }
        else if(item.getItemId() == R.id.home){
            Log.d(TAG, "onOptionsItemSelected: back button pressed");
        }
        return NavigationUI.onNavDestinationSelected(item, nav) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }
}