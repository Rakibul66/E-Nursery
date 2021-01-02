package com.rakibul.onlinedailygroceries.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rakibul.onlinedailygroceries.R;
import com.rakibul.onlinedailygroceries.UI.ViewProductActivity;
import com.rakibul.onlinedailygroceries.model.PostBlog;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{

    ArrayList<PostBlog> list;
    Activity activity;

    public BlogAdapter(ArrayList<PostBlog> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        String price = list.get(position).getPrice() + "  ৳";
        holder.price.setText(price);
        Picasso.get().load(list.get(position).getUrl()).into(holder.picture);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewProductActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("division", list.get(position).getDivision());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.hotel_image);
            name = itemView.findViewById(R.id.hotel_name);
            price = itemView.findViewById(R.id.hotel_price);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
