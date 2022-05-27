package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import render.animations.Attention;
import render.animations.Bounce;
import render.animations.Render;

public class HomeShopper extends AppCompatActivity {

    // Global variables
    public static List<JSONObject> messagesList = new ArrayList<>();
    public static String session_email = "";
    public static HomeShopper.MessageAdapter adapter;
    public static Handler mHandler = new Handler();

    private int messageCount = 1;

    MeowBottomNavigation bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shopper);

        startRepeating();

        getSessionEmail();

        adapter = new HomeShopper.MessageAdapter();

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

    }


    //define a load method to feed the screen
    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container,fragment, null)
                .addToBackStack(null)
                .commit();

        // If the fragment is the Volunteer_Chat_Fragment, then set the message count to 0
        if (fragment instanceof Shopper_Chat_Fragment) {
            // Remove the badge
            bottomNav.setCount(6, "0");
            messageCount = 1;
            bottomNav.clearCount(6);

        }
    }

    public void getSessionEmail() {
        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);
        params.put("user_type", MainActivity.user_type);

        Requests.request(this, "getSessionWith", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                session_email = jsonObject.getString("session_with");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void startRepeating() {
        mHandler.postDelayed(mHandlerTask, 500);
    }

    public void stopRepeating() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    private Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", "run");
                    try {
                        OkHttpClient client = new OkHttpClient();
                        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/checkForMessages.php")).newBuilder();
                        urlBuilder.addQueryParameter("session_id", "33");
                        urlBuilder.addQueryParameter("user_email", MainActivity.user_email);

                        String url = urlBuilder.build().toString();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        Response response = client.newCall(request).execute();
                        final String result = Objects.requireNonNull(response.body()).string();
                        // Create json object from the response
                        JSONObject jsonObject = new JSONObject(result);
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              try {
                                                  if (jsonObject.getString("found").equals("true")) {
                                                      String message = jsonObject.getString("msg_content");
                                                      JSONObject json = new JSONObject();
                                                      try {
                                                          json.put("message", message);
                                                          json.put("byServer", "true");

                                                          // Get the current fragment
                                                          Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
                                                          // Check if the current fragment is the chat fragment
                                                          if (!(currentFragment instanceof Shopper_Chat_Fragment)) {
                                                              bottomNav.setCount(6, String.valueOf(messageCount++));
                                                          }

                                                          adapter.addItem(json);
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                              } catch (JSONException e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      }
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            mHandler.postDelayed(this, 2000);
        }
    };

    public class MessageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messagesList.size();
        }

        @Override
        public Object getItem(int i) {
            return messagesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null)
                view = getLayoutInflater().inflate(R.layout.message_list_item, viewGroup, false);

            TextView sentMessage = view.findViewById(R.id.sentMessage);
            TextView receivedMessage = view.findViewById(R.id.receivedMessage);
            JSONObject item = messagesList.get(i);
            try {
                if (item.getBoolean("byServer")) {
                    receivedMessage.setVisibility(View.VISIBLE);
                    receivedMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);

                    if (i == messagesList.size()-1) {
                        Render render = new Render(HomeShopper.this);
                        render.setAnimation(Bounce.InLeft(receivedMessage));
                        render.start();
                    }
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    receivedMessage.setVisibility(View.INVISIBLE);

                    if (i == messagesList.size()-1) {
                        Render render = new Render(HomeShopper.this);
                        render.setAnimation(Bounce.InRight(sentMessage));
                        render.start();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }

        void addItem(JSONObject item) {
            messagesList.add(item);
            notifyDataSetChanged();
        }
    }

}