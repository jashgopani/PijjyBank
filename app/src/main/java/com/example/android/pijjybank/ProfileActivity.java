package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity{


    FirebaseAuth firebaseAuth;
    private DrawerLayout mDrawerLayout;
    private TextView appbarTitle;
    String userEmail;
    TextView emailSideBar;
    NavigationView navigationView;
    View headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //setting the toolbar
       Toolbar toolbar = findViewById(R.id.profile_appbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        //Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        appbarTitle = (TextView) findViewById(R.id.appbarTitle);
        appbarTitle.setText("Profile");
        mDrawerLayout = findViewById(R.id.navd);


       navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onOptionsItemSelected(item);
                item.setChecked(true);// set item as selected to persist highlight
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        headerView = navigationView.getHeaderView(0);
        if (user != null) {
            // User is signed in
            userEmail = user.getEmail();
            emailSideBar = (TextView)headerView.findViewById(R.id.userEmail);

        }
        emailSideBar.setText(userEmail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START, true);
                return true;

            case R.id.Logout:
                logout();
                return true;

            case R.id.Timeline:
                startActivity(new Intent(ProfileActivity.this,PayrollActivity.class));
                return true;

            case R.id.Dashboard:
                return true;

            case R.id.Profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        firebaseAuth.signOut();//firebase method
        Log.i("Payroll", "Logout Failed");
        finish();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        Toast.makeText(this, "Logout Successful ", Toast.LENGTH_SHORT).show();
    }


}
