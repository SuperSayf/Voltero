package com.voltero;


import android.content.ContentValues;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeShopper extends AppCompatActivity {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shopper);

        addNewCard();
    }

    public void addNewCard() {
        ContentValues params = new ContentValues();

        Requests.request(this, "getGroceries", params, response -> {
            try {
                //TODO: change categories
                JSONArray categories = new JSONArray(response);
                courseRV = findViewById(R.id.idRVCourse);
                cardBuilderArrayList = new ArrayList<>();
                for (int i = 0; i < categories.length(); ++i) {
                    JSONObject object = categories.getJSONObject(i);
                    String grc_name = object.getString("grc_name");
                    String grc_image = object.getString("grc_image");
                    cardBuilderArrayList.add(new CardBuilder(grc_name, grc_image));
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LinearCardListMaker cardListMaker = new LinearCardListMaker(HomeShopper.this, cardBuilderArrayList);
                        courseRV.setLayoutManager(new GridLayoutManager(HomeShopper.this, 2));
                        courseRV.setAdapter(cardListMaker);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

}