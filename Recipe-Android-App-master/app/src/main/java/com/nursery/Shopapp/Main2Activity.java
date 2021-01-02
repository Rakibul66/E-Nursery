package com.nursery.Shopapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nursery.Shopapp.Adapters.Feed;
import com.nursery.Shopapp.Adapters.FeedAdapter;
import com.nursery.Shopapp.Adapters.FeedRecyclerDecoration;


public class Main2Activity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView2 = findViewById(R.id.recyclerview2);
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
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView2.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {

            }
        });

    }
}
