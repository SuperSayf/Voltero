package com.voltero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Chat extends AppCompatActivity {

    private MessageAdapter adapter;
    private Handler mHandler = new Handler();
    public static String session_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        startRepeating();

        getSessionEmail();

        ListView messageList = findViewById(R.id.messageList);
        final EditText messageBox = findViewById(R.id.messageBox);
        TextView send = findViewById(R.id.send);


        adapter = new MessageAdapter();
        messageList.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageBox.getText().toString();
                if (!message.isEmpty()) {
                    messageBox.setText("");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("message", message);
                        json.put("byServer", "false");

                        adapter.addItem(json);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    OkHttpClient client = new OkHttpClient();
                                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/sendMessage.php")).newBuilder();
                                    urlBuilder.addQueryParameter("session_id", "33");
                                    urlBuilder.addQueryParameter("user_email", session_email);
                                    urlBuilder.addQueryParameter("msg_content", message);
                                    urlBuilder.addQueryParameter("msg_seen", "false");

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
        List<JSONObject> messagesList = new ArrayList<>();

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
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    receivedMessage.setVisibility(View.INVISIBLE);
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