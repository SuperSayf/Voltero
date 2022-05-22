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

public class GroupedGroceries extends AppCompatActivity {

    public static String category_name;

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouped_groceries);

        addNewCard();
    }

    public void addNewCard() {
        ContentValues params = new ContentValues();
        params.put("cat_name", category_name);
        Log.e("test", category_name);

        Requests.request(this, "categoryToGroceries", params, response -> {
            try {
                //TODO: change categories
                JSONArray groceries = new JSONArray(response);
                Log.e("list", String.valueOf(groceries));
                courseRV = findViewById(R.id.idRVCourse);
                cardBuilderArrayList = new ArrayList<>();
                for (int i = 0; i < groceries.length(); ++i) {
                    JSONObject object = groceries.getJSONObject(i);
                    String grc_name = object.getString("grc_name");
                    String grc_image = object.getString("grc_image");
                    cardBuilderArrayList.add(new CardBuilder(grc_name, grc_image));
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LinearCardListMaker cardListMaker = new LinearCardListMaker(GroupedGroceries.this, cardBuilderArrayList);
                        courseRV.setLayoutManager(new GridLayoutManager(GroupedGroceries.this, 2));
                        courseRV.setAdapter(cardListMaker);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }
}