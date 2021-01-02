package com.nursery.Shopapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MoreEditpostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    EditText title, description;
    FloatingActionButton post;

    private String userID;
    private DocumentReference document_ref;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String POST_ID,TITLE,DESCRIPTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_editpost);




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

        final Intent intent = getIntent();
        POST_ID = intent.getStringExtra(MyPostActivity.EXTRA_POST_ID);
        TITLE = intent.getStringExtra(MyPostActivity.EXTRA_TITLE);
        DESCRIPTION = intent.getStringExtra(MyPostActivity.EXTRA_DESCRIPTION);

        title.setText(TITLE);
        description.setText(DESCRIPTION);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        document_ref = db.collection("Post2").document(POST_ID);

//        userID = mAuth.getUid();
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

            final ProgressDialog progressDialog = new ProgressDialog(MoreEditpostActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();

            document_ref.update("title", Title,
                    "desc", Description).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MoreEditpostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            Intent save = new Intent(MoreEditpostActivity.this, morepostActivity.class);
            startActivity(save);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_post_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_post:
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreEditpostActivity.this);
                builder.setTitle("Are you sure?")
                        .setMessage("If you delete this, this will no longer be shown in the home page!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                document_ref.delete();
                                Intent intent = new Intent(MoreEditpostActivity.this,morepostActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
