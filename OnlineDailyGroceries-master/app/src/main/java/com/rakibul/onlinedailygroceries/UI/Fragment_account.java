package com.rakibul.onlinedailygroceries.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.rakibul.onlinedailygroceries.EmailActivity;
import com.rakibul.onlinedailygroceries.R;

public class Fragment_account extends Fragment {
    View view;


    private Toolbar toolbar;
    private TextView toolbarTitle;
    private CardView cardView1,email,phone;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbarTitle.setText("About");
        Intent myintent = new Intent(Intent.ACTION_SEND);
        cardView1 = view.findViewById(R.id.share);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01315861003"));
                startActivity(intent);
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(Intent.ACTION_SEND);
                  myintent.setType("Text/plain");
                String shareBody = "your body here";
                String shareSub = "your subject here";
                myintent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myintent, "share using"));

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EmailActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }





}
