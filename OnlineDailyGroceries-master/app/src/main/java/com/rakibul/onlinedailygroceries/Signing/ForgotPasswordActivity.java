package com.rakibul.onlinedailygroceries.Signing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private TextView tvRecovery;
    private RelativeLayout sendRecovery;
    private EditText email;
    private ProgressBar pbar;
    String EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = findViewById(R.id.back_forgot);
        email = findViewById(R.id.forgot_email);
        tvRecovery = findViewById(R.id.tv_recovery);
        sendRecovery = findViewById(R.id.send_recovery);
        pbar = findViewById(R.id.forgot_p_bar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        sendRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRecovery.setText("");
                pbar.setVisibility(View.VISIBLE);
                EMAIL = email.getText().toString();
                if (!EMAIL.isEmpty()){
                    sendRecoveryMail();
                }
                else {
                    email.setError("Enter your email");
                }
            }
        });
    }

    private void sendRecoveryMail(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");

        reference.orderByChild("email").equalTo(EMAIL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            FirebaseAuth.getInstance().sendPasswordResetEmail(EMAIL)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            pbar.setVisibility(View.GONE);
                                            sendRecovery.setBackgroundResource(R.drawable.background_green);
                                            tvRecovery.setText("Check your Email");
                                            Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        else {
                            pbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ForgotPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
