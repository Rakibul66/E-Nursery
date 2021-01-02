package com.rakibul.onlinedailygroceries.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.model.PostBlog;
import com.rakibul.onlinedailygroceries.model.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    ArrayList<PostBlog> list;
    Activity activity;


    public ProfileAdapter(ArrayList<PostBlog> list, Activity activity) {
        this.list = list;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        String price = list.get(position).getPrice() + "  BDT";
        holder.price.setText(price);
        Picasso.get().load(list.get(position).getUrl()).into(holder.picture);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hotel Blog");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage("The post will be deleted");
                dialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child("User Posts").child(FirebaseAuth.getInstance().getUid()).child(list.get(position).getId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ref.child("Posts").child(list.get(position).getDivision()).child(list.get(position).getId()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //deleted fuck, finish,, my god
                                                                //By: Caffeine Software
                                                                //Author: Saad Ahmed
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                );

                Dialog Dialog = dialog.create();
                Dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picture;
        TextView name, price;
        RelativeLayout delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.hotel_image);
            name = itemView.findViewById(R.id.hotel_name);
            price = itemView.findViewById(R.id.hotel_price);
            delete = itemView.findViewById(R.id.delete_hotel);
        }
    }
}