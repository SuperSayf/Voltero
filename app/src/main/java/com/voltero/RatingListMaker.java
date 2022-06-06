package com.voltero;

import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingListMaker extends RecyclerView.Adapter<RatingListMaker.Viewholder> {
    private FragmentActivity activity;
    private ArrayList<RatingCard> RatingsArrayList;

    public RatingListMaker(FragmentActivity activity, ArrayList<RatingCard> OrdersArrayList) {
        this.activity = activity;
        this.RatingsArrayList = OrdersArrayList;
    }

    @NonNull
    @Override
    public RatingListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_card_layout, parent, false);
        return new RatingListMaker.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingListMaker.Viewholder holder, int position) {
        RatingCard model = RatingsArrayList.get(position);
        //holder.rator_email.setText(model.getRator_email());
        holder.rator_name.setText(model.getRator_name());
        holder.rator_comment.setText(model.getRator_comment());
        holder.rator_rating.setRating(Float.parseFloat(model.getRator_rating()));

        ContentValues params = new ContentValues();
        params.put("user_email", model.getRator_email());

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

    }

    @Override
    public int getItemCount() { return RatingsArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private CircleImageView pfp;
        private RatingBar rator_rating;
        //private TextView rator_email;
        private TextView rator_name;
        private TextView rator_comment;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pfp = itemView.findViewById(R.id.profileImage);
            rator_rating = itemView.findViewById(R.id.ratingBar2);
            //rator_email = itemView.findViewById(R.id.ratorEmail);
            rator_name = itemView.findViewById(R.id.txtName);
            rator_comment = itemView.findViewById(R.id.txtComment);
        }
    }
}

