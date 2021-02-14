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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nathan630pm.nk_final_project.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController nav;

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawerLayout.openDrawer(GravityCompat.START);
        return NavigationUI.onNavDestinationSelected(item, nav) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }
}