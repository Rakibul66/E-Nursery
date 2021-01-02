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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.UI.MainActivity;

public class SignInActivity extends AppCompatActivity {

    private TextView forgot, continue_tv;
    private RelativeLayout signIn, signUp;
    private ProgressBar progressBar;
    private EditText email, password;
    private String EMAIL, PASSWORD;
    private FirebaseAuth firebaseAuth;
    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gettingLayoutIDs();
        clickListeners();
    }

    private void loginUserWithEmailAndPassword() {
        firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified();
                        }

                        else {
                            progressBar.setVisibility(View.GONE);
                            String next = "Continue";
                            continue_tv.setText(next);
                            Toast.makeText(SignInActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            progressBar.setVisibility(View.GONE);
            String next = "Continue";
            continue_tv.setText(next);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(SignInActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validate(){
        boolean valid = true;

        if (EMAIL.isEmpty()){
            email.setError("Enter your Email address");
            email.requestFocus();
            valid = false;
        }

        else if (!EMAIL.matches(EMAIL_PATTERN)){
            email.setError("Invalid Email address");
            email.requestFocus();
            valid = false;
        }

        if (PASSWORD.isEmpty()){
            password.setError("Password cannot be empty");
            password.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void clickListeners(){
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMAIL = email.getText().toString().trim();
                PASSWORD = password.getText().toString();

                if (validate()){
                    progressBar.setVisibility(View.VISIBLE);
                    continue_tv.setText("");

                    loginUserWithEmailAndPassword();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void gettingLayoutIDs(){
        forgot = findViewById(R.id.forgot_password);
        continue_tv = findViewById(R.id.tv_sign_in);
        signUp = findViewById(R.id.sign_up_button);
        signIn = findViewById(R.id.sign_in_to_account);
        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);
        progressBar = findViewById(R.id.sign_in_progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}
