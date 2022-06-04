package com.voltero;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridCardListMaker extends RecyclerView.Adapter<GridCardListMaker.Viewholder> {
    private Context context;
    private ArrayList<GroceryCard> groceryCardArrayList;

    public GridCardListMaker(Context context, ArrayList<GroceryCard> groceryCardArrayList) {
        this.context = context;
        this.groceryCardArrayList = groceryCardArrayList;
    }

    @NonNull
    @Override
    public GridCardListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardListMaker.Viewholder holder, int position) {
        GroceryCard model = groceryCardArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        Picasso.get()
                .load(model.getCourse_image()) // internet path
                .placeholder(android.R.drawable.screen_background_light_transparent)
                .resize(500, 500).centerCrop()
                .into(holder.courseIV);

        holder.cardView.setOnClickListener(v -> {
            HomeShopper.grocery_name = model.getCourse_name();
            HomeShopper.grocery_image = model.getCourse_image();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();

            ContentValues params = new ContentValues();
            params.put("user_email", MainActivity.user_email);
            params.put("grc_name", HomeShopper.grocery_name);
            params.put("grc_image", HomeShopper.grocery_image);
            params.put("change_type", "add");

            Requests.request(activity, "cartItems", params, response -> {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,HomeShopper.grocery_name+" added to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    @Override
    public int getItemCount() { return groceryCardArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV;
        private CardView cardView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            cardView = itemView.findViewById(R.id.groceryCard);

        }
    }
}