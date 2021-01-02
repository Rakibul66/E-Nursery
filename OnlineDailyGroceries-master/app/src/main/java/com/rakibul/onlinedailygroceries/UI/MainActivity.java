package com.rakibul.onlinedailygroceries.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.rakibul.onlinedailygroceries.R;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.nav_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_home()).commit();
        bottomMenu();
    }


    private void bottomMenu() {


        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.nav_home:
                        fragment = new Fragment_home();
                        break;
                    case R.id.nav_favorites:
                        fragment = new Fragment_home2();
                        break;
                    case R.id.nav_dis:
                        fragment = new Fragment_discussion();
                        break;
                    case R.id.nav_search:
                        fragment = new Fragment_account();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
        //
        // CALL getInternetStatus() function to check for internet and display error dialog
        if (new InternetDialog(this).getInternetStatus()) {
            Toast.makeText(this, "INTERNET VALIDATION PASSED", Toast.LENGTH_SHORT).show();
            //
        }

    }}