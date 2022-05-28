package com.voltero;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
                .load(model.getCourse_image())
                .placeholder(android.R.drawable.screen_background_light_transparent)
                .resize(500, 500).centerCrop()
                .into(holder.courseIV);
    }

    @Override
    public int getItemCount() { return cardBuilderArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV;
        private TextView courseQuantity;
        private CardView cardView;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseQuantity = itemView.findViewById(R.id.idCourseQuantity);
            cardView = itemView.findViewById(R.id.groceryCard);

        }
    }
}
