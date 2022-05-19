package com.voltero;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

//        courseRV = findViewById(R.id.relativeLayout2);
//
//        // here we have created new array list and added data to it.
//        cardBuilderArrayList = new ArrayList<>();
//        cardBuilderArrayList.add(new CardBuilder("DSA in Java", R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java Course",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("C++ COurse",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("DSA in C++",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Kotlin for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("HTML and CSS",  R.drawable.ic_launcher_foreground));
//
//        // we are initializing our adapter class and passing our arraylist to it.
//        CardListMaker cardListMaker = new CardListMaker(this, cardBuilderArrayList);
//
//        // below line is for setting a layout manager for our recycler view.
//        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//
//
//        // in below two lines we are setting layoutmanager and adapter to our recycler view.
//        courseRV.setLayoutManager(linearLayoutManager);
//        courseRV.setAdapter(cardListMaker);
    }
}