package com.voltero;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeShopper extends AppCompatActivity {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shopper);
        courseRV = findViewById(R.id.idRVCourse);

        // here we have created new array list and added data to it.
        cardBuilderArrayList = new ArrayList<>();
        cardBuilderArrayList.add(new CardBuilder("DSA in Java", R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Java Course",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("C++ Course",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("DSA in C++",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Kotlin for Android",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Java for Android",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Random 1",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Random 2",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Random 3",  R.drawable.ic_launcher_foreground));
        cardBuilderArrayList.add(new CardBuilder("Random 4",  R.drawable.ic_launcher_foreground));

        // we are initializing our adapter class and passing our arraylist to it.
        CardListMaker cardListMaker = new CardListMaker(this, cardBuilderArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false);


        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(gridLayoutManager);
        courseRV.setAdapter(cardListMaker);
    }

    public void goToChat(View v) {
        // Intent to the ChatActivity
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
    }
}