package com.voltero;

import android.content.Context;
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

import java.util.ArrayList;

public class CategoriesCardListMaker extends RecyclerView.Adapter<CategoriesCardListMaker.Viewholder> {
    private Context context;
    private ArrayList<GroceryCard> groceryCardArrayList;

    public CategoriesCardListMaker(Context context, ArrayList<GroceryCard> groceryCardArrayList) {
        this.context = context;
        this.groceryCardArrayList = groceryCardArrayList;
    }

    @NonNull
    @Override
    public CategoriesCardListMaker.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesCardListMaker.Viewholder holder, int position) {
        GroceryCard model = groceryCardArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
//        Picasso.get()
//                .load(model.getCourse_image())
//                .placeholder(android.R.drawable.screen_background_light_transparent)
//                .resize(500, 500).centerCrop()
//                .into(holder.courseIV);
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
            Shopper_Sorted_Groceries_Fragment.category_name =  model.getCourse_name();

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
    public int getItemCount() { return groceryCardArrayList.size(); }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageLoaderView idIVCourseImagePlace;
        private ImageView courseIV;
        private TextView courseNameTV;
        private CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            cardView = itemView.findViewById(R.id.groceryCard);
            idIVCourseImagePlace = itemView.findViewById(R.id.idIVCourseImagePlace);
        }
    }
}
