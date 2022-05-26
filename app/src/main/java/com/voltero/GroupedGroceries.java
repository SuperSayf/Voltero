//package com.voltero;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
//
//import java.util.ArrayList;
//
//public class GroupedGroceries extends AppCompatActivity {
//
//    public static String category_name;
//
//    private RecyclerView courseRV;
//
//    // Arraylist for storing data
//    private ArrayList<CardBuilder> cardBuilderArrayList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_grouped_groceries);
//
//        //addNewCard();
//    }
//
//    public void addNewCard() {
//        ContentValues params = new ContentValues();
//        params.put("cat_name", category_name);
//        Log.e("test", category_name);
//
//        Requests.request(this, "categoryToGroceries", params, response -> {
//            try {
//                //TODO: change categories
//                JSONArray groceries = new JSONArray(response);
//                Log.e("list", String.valueOf(groceries));
//                courseRV = findViewById(R.id.idRVCourse);
//                cardBuilderArrayList = new ArrayList<>();
//                for (int i = 0; i < groceries.length(); ++i) {
//                    JSONObject object = groceries.getJSONObject(i);
//                    String grc_name = object.getString("grc_name");
//                    String grc_image = object.getString("grc_image");
//                    cardBuilderArrayList.add(new CardBuilder(grc_name, grc_image));
//                }
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        LinearCardListMaker cardListMaker = new LinearCardListMaker(GroupedGroceries.this, cardBuilderArrayList);
//                        courseRV.setLayoutManager(new GridLayoutManager(GroupedGroceries.this, 2));
//                        courseRV.setAdapter(cardListMaker);
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        });
//    }
//
//}

package com.voltero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class GroupedGroceries extends AppCompatActivity {

    MeowBottomNavigation bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouped_groceries);


        bottomNav = findViewById(R.id.bottomNav);

        //add menu items to bottom nav
        bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_category));
        bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_search));
        bottomNav.add(new MeowBottomNavigation.Model(4, R.drawable.ic_cart));
        bottomNav.add(new MeowBottomNavigation.Model(5, R.drawable.ic_map));
        bottomNav.add(new MeowBottomNavigation.Model(6, R.drawable.ic_chat));
        bottomNav.add(new MeowBottomNavigation.Model(7, R.drawable.ic_profile));


        //set bottom nav on show listener
        bottomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //define a fragment
                Fragment fragment;

                //initialize fragment according to its id
                switch (item.getId()) {
                    case 1:
                        fragment = new Shopper_Home_Fragment();
                        break;
                    case 2:
                        fragment = new Shopper_Categories_Fragment();
                        break;
                    case 3:
                        fragment = new Shopper_Search_Fragment();
                        break;
                    case 4:
                        fragment = new Shopper_Cart_Fragment();
                        break;
                    case 5:
                        fragment = new Shopper_Map_Fragment();
                        break;
                    case 6:
                        fragment = new Shopper_Chat_Fragment();
                        break;
                    case 7:
                        fragment = new Shopper_Profile_Fragment();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getId());
                }

                //use load fragment method to show the current fragment
                loadFragment(fragment);
            }
        });

        //set the initial fragment to show
        bottomNav.show(1, true);

        //set menu item on click listener
        bottomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //display a toast
                //Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //set on reselect listener
        bottomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //display a toast
                //Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        //set count to dashboard item
        bottomNav.setCount(6, "3");

    }


    //define a load method to feed the screen
    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container,fragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void goToChat(View v) {
        // Intent to the ChatActivity
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
    }

}