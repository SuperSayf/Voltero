package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Categories extends AppCompatActivity {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    public static String category_names= "";

    public JSONArray categories = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        courseRV = findViewById(R.id.idRVCourse);

        ContentValues params = new ContentValues();

        // Create the request
        Requests.request(this, "getCategories", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getString("success").equals("true")) {
                    // Show the activity_main and close this activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Show the error
                    Requests.showMessage(this, jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/sendMessage.php")).newBuilder();

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    category_names = Objects.requireNonNull(response.body()).string();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        cardBuilderArrayList = new ArrayList<>();

        System.out.println(category_names);
//        try {
//            JSONArray jsonArray = new JSONArray(category_names);
//            for (int i = 0; i < jsonArray.length(); ++i) {
//                JSONObject object = jsonArray.getJSONObject(i);
//                System.out.println(object);
//                String cat_name = object.getString("cat_name");
//                String cat_image = object.getString("cat_image");
//                cardBuilderArrayList.add(new CardBuilder(cat_name, R.drawable.ic_launcher_foreground));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // here we have created new array list and added data to it.

//        cardBuilderArrayList.add(new CardBuilder("DSA in Java", R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java Course",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("C++ COurse",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("DSA in C++",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Kotlin for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("Java for Android",  R.drawable.ic_launcher_foreground));
//        cardBuilderArrayList.add(new CardBuilder("HTML and CSS",  R.drawable.ic_launcher_foreground));

        // we are initializing our adapter class and passing our arraylist to it.
        CardListMaker cardListMaker = new CardListMaker(this, cardBuilderArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(cardListMaker);
    }

}