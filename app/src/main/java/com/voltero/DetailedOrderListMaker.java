package com.voltero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailedOrderListMaker extends RecyclerView.Adapter<DetailedOrderListMaker.Viewholder>{
    private Context context;
    private ArrayList<CartItemCard> groceryCardArrayList;

    public DetailedOrderListMaker(Context context, ArrayList<CartItemCard> groceryCardArrayList) {
        this.context = context;
        this.groceryCardArrayList = groceryCardArrayList;
    }

    @NonNull
    @Override
    public DetailedOrderListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_order_card, parent, false);
        return new DetailedOrderListMaker.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedOrderListMaker.Viewholder holder, int position) {
        CartItemCard model = groceryCardArrayList.get(position);
        holder.courseQuantity.setText(model.getCourse_quantity());
        holder.courseNameTV.setText(model.getCourse_name());
        Picasso.get()
                .load(model.getCourse_image())
                .placeholder(android.R.drawable.screen_background_light_transparent)
                .resize(500, 500).centerCrop()
                .into(holder.courseIV);

    }

    @Override
    public int getItemCount() { return groceryCardArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV;
        private TextView courseQuantity;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseQuantity = itemView.findViewById(R.id.idCourseQuantity);
        }
    }
}
