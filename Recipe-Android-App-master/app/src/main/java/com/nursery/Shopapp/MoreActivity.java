package com.nursery.Shopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoreActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;

    EditText title, description;
    FloatingActionButton post;

    private String userID;
    private DocumentReference document_ref;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @ServerTimestamp
    Date time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        post = findViewById(R.id.post);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

//        userID = mAuth.getUid();

        document_ref = db.collection("Post").document();
    }

    private void saveData() {
        final String Title = title.getText().toString();
        final String Description = description.getText().toString();

        if (Title.isEmpty()) {
            title.setError("All field must be filled");
            return;
        }
        if (Description.isEmpty()) {
            description.setError("All field must be filled");
            return;
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(MoreActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();


            String post_id2 = document_ref.getId();
            Map<String, Object> userMap = new HashMap<>();

            userMap.put("title", Title);
            userMap.put("desc", Description);
            userMap.put("post_id", post_id2);
            userMap.put("timestamp", FieldValue.serverTimestamp());

            document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.cancel();

                    Intent save = new Intent(MoreActivity.this, Main2Activity.class);
                    startActivity(save);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MoreActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
