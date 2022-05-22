package com.voltero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridCardListMaker extends RecyclerView.Adapter<GridCardListMaker.Viewholder> {
    private Context context;
    private ArrayList<CardBuilder> cardBuilderArrayList;

    public GridCardListMaker(Context context, ArrayList<CardBuilder> cardBuilderArrayList) {
        this.context = context;
        this.cardBuilderArrayList = cardBuilderArrayList;
    }

    @NonNull
    @Override
    public GridCardListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardListMaker.Viewholder holder, int position) {
        CardBuilder model = cardBuilderArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        Picasso.get()
                .load(model.getCourse_image()) // internet path
                .placeholder(android.R.drawable.screen_background_light_transparent)
                .resize(500, 500).centerCrop()
                .into(holder.courseIV);
    }

    @Override
    public int getItemCount() { return cardBuilderArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
        }
    }
}