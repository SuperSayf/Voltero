package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_DetailedOrder_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_DetailedOrder_Fragment extends Fragment {

    private RecyclerView courseRV;

    JSONArray orderItems;
    public Button closeSession;

    public String user_fname;
    public String user_lname;
    public String user_address;
    public String user_cell;
    public String user_image;

    DetailedOrderListMaker linearCartListMaker;
    private ArrayList<CartItemCard> cardBuilderArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Volunteer_DetailedOrder_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Volunteer_DetailedOrder_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Volunteer_DetailedOrder_Fragment newInstance(String param1, String param2) {
        Volunteer_DetailedOrder_Fragment fragment = new Volunteer_DetailedOrder_Fragment();
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
        View view;
        cardBuilderArrayList = new ArrayList<>();

        // Inflate the layout for this fragment
        if (HomeVolunteer.session_email.equals("")){
            view = inflater.inflate(R.layout.fragment_detailed_order, container, false);
            courseRV = view.findViewById(R.id.idRVCourse);
            cardBuilderArrayList.add(new CartItemCard("Number active sessions", "https://bit.ly/3MHPwS8", "0"));

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    courseRV.setHasFixedSize(true);
                    linearCartListMaker = new DetailedOrderListMaker(getActivity(), cardBuilderArrayList);
                    courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    courseRV.setAdapter(linearCartListMaker);
                    // Stop the progressBar
                    view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
            });

        } else {
            view = inflater.inflate(R.layout.fragment_detailed_order_active_, container, false);

            TextView shopper_name = (TextView)view.findViewById(R.id.shopperName);
            TextView shopper_address = (TextView)view.findViewById(R.id.shopperAddress);
            TextView shopper_cell = (TextView)view.findViewById(R.id.shopperCell);

            ContentValues params3 = new ContentValues();
            params3.put("user_email", HomeVolunteer.session_email);

            Requests.request(getActivity(), "getDetails", params3, response -> {
                try {
                    JSONArray user_details = new JSONArray(response);
                    for (int i = 0; i < user_details.length(); ++i) {
                        JSONObject object = user_details.getJSONObject(i);
                        user_fname = object.getString("user_firstname");
                        user_lname = object.getString("user_surname");
                        user_address = object.getString("user_address");
                        user_cell = object.getString("user_cell");
                        user_image = object.getString("user_image");
                    }
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String name = user_fname + ' ' + user_lname;
                                shopper_name.setText("Name: " + name);
                                shopper_address.setText("Address: " + user_address);
                                shopper_cell.setText("Cell: " + user_cell);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });


            ContentValues params = new ContentValues();
            params.put("user_email", HomeVolunteer.session_email);

            Requests.request(getActivity(), "getCartItems", params, response -> {
                try {
                    //TODO: change categories
                    orderItems = new JSONArray(response);
                    courseRV = view.findViewById(R.id.idRVCourse);
                    for (int i = 0; i < orderItems.length(); ++i) {
                        JSONObject object = orderItems.getJSONObject(i);
                        String grc_name = object.getString("grc_name");
                        String grc_image = object.getString("grc_image");
                        String grc_quantity = object.getString("grc_quantity");
                        cardBuilderArrayList.add(new CartItemCard(grc_name, grc_image, grc_quantity));
                    }
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                courseRV.setHasFixedSize(true);
                                linearCartListMaker = new DetailedOrderListMaker(getActivity(), cardBuilderArrayList);
                                courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                                courseRV.setAdapter(linearCartListMaker);
                                // Stop the progressBar
                                view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            ContentValues params2 = new ContentValues();
            params2.put("user_email", HomeVolunteer.session_email);
            params2.put("session_with", MainActivity.user_email);

            closeSession = (Button) view.findViewById(R.id.closeOrder);

            closeSession.setOnClickListener(v -> {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/sendMessage.php")).newBuilder();
                            urlBuilder.addQueryParameter("session_id", HomeVolunteer.session_ID);
                            urlBuilder.addQueryParameter("user_email", HomeVolunteer.session_email);
                            urlBuilder.addQueryParameter("msg_content", "SessionTerminated23892839283928938293891231361731563516351351625313131453143652");
                            urlBuilder.addQueryParameter("msg_seen", "false");

                            String url = urlBuilder.build().toString();

                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            Response response = client.newCall(request).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Requests.request(activity, "closeSession", params2, response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message.equals("success")) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    HomeVolunteer.session_email = "";
                                    HomeVolunteer.session_started = false;
                                    HomeVolunteer.session_ID = "";
                                    HomeVolunteer.isInSession = "false";
                                    HomeVolunteer.session_initialized = true;
                                    Toast.makeText(activity, "Order completed!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Requests.showMessage(activity, "Error with request");
                    }
                });
            });
        }

        return view;
    }
}