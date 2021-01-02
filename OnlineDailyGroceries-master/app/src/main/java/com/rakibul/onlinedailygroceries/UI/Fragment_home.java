package com.rakibul.onlinedailygroceries.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibul.onlinedailygroceries.Helper.GridSpacingItemDecoration;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.adapter.BlogAdapter;
import com.rakibul.onlinedailygroceries.model.PostBlog;

import java.util.ArrayList;

public class Fragment_home extends Fragment {
    View view;
    private TextView postBtn;
    private LinearLayout notFound;
    private Spinner spinner;
    private RelativeLayout profile;

    private RecyclerView recyclerView;
    private ArrayList<PostBlog> list;
    private DatabaseReference reference;
    private Dialog dialog;

    private String div = "Barishal";



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        gettingLayoutIDs();

//        dialog.setContentView(R.layout.progress_dialog);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
        postBtn = view.findViewById(R.id.post_btn);
        profile = view.findViewById(R.id.profile_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostProductActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
                //  overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.divisions, R.layout.spinner_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  dialog.show();
                div = spinner.getSelectedItem().toString();
                showHotels(div);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showHotels(div);
        return view;
    }


    private void showHotels(String division){
        reference.child(division).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    list.add(ds.getValue(PostBlog.class));
                }

                BlogAdapter adapter = new BlogAdapter(list, getActivity());
                if (adapter.getItemCount() == 0 || adapter.getItemCount() > 0){
                    if (adapter.getItemCount() == 0){
                        notFound.setVisibility(View.VISIBLE);
                    }
                    else {
                        notFound.setVisibility(View.GONE);
                    }
                    //dialog.dismiss();
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gettingLayoutIDs() {
        spinner = view.findViewById(R.id.division_spinner);




        notFound = view.findViewById(R.id.not_found);
       // dialog = new Dialog(getContext());

        reference = FirebaseDatabase.getInstance().getReference().child("Hotel Blog").child("Posts");
        recyclerView = view.findViewById(R.id.home_hotels);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 0, false));




    }







}
