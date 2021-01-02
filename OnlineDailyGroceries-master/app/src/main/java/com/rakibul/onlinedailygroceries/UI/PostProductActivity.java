package com.rakibul.onlinedailygroceries.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.model.PostBlog;

import java.util.Random;

public class PostProductActivity extends AppCompatActivity {

    private EditText name, district, area, price, description, contact;
    private TextView chooseImage, postTv;
    private RelativeLayout postBtn;
    private ProgressBar progressBar;
    private Spinner spinner;
    private ImageView selected_image, close;
    private Uri uri;

    private String NAME, DISTRICT, AREA, PRICE, DESC, DIVISION, URL, CONTACT;
    private String randomNumber = "";
    String UID;
    int count = 0;

    private DatabaseReference reference;
    private DatabaseReference userRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);

        gettingLayoutIDs();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.divisions, R.layout.spinner_item_white);
        spinner.setAdapter(adapter);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                finish();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0){
                    initialize();
                    if (validate()){
                        postTv.setText("");
                        progressBar.setVisibility(View.VISIBLE);
                        uploadImageToFirebase();
                    }
                }
            }
        });
    }

    /*--------------------------------------------------------SEND_DATA_TO_FIREBASE--------------------------------------------------*/

    private void sendDataToFirebase(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();


        for (int i=0; i<30; i++){
            char c = alphabet.charAt(random.nextInt(alphabet.length()));
            String some = Character.toString(c);
            randomNumber = some + randomNumber;
        }

        UID = FirebaseAuth.getInstance().getUid();
        final PostBlog post = new PostBlog();

        post.setId(randomNumber);
        post.setUid(UID);
        post.setName(NAME);
        post.setDivision(DIVISION);
        post.setDistrict(DISTRICT);
        post.setArea(AREA);
        post.setContact(CONTACT);
        post.setUrl(URL);
        post.setPrice(PRICE);
        post.setDescription(DESC);

        reference.child(DIVISION).child(randomNumber).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    userRef.child(UID).child(randomNumber).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                String success = "Post Successful";
                                postTv.setText(success);
                                postBtn.setBackgroundResource(R.drawable.background_green);
                                count++;

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(PostProductActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                    }
                                },1000);
                            }

                            else {
                                progressBar.setVisibility(View.GONE);
                                String success = "Upload Failed";
                                postTv.setText(success);
                            }
                        }
                    });
                }

                else {
                    progressBar.setVisibility(View.GONE);
                    String success = "Upload Failed";
                    postTv.setText(success);
                }
            }
        });
    }

    /*--------------------------------------------------------SEND_DATA_TO_FIREBASE--------------------------------------------------*/



    /*--------------------------------------------------------UPLOAD_IMAGE_TO_FIREBASE--------------------------------------------------*/

    /**
     * Making MIME Type Map
     * Assigning URI value into String
     */

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImageToFirebase(){
        final StorageReference imageone = mStorageRef.child(System.currentTimeMillis() + "." + getExtension(uri));

        imageone.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        imageone.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                URL = uri.toString();
                                sendDataToFirebase();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        String success = "Upload Failed";
                        postTv.setText(success);
                    }
                });
    }

    /*--------------------------------------------------------UPLOAD_IMAGE_TO_FIREBASE--------------------------------------------------*/



    /*-------------------------------------------------------------SELECT_IMAGES-------------------------------------------------------*/

    /**
     * Picking Images
     * Creating URI
     */

    private void chooseImage(){
        Intent intent = new Intent();
        Toast.makeText(PostProductActivity.this, "Select low quality image", Toast.LENGTH_SHORT).show();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            selected_image.setImageURI(uri);
            String status = "Picture Selected";
            chooseImage.setText(status);
            chooseImage.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    /*-------------------------------------------------------------SELECT_IMAGES-------------------------------------------------------*/

    private boolean validate(){
        boolean valid = true;

        if (NAME.isEmpty()){
            name.setError("Field cannot be empty");
            name.requestFocus();
            valid = false;
        }

        else if (DISTRICT.isEmpty()){
            district.setError("Field cannot be empty");
            district.requestFocus();
            valid = false;
        }

        else if (AREA.isEmpty()){
            area.setError("Field cannot be empty");
            area.requestFocus();
            valid = false;
        }

        else if (CONTACT.isEmpty()){
            contact.setError("Field cannot be empty");
            contact.requestFocus();
            valid = false;
        }

        else if (PRICE.isEmpty()){
            price.setError("Field cannot be empty");
            price.requestFocus();
            valid = false;
        }

        else if (uri == null){
            Toast.makeText(PostProductActivity.this, "Picture isn't selected", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (DESC.isEmpty()){
            description.setError("Field cannot be empty");
            description.requestFocus();
            valid = false;
        }

        return valid;
    }

    private void initialize(){
        NAME = name.getText().toString();
        DISTRICT = district.getText().toString();
        AREA = area.getText().toString();
        CONTACT = contact.getText().toString();
        PRICE = price.getText().toString();
        DESC = description.getText().toString();
        DIVISION = spinner.getSelectedItem().toString();
    }

    private void gettingLayoutIDs(){
        spinner = findViewById(R.id.post_division_spinner);
        name = findViewById(R.id.hotel_name);
        district = findViewById(R.id.district);
        area = findViewById(R.id.local_area);
        contact = findViewById(R.id.contact);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        chooseImage = findViewById(R.id.choose_image);
        postTv = findViewById(R.id.tv_post_hotel);
        postBtn = findViewById(R.id.post_hotel_btn);
        progressBar = findViewById(R.id.post_hotel_progress_bar);
        selected_image = findViewById(R.id.selected_image);
        close = findViewById(R.id.back_post_hotel);

        reference = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Posts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("User Posts");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
        finish();
    }
}
