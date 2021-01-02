package com.rakibul.onlinedailygroceries.Signing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.UI.MainActivity;

public class SignActivity extends AppCompatActivity {

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        bar = findViewById(R.id.bar);

        try {
            autoLogin();
        }catch (Exception e){}

        TextView phone, email;

        phone = findViewById(R.id.sign_in_phone);
        email = findViewById(R.id.sign_in_email);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignActivity.this, PhoneAuthActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void checkData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");

        ref.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Intent intent = new Intent(SignActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            bar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void autoLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            checkData();
        }

        else {
            FirebaseAuth.getInstance().signOut();
            bar.setVisibility(View.GONE);
        }
    }
}