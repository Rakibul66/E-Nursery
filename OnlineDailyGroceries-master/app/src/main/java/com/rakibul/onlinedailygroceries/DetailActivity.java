package com.rakibul.onlinedailygroceries;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rakibul.onlinedailygroceries.UI.ViewProductActivity;

public class DetailActivity extends AppCompatActivity {


    TextView foodDescription,RecipeName,RecipePrice;
    ImageView foodImage;
    String key="";
    String imageUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RecipeName = (TextView) findViewById(R.id.txtRecipeName);
        RecipePrice = (TextView) findViewById(R.id.txtPrice);
        foodDescription = (TextView) findViewById(R.id.txtDescription);
        foodImage = (ImageView) findViewById(R.id.ivImage2);



        Bundle mBundle = getIntent().getExtras();

        if (mBundle != null) {

            foodDescription.setText(mBundle.getString("Description"));
            key = mBundle.getString("keyValue");
            imageUrl = mBundle.getString("Image");

            RecipeName.setText(mBundle.getString("RecipeName"));
            RecipePrice.setText(mBundle.getString("price"));
            // foodImage.setImageResource(mBundle.getInt("Image"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(foodImage);


        }

    }











    }

