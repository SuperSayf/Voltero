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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_DetailedOrder_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_DetailedOrder_Fragment extends Fragment {

    private RecyclerView courseRV;

    JSONArray orderItems;
    public Button closeSession;

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

    public void getAddress() {
        ContentValues params = new ContentValues();
        params.put("user_email", HomeVolunteer.session_email);

        Requests.request(getActivity(), "getAddress", params, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                HomeVolunteer.shopper_address = jsonObject.getString("user_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
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



            TextView shopper_email = (TextView)view.findViewById(R.id.shopperName);
            TextView shopper_address = (TextView)view.findViewById(R.id.shopperAddress);

            ContentValues params3 = new ContentValues();
            params3.put("user_email", HomeVolunteer.session_email);

            Requests.request(getActivity(), "getAddress", params3, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    HomeVolunteer.shopper_address = jsonObject.getString("user_address");
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shopper_address.setText(HomeVolunteer.shopper_address);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            shopper_email.setText(HomeVolunteer.session_email);


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
                Requests.request(activity, "closeSession", params2, response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message.equals("success")) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    HomeVolunteer.session_email = "";
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