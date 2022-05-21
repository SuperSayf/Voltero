package com.voltero;


import android.content.ContentValues;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        // Create the request
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/getCategories.php")).newBuilder();

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();

                    final String result = Objects.requireNonNull(response.body()).string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

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