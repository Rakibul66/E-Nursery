package com.nursery.Shopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    ProgressDialog progressDialog;
    MyAdapter myAdapter;
    EditText txt_Search;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Navigation drawer layout

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.navigation_view_left);

        NavigationItems();


        //........................

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        txt_Search = (EditText)findViewById(R.id.txt_searchtext);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items ....");


        myFoodList = new ArrayList<>();

        myAdapter  = new MyAdapter(MainActivity.this,myFoodList);
        mRecyclerView.setAdapter(myAdapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFoodList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());
                    myFoodList.add(foodData);

                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });



    }
    private void filter(String text) {

        ArrayList<FoodData> filterList = new ArrayList<>();

        for(FoodData item: myFoodList){

            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(item);

            }

        }

        myAdapter.filteredList(filterList);

    }
    public void btn_uploadActivity(View view) {
        startActivity(new Intent(this, Upload_Recipe.class));
    }

        //
        public void NavigationItems() {

            navigationView.setCheckedItem(R.id.feed);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.feed:

                            break;
                        case R.id.post:
                            Intent post = new Intent(MainActivity.this, MoreActivity.class);
                            startActivity(post);
                            break;

                        case R.id.my_post:
                            Intent morepost = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(morepost);
                            break;

//                        case R.id.logout:
//                            FirebaseAuth.getInstance().signOut();
//                            Intent signOut = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(signOut);
//                            finish();
//                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        }

        @Override
        protected void onStart() {
            super.onStart();
            navigationView.setCheckedItem(R.id.feed);
    }
}
