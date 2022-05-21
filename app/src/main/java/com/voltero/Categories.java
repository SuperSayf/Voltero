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

        addNewCard();
    }

    public void addNewCard() {
        ContentValues params = new ContentValues();

        Requests.request(this, "getCategories", params, response -> {
            try {
                // Get the response
                JSONArray categories = new JSONArray(response);
                courseRV = findViewById(R.id.idRVCourse);
                cardBuilderArrayList = new ArrayList<>();
                for (int i = 0; i < categories.length(); ++i) {
                    JSONObject object = categories.getJSONObject(i);
                    //System.out.println(object);
                    String cat_name = object.getString("cat_name");
                    //String cat_image = object.getString("cat_image");
                    System.out.println(cat_name);
                    cardBuilderArrayList.add(new CardBuilder(cat_name, R.drawable.ic_launcher_foreground));
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        CardListMaker cardListMaker = new CardListMaker(Categories.this, cardBuilderArrayList);
                        courseRV.setLayoutManager(new GridLayoutManager(Categories.this, 2));
                        courseRV.setAdapter(cardListMaker);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }


}