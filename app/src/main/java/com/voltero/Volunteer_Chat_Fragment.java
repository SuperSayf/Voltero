package com.voltero;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
 * Use the {@link Volunteer_Chat_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_Chat_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Volunteer_Chat_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Volunteer_Chat_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Volunteer_Chat_Fragment newInstance(String param1, String param2) {
        Volunteer_Chat_Fragment fragment = new Volunteer_Chat_Fragment();
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

    // On fragment size change, perform the refresh
    @Override
    public void onResume() {
        super.onResume();
        // Refresh the recycler view
        HomeVolunteer.adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_volunteer__chat_, container, false);

        ListView messageList = view.findViewById(R.id.messageList);
        final EditText messageBox = view.findViewById(R.id.messageBox);
        final AppCompatImageView send = view.findViewById(R.id.send);

        // Open keyboard
        messageBox.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(messageBox, InputMethodManager.SHOW_IMPLICIT);

        messageList.setAdapter(HomeVolunteer.adapter);

        send.setOnClickListener(v -> {
            String message = messageBox.getText().toString();
            if (!message.isEmpty()) {
                messageBox.setText("");
                JSONObject json = new JSONObject();
                try {
                    json.put("message", message);
                    json.put("byServer", "false");

                    HomeVolunteer.adapter.addItem(json);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient client = new OkHttpClient();
                                HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/sendMessage.php")).newBuilder();
                                urlBuilder.addQueryParameter("session_id", HomeVolunteer.session_ID);
                                urlBuilder.addQueryParameter("user_email", HomeVolunteer.session_email);
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
        });

        return view;
    }
}