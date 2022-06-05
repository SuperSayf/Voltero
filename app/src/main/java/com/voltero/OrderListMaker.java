package com.voltero;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kpstv.imageloaderview.ImageLoaderView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderListMaker extends RecyclerView.Adapter<OrderListMaker.Viewholder> {
    private FragmentActivity activity;
    private ArrayList<OrderCard> OrdersArrayList;

    public OrderListMaker(FragmentActivity activity, ArrayList<OrderCard> OrdersArrayList) {
        this.activity = activity;
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
        OrderCard model = OrdersArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        holder.shopperName.setText(model.getShopper_name());
        holder.shopperAddress.setText(model.getShopper_address());

        ContentValues params = new ContentValues();
        params.put("user_email", model.getCourse_name());

        Requests.request(activity, "getImage", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("success").equals("true")) {
                    String encodedImage = jsonObject.getString("user_image");
                    // Convert encoded image to bitmap
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    // convert byte array back to original image
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.pfp.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                            }
                        });
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

//        Picasso.get()
//                .load(model.getCourse_image()) // internet path
//                .placeholder(R.drawable.progress_animation)
//                .resize(500, 500).centerCrop()
//                .priority(Picasso.Priority.HIGH)
//                .into(holder.courseIV, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        ImageLoaderView imageLoaderView = (ImageLoaderView) holder.idIVCourseImagePlace;
//                        // Make it invisible
//                        imageLoaderView.setVisibility(View.INVISIBLE);
//
//                        // Make the picture visible
//                        holder.courseIV.setVisibility(View.VISIBLE);
//                        // Make the text visible
//                        holder.courseNameTV.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Toast.makeText(context, "One or more images failed to load", Toast.LENGTH_SHORT).show();
//                    }
//                });

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
        //private ImageLoaderView idIVCourseImagePlace;
        private CircleImageView pfp;
        private TextView courseNameTV;
        private TextView shopperName;
        private TextView shopperAddress;
        private CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pfp = itemView.findViewById(R.id.courseIV);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            cardView = itemView.findViewById(R.id.orderCard);
            //idIVCourseImagePlace = itemView.findViewById(R.id.idIVCourseImagePlace);
            shopperName = itemView.findViewById(R.id.txtName);
            shopperAddress = itemView.findViewById(R.id.txtLocation);
        }
    }
}
