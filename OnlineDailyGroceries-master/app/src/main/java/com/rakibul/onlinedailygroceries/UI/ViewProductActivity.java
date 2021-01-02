package com.rakibul.onlinedailygroceries.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.model.PostBlog;
import com.squareup.picasso.Picasso;

public class ViewProductActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private ImageView back, picture;
    private TextView area;
    private TextView district;
    private TextView div;
    private TextView name;
    private TextView price;
    private String contact;
    private TextView description;
     RelativeLayout order;
    private String id, division,areaN;
    private DatabaseReference reference;
    TextView callx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        back = findViewById(R.id.close_hotel);
        reference = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Posts");
        picture = findViewById(R.id.hotel_image);
        area = findViewById(R.id.area);
        district = findViewById(R.id.district);
        div = findViewById(R.id.division);
        name = findViewById(R.id.hotel_name);
        price = findViewById(R.id.hotel_price);
        callx = findViewById(R.id.hotel_contact);
        description = findViewById(R.id.hotel_description);





        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        division = intent.getStringExtra("division");
        callx.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override
            public void onClick(View v) {
                makePhoneCall();

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reference.child(division).child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostBlog hotel = new PostBlog();
                        hotel = snapshot.getValue(PostBlog.class);

                        try {
                            Picasso.get().load(hotel.getUrl()).into(picture);
                            name.setText(hotel.getName());
                            area.setText(hotel.getArea());
                            district.setText(hotel.getDistrict());
                            div.setText(hotel.getDivision());
                            contact = hotel.getContact();
                            callx.setText(hotel.getContact());
                            price.setText(hotel.getPrice() + "  ৳");
                            description.setText(hotel.getDescription());
                        }catch (Exception e){}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        }, 500);
    }
    //phone call
    private void makePhoneCall() {


            if (ContextCompat.checkSelfPermission(ViewProductActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ViewProductActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + contact;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //

    @Override
    public void onBackPressed() {
        finish();
    }

}