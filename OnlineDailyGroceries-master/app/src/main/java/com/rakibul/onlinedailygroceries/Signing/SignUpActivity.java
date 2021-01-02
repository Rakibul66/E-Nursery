package com.rakibul.onlinedailygroceries.Signing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.UI.MainActivity;
import com.rakibul.onlinedailygroceries.model.UserDetails;

public class SignUpActivity extends AppCompatActivity {

    private TextView alreadyHaveAnAccount, sign_up_tv;
    private RelativeLayout signUp, phone_layout;
    private EditText name, email, phone, password, confirm;
    private ImageView back;
    private ProgressBar progressBar;
    private String NAME, EMAIL, PHONE, PASSWORD, CONFIRM;
    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        gettingLayoutIDs();
        clickListeners();
    }

    private void clickListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count){
                    case 0:
                        initialize();
                        if (validate()){
                            sign_up_tv.setText("");
                            progressBar.setVisibility(View.VISIBLE);

                            signUpUser();
                        }

                        break;

                    case 1:
                        initialize();
                        if (validate()){
                            sign_up_tv.setText("");
                            progressBar.setVisibility(View.VISIBLE);
                            loginUserWithEmailAndPassword();
                        }

                        break;
                }
            }
        });
    }

    private void loginUserWithEmailAndPassword() {
        firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified();
                        }
                    }
                });
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            progressBar.setVisibility(View.GONE);
            String next = "Continue";
            sign_up_tv.setText(next);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(SignUpActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendDataToFirebase(){
        UserDetails user = new UserDetails();
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPhone(PHONE);
        user.setPassword(PASSWORD);
        user.setUid(FirebaseAuth.getInstance().getUid());

        reference.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    count++;
                    signUp.setBackgroundResource(R.drawable.background_green);
                    String next = "Continue";
                    sign_up_tv.setText(next);
                }

                else {
                    progressBar.setVisibility(View.GONE);
                    String next = "Continue";
                    sign_up_tv.setText(next);
                    Toast.makeText(SignUpActivity.this, "Verification message sent to your Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationMail(){
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //sendDataToFirebase
                            sendDataToFirebase();
                            FirebaseAuth.getInstance().signOut();
                        }

                        else {
                            progressBar.setVisibility(View.GONE);
                            String next = "Sign Up";
                            sign_up_tv.setText(next);
                            Toast.makeText(SignUpActivity.this, "Verification message not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUpUser(){
        firebaseAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //sendVerification
                            sendVerificationMail();
                        }

                        else {
                            progressBar.setVisibility(View.GONE);
                            String next = "Sign Up";
                            sign_up_tv.setText(next);
                            Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validate(){
        boolean valid = true;

        if (NAME.isEmpty()){
            name.setError("Enter your Fullname");
            name.requestFocus();
            valid = false;
        }

        else if (NAME.length() < 3 || NAME.length() > 24){
            name.setError("Name cannot be too long or too short");
            name.requestFocus();
            valid = false;
        }

        else if (EMAIL.isEmpty()){
            email.setError("Enter your Email address");
            email.requestFocus();
            valid = false;
        }

        else if (!EMAIL.matches(EMAIL_PATTERN)){
            email.setError("Invalid Email address");
            email.requestFocus();
            valid = false;
        }

        else if (PHONE.isEmpty()){
            phone.setError("Enter your mobile number");
            phone.requestFocus();
            valid = false;
        }

        else if (PHONE.length() < 11){
            phone.setError("Invalid mobile number");
            phone.requestFocus();
            valid = false;
        }

        else if (PASSWORD.isEmpty()){
            password.setError("Password cannot be empty");
            password.requestFocus();
            valid = false;
        }

        else if (PASSWORD.length() < 6 || PASSWORD.length() > 20){
            password.setError("Password should be 6 to 20 characters");
            password.requestFocus();
            valid = false;
        }

        else if (CONFIRM.isEmpty()){
            confirm.setError("Confirm your Password");
            confirm.requestFocus();
            valid = false;
        }

        else if (!CONFIRM.equals(PASSWORD)){
            confirm.setError("Password didn't match");
            confirm.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void initialize(){
        NAME = name.getText().toString();
        EMAIL = email.getText().toString();
        PHONE = "+88" + phone.getText().toString();
        PASSWORD = password.getText().toString();
        CONFIRM = confirm.getText().toString();
    }

    private void gettingLayoutIDs(){
        alreadyHaveAnAccount = findViewById(R.id.already_have_an_account);
        sign_up_tv = findViewById(R.id.tv_sign_up);
        signUp = findViewById(R.id.sign_up_for_account);
        phone_layout = findViewById(R.id.phone_layout);
        progressBar = findViewById(R.id.sign_up_progress_bar);
        back = findViewById(R.id.back_sign_up);
        name = findViewById(R.id.sign_up_name);
        email = findViewById(R.id.sign_up_email);
        phone = findViewById(R.id.sign_up_phone);
        password = findViewById(R.id.sign_up_password);
        confirm = findViewById(R.id.sign_up_confirm);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Users");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
