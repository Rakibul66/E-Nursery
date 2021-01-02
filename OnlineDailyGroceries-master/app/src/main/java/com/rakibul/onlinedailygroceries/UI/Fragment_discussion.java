package com.rakibul.onlinedailygroceries.UI;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rakibul.onlinedailygroceries.MyAdapter;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.adapter.Feed;
import com.rakibul.onlinedailygroceries.adapter.FeedAdapter;
import com.rakibul.onlinedailygroceries.adapter.FeedRecyclerDecoration;
import com.rakibul.onlinedailygroceries.adapter.FoodData;

import java.util.ArrayList;
import java.util.List;

public class Fragment_discussion extends Fragment {
    View view;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView2;
    private FeedAdapter adapter;

    private FirebaseFirestore db;
    private CollectionReference post;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discussion, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbarTitle.setText("Notification");


        recyclerView2 = view.findViewById(R.id.recyclerview2);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        int bottomPadding = getResources().getDimensionPixelSize(R.dimen.bottomPadding);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        recyclerView2.addItemDecoration(new FeedRecyclerDecoration(topPadding, sidePadding, bottomPadding));

        db = FirebaseFirestore.getInstance();
        post = db.collection("Post");
        Query query = post.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Feed> options = new FirestoreRecyclerOptions.Builder<Feed>()
                .setQuery(query, Feed.class)
                .build();
        adapter = new FeedAdapter(options);

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {

            }
        });
return  view;



    }




}
