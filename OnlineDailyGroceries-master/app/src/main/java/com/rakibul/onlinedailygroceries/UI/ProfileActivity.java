package com.rakibul.onlinedailygroceries.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.Signing.SignActivity;
import com.rakibul.onlinedailygroceries.Signing.SignInActivity;
import com.rakibul.onlinedailygroceries.adapter.ProfileAdapter;
import com.rakibul.onlinedailygroceries.model.PostBlog;
import com.rakibul.onlinedailygroceries.model.UserDetails;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ImageView close;
    private TextView name, email, phone;
    private RecyclerView recyclerView;
    private DatabaseReference profileRef, postRef;
    private ArrayList<PostBlog> list;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        close = findViewById(R.id.close_profile);
        back = findViewById(R.id.back);
        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userNumber);
        recyclerView = findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileRef = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().getRef().child("Hotel Blog").child("User Posts");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, SignActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                finish();
            }
        });

        profileRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails details;
                details = snapshot.getValue(UserDetails.class);

                name.setText(details.getName());
                email.setText(details.getEmail());
                phone.setText(details.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    list.add(ds.getValue(PostBlog.class));
                }

                ProfileAdapter adapter = new ProfileAdapter(list, ProfileActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
        super.onBackPressed();
    }
}
