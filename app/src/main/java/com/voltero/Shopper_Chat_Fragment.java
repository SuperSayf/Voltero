package com.voltero;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shopper_Chat_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shopper_Chat_Fragment extends Fragment {

    private Shopper_Chat_Fragment.MessageAdapter adapter;
    private Handler mHandler = new Handler();
    public static String session_email = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shopper_Chat_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shopper_Chat_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shopper_Chat_Fragment newInstance(String param1, String param2) {
        Shopper_Chat_Fragment fragment = new Shopper_Chat_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopper__chat_, container, false);

        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        startRepeating();

        getSessionEmail();

        ListView messageList = view.findViewById(R.id.messageList);
        final EditText messageBox = view.findViewById(R.id.messageBox);
        AppCompatImageView send = view.findViewById(R.id.send);


        adapter = new Shopper_Chat_Fragment.MessageAdapter();
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

        return view;
    }

    public void getSessionEmail() {
        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);
        params.put("user_type", MainActivity.user_type);

        Requests.request(getActivity(), "getSessionWith", params, response -> {
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
                        if (isAdded()) {
                            requireActivity().runOnUiThread(new Runnable() {
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
                        }
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
        public View getView(int i, View viewM, ViewGroup viewGroup) {

            if (viewM == null)
                viewM = getLayoutInflater().inflate(R.layout.message_list_item, viewGroup, false);

            TextView sentMessage = viewM.findViewById(R.id.sentMessage);
            TextView receivedMessage = viewM.findViewById(R.id.receivedMessage);
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

            return viewM;
        }

        void addItem(JSONObject item) {
            messagesList.add(item);
            notifyDataSetChanged();
        }
    }
}