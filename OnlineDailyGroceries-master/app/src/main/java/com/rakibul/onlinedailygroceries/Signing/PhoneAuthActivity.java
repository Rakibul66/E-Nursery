package com.rakibul.onlinedailygroceries.Signing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.UI.MainActivity;
import com.rakibul.onlinedailygroceries.model.UserDetails;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText phone, code, name;
    private TextView verify_txt;
    private String CODE, PHONE, NAME, verification_id;
    private RelativeLayout sign_in_layout, code_layout, name_layout;
    private ProgressBar bar;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        gettingLayoutIDs();

        sign_in_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
                switch (count){
                    case 1:
                        if (validate()){
                            count ++;
                            bar.setVisibility(View.VISIBLE);
                            verify_txt.setText("");
                            sendVerificationCode(PHONE);
                        }
                        break;

                    case 2:
                        if (!CODE.isEmpty()){
                            verify_txt.setText("");
                            bar.setVisibility(View.VISIBLE);
                            verifyCode(CODE);
                        }
                        else {
                            code.setError("Enter the verification code");
                            code.requestFocus();
                        }
                        break;
                    case 3:
                        if (NAME.isEmpty()){
                            name.setError("Name is required");
                            name.requestFocus();
                        }
                        else {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");
                            UserDetails user = new UserDetails();
                            user.setEmail("");
                            user.setName(NAME);
                            user.setPhone(PHONE);
                            user.setUid(FirebaseAuth.getInstance().getUid());
                            user.setPassword("");
                            ref.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                }
            }
        });
    }

    private void checkData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");
        ref.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.child("uid").getValue(String.class);
                if (uid == null){
                    name_layout.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    verify_txt.setText("Submit");
                    count ++;
                }
                else {
                    Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            checkData();
                        }
                        else {
                            Toast.makeText(PhoneAuthActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verification_id = s;
            bar.setVisibility(View.GONE);
            code_layout.setVisibility(View.VISIBLE);
            verify_txt.setText("Verify Code");
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signInWithCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            phone.setError("Invalid Number or any other error occured");
            phone.requestFocus();
            bar.setVisibility(View.GONE);
            verify_txt.setText("Send verification code");
            count--;
        }
    };

    private boolean validate(){
        boolean valid = true;

        if (PHONE.isEmpty()){
            phone.setError("Phone number required");
            phone.requestFocus();
            valid = false;
        }
        else if (PHONE.length() < 11){
            phone.setError("Invalid Number");
            phone.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void initialize(){
        CODE = code.getText().toString();
        PHONE = "+88" + phone.getText().toString();
        NAME = name.getText().toString();
    }

    private void gettingLayoutIDs(){
        phone = findViewById(R.id.sign_in_phone);
        code = findViewById(R.id.code);
        name = findViewById(R.id.name);
        name_layout = findViewById(R.id.name_layout);
        verify_txt = findViewById(R.id.phone_auth);
        sign_in_layout = findViewById(R.id.sign_in_phone_layout);
        code_layout = findViewById(R.id.code_layout);
        bar = findViewById(R.id.progress_bar);
    }
}