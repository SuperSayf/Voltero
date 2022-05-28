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

public class LinearCardListMaker extends RecyclerView.Adapter<LinearCardListMaker.Viewholder> {
    private Context context;
    private ArrayList<CardBuilder> cardBuilderArrayList;

    public LinearCardListMaker(Context context, ArrayList<CardBuilder> cardBuilderArrayList) {
        this.context = context;
        this.cardBuilderArrayList = cardBuilderArrayList;
    }

    @NonNull
    @Override
    public LinearCardListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearCardListMaker.Viewholder holder, int position) {
        CardBuilder model = cardBuilderArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        Picasso.get()
                .load(model.getCourse_image())
                .placeholder(android.R.drawable.screen_background_light_transparent)
                .resize(500, 500).centerCrop()
                .into(holder.courseIV);

        holder.cardView.setOnClickListener(v -> {
            Shopper_Sorted_Groceries_Fragment.category_name =  model.getCourse_name();
            //TODO: Make dynamically switch based on current activity.
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment fragment = new Shopper_Sorted_Groceries_Fragment();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_container, fragment, null)
                    .addToBackStack(null)
                    .commit();

        });

    }

    @Override
    public int getItemCount() { return cardBuilderArrayList.size(); }

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
