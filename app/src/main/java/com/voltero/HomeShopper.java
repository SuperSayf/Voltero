package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.hosseiniseyro.apprating.AppRatingDialog;
import com.hosseiniseyro.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import render.animations.Bounce;
import render.animations.Render;
import timber.log.Timber;

public class HomeShopper extends AppCompatActivity implements RatingDialogListener {

    @Override
    public void onPositiveButtonClickedWithComment(int rate, @NonNull String comment) {
        ContentValues params = new ContentValues();
        params.put("user_email", HomeShopper.session_email);
        params.put("rating_from", MainActivity.user_email);
        params.put("rating_stars", String.valueOf(rate));
        params.put("rating_comment", comment);

        Requests.request(this, "postFeedback", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("message").equals("Rating added successfully")) {
                    Requests.showMessage(this, "Rating added successfully");
                } else {
                    Requests.showMessage(this, "Error adding rating");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPositiveButtonClickedWithoutComment(int rate) {
        // interpret results, send it to analytics etc...
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    // Global variables
    public static List<JSONObject> messagesList = new ArrayList<>();
    public static String session_email = "";
    public static String session_ID = "";
    public static HomeShopper.MessageAdapter adapter;
    public static Handler mHandler = new Handler();
    public static String grocery_name = "";
    public static String grocery_image = "";

    public static boolean session_started = false;
    public static boolean cart_complete = false;
    public static String isInSession = "false";

    private int messageCount = 1;

    MeowBottomNavigation bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_shopper);

        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);

        Requests.request(this, "isInSessionShopper", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                isInSession = jsonObject.getString("inSession");

                if (isInSession.equals("true")) {
                    getSessionEmail();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        startRepeating();

        adapter = new HomeShopper.MessageAdapter();

        bottomNav = findViewById(R.id.bottomNav);

        //add menu items to bottom nav
        bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_category));
        bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_search));
        bottomNav.add(new MeowBottomNavigation.Model(4, R.drawable.cartlogo));
        bottomNav.add(new MeowBottomNavigation.Model(5, R.drawable.ic_rating));
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
                        if (isInSession.equals("true")) {
                            fragment = new Shopper_Map_Fragment();
                        } else {
                            fragment = new StartASession();
                        }
                        break;
                    case 6:
                        if (isInSession.equals("true")) {
                            fragment = new Shopper_Chat_Fragment();
                        } else {
                            fragment = new StartASession();
                        }
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
                HomeShopper.session_email = jsonObject.getString("user_email");
                HomeShopper.session_ID = jsonObject.getString("session_ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(0)
                .setThreshold(5)
                .setTitle("Please rate your experience")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("The volunteer was very helpful")
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.contentTextColor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setDialogBackgroundColor(R.color.rateAppDialogBackgroundColor)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(this)
                .show();
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
            if (!session_started && cart_complete) {
                ContentValues params = new ContentValues();
                params.put("user_email", MainActivity.user_email);

                Requests.request(HomeShopper.this, "isInSessionShopper", params, response -> {
                    try {
                        // Get the response
                        JSONObject jsonObject = new JSONObject(response);
                        isInSession = jsonObject.getString("inSession");

                        if (isInSession.equals("true")) {
                            getSessionEmail();
                            isInSession = "true";
                            session_started = true;
                            System.out.println("Found");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(HomeShopper.this, "Session has been accepted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            System.out.println("Finding...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (session_started) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/checkForMessages.php")).newBuilder();
                            urlBuilder.addQueryParameter("session_id", HomeShopper.session_ID);
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

                                                          if (message.equals("SessionTerminated23892839283928938293891231361731563516351351625313131453143652")) {
                                                              // Session has been terminated
                                                              System.out.println("Session has been terminated");
                                                              runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      Toast.makeText(HomeShopper.this, "Session has been terminated", Toast.LENGTH_SHORT).show();
                                                                      showDialog();
                                                                  }
                                                              });
                                                                session_started = false;
                                                                cart_complete = false;
                                                                isInSession = "false";

                                                              ContentValues sessionParams = new ContentValues();
                                                              sessionParams.put("user_email", MainActivity.user_email);
                                                              Requests.request(HomeShopper.this, "openSession", sessionParams, response2 -> {    });
                                                          } else {
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
            }
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

            CircleImageView userImage = view.findViewById(R.id.user);
            CircleImageView serverImage = view.findViewById(R.id.server);

            ContentValues params = new ContentValues();
            params.put("user_email", MainActivity.user_email);

            Requests.request(HomeShopper.this, "getImage", params, response -> {
                try {
                    // Get the response
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        String encodedImage = jsonObject.getString("user_image");
                        // Convert encoded image to bitmap
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        // convert byte array back to original image
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userImage.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            ContentValues params2 = new ContentValues();
            params2.put("user_email", session_email);

            Requests.request(HomeShopper.this, "getImage", params2, response -> {
                try {
                    // Get the response
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        String encodedImage = jsonObject.getString("user_image");
                        // Convert encoded image to bitmap
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        // convert byte array back to original image
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                serverImage.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            TextView sentMessage = view.findViewById(R.id.sentMessage);
            TextView receivedMessage = view.findViewById(R.id.receivedMessage);
            JSONObject item = messagesList.get(i);
            try {
                if (item.getBoolean("byServer")) {
                    receivedMessage.setVisibility(View.VISIBLE);
                    serverImage.setVisibility(View.VISIBLE);
                    receivedMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.INVISIBLE);

                    if (i == messagesList.size()-1) {
                        Render render = new Render(HomeShopper.this);
                        render.setAnimation(Bounce.InLeft(receivedMessage));
                        render.start();

                        Render render2 = new Render(HomeShopper.this);
                        render2.setAnimation(Bounce.InRight(serverImage));
                        render2.start();
                    }
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    receivedMessage.setVisibility(View.INVISIBLE);
                    serverImage.setVisibility(View.INVISIBLE);

                    if (i == messagesList.size()-1) {
                        Render render = new Render(HomeShopper.this);
                        render.setAnimation(Bounce.InRight(sentMessage));
                        render.start();

                        Render render2 = new Render(HomeShopper.this);
                        render2.setAnimation(Bounce.InRight(userImage));
                        render2.start();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = Objects.requireNonNull(data).getData();
        Shopper_Profile_Fragment.pfp.setImageURI(uri);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            // Convert Bitmap image into byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteImage = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteImage, Base64.DEFAULT);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            okhttp3.RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("user_email", MainActivity.user_email)
                    .addFormDataPart("user_image", encodedImage)
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://lamp.ms.wits.ac.za/~s2430888/uploadImage.php")
                    .method("POST", body)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Timber.tag("ERROR").d(e.getMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomeShopper.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}