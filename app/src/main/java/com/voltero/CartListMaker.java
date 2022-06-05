package com.voltero;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kpstv.imageloaderview.ImageLoaderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartListMaker extends RecyclerView.Adapter<CartListMaker.Viewholder> {
    private Context context;
    private ArrayList<CartItemCard> cardBuilderArrayList;

    public CartListMaker(Context context, ArrayList<CartItemCard> CartItemCardArrayList) {
        this.context = context;
        this.cardBuilderArrayList = CartItemCardArrayList;
    }

    @NonNull
    @Override
    public CartListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListMaker.Viewholder holder, int position) {
        CartItemCard model = cardBuilderArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        holder.courseQuantity.setText(model.getCourse_quantity());

        Picasso.get()
                .load(model.getCourse_image()) // internet path
                .placeholder(R.drawable.progress_animation)
                .resize(500, 500).centerCrop()
                .priority(Picasso.Priority.HIGH)
                .into(holder.courseIV, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ImageLoaderView imageLoaderView = (ImageLoaderView) holder.idIVCourseImagePlace;
                        // Make it invisible
                        imageLoaderView.setVisibility(View.INVISIBLE);

                        // Make the picture visible
                        holder.courseIV.setVisibility(View.VISIBLE);
                        // Make the text visible
                        holder.courseNameTV.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, "One or more images failed to load", Toast.LENGTH_SHORT).show();
                    }
                });

        HomeShopper.grocery_name = model.getCourse_name();
        HomeShopper.grocery_image = model.getCourse_image();

        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);
        params.put("grc_name", HomeShopper.grocery_name);
        params.put("grc_image", HomeShopper.grocery_image);

        holder.increase.setOnClickListener(v -> {

            AppCompatActivity activity = (AppCompatActivity) v.getContext();

            params.put("change_type", "add");

            Requests.request(activity, "cartItems", params, response -> {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        Button Pasta = activity.findViewById(R.id.Pasta);
                        Pasta.performClick();
                    }
                });
            });
        });

        holder.decrease.setOnClickListener(v -> {

            AppCompatActivity activity = (AppCompatActivity) v.getContext();

            params.put("change_type", "decrease");

            Requests.request(activity, "cartItems", params, response -> {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        Button Pasta = activity.findViewById(R.id.Pasta);
                        Pasta.performClick();
                    }
                });
            });
        });

        holder.remove.setOnClickListener(v -> {

            AppCompatActivity activity = (AppCompatActivity) v.getContext();

            params.put("change_type", "removeAll");

            Requests.request(activity, "cartItems", params, response -> {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        Button Pasta = activity.findViewById(R.id.Pasta);
                        Pasta.performClick();
                    }
                });
            });
        });
    }

    @Override
    public int getItemCount() { return cardBuilderArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageLoaderView idIVCourseImagePlace;
        private ImageView courseIV;
        private TextView courseNameTV;
        private TextView courseQuantity;
        private CardView cardView;
        private ImageButton increase;
        private ImageButton decrease;
        private ImageButton remove;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.txtComment);
            courseQuantity = itemView.findViewById(R.id.idCourseQuantity);
            cardView = itemView.findViewById(R.id.groceryCard);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            remove = itemView.findViewById(R.id.remove);
            idIVCourseImagePlace = itemView.findViewById(R.id.idIVCourseImagePlace);
        }
    }
}
