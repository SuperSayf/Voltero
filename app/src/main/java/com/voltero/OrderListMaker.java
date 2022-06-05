package com.voltero;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kpstv.imageloaderview.ImageLoaderView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListMaker extends RecyclerView.Adapter<OrderListMaker.Viewholder> {
    private Context context;
    private ArrayList<GroceryCard> OrdersArrayList;

    public OrderListMaker(Context context, ArrayList<GroceryCard> OrdersArrayList) {
        this.context = context;
        this.OrdersArrayList = OrdersArrayList;
    }

    @NonNull
    @Override
    public OrderListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        return new OrderListMaker.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListMaker.Viewholder holder, int position) {
        GroceryCard model = OrdersArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());

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

        holder.cardView.setOnClickListener(v -> {
            HomeVolunteer.shopper_email =  model.getCourse_name();

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment fragment = new DetailedPopup_Fragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_container, fragment, null)
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() { return OrdersArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageLoaderView idIVCourseImagePlace;
        private ImageView courseIV;
        private TextView courseNameTV;
        private CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            cardView = itemView.findViewById(R.id.orderCard);
            idIVCourseImagePlace = itemView.findViewById(R.id.idIVCourseImagePlace);
        }
    }
}
