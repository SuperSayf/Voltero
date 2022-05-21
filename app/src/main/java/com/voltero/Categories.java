package com.voltero;


import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        courseRV = findViewById(R.id.idRVCourse);

        cardBuilderArrayList = new ArrayList<>();

        ContentValues params = new ContentValues();
//
//        // Create the request
        Requests.request(this, "getCategories", params, response -> {
            try {
                // Get the response
                JSONArray categories = new JSONArray(response);
                Log.e("test", "this shit is broken");

                for (int i = 0; i < categories.length(); ++i) {
                    JSONObject object = categories.getJSONObject(i);
                    //System.out.println(object);
                    String cat_name = object.getString("cat_name");
                    //String cat_image = object.getString("cat_image");
                    //System.out.println(cat_name);
                    cardBuilderArrayList.add(new CardBuilder(cat_name, R.drawable.ic_launcher_foreground));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

//        for (int i = 0; i < 10; ++i) {
//            cardBuilderArrayList.add(new CardBuilder("stand in val", R.drawable.ic_launcher_background));
//        }



        // here we have created new array list and added data to it.

//        cardBuilderArrayList.add(new CardBuilder("DSA in Java", R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java Course",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("C++ Course",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("DSA in C++",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Kotlin for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Random 1",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Random 2",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Random 3",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Random 4",  R.drawable.ic_launcher_foreground));

        // we are initializing our adapter class and passing our arraylist to it.
        CardListMaker cardListMaker = new CardListMaker(this, cardBuilderArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false);


        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(gridLayoutManager);
        courseRV.setAdapter(cardListMaker);
    }

    public void addNewCard(String cat_name) {
        cardBuilderArrayList.add(new CardBuilder(cat_name, R.drawable.ic_launcher_foreground));
    }


}