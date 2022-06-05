package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_Home_Fragment extends Fragment {

    public static String session_id;
    public static String shopper_email;
    public static String shopper_fname;
    public static String shopper_lname;
    public static String shopper_address;
    public static String shopper_image;

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<OrderCard> orderArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Volunteer_Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Volunteer_Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Volunteer_Home_Fragment newInstance(String param1, String param2) {
        Volunteer_Home_Fragment fragment = new Volunteer_Home_Fragment();
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

        View view = inflater.inflate(R.layout.fragment_volunteer__home_, container, false);

        ContentValues params = new ContentValues();

        Requests.request(getActivity(), "findSessions", params, response -> {
            try {
                JSONArray sessions = new JSONArray(response);
                courseRV = view.findViewById(R.id.idRVCourse);
                orderArrayList = new ArrayList<>();

                if (sessions.getJSONObject(0).getString("message").equals("Found")) {
                    for (int i = 0; i < sessions.length(); ++i) {
                        JSONObject session = sessions.getJSONObject(i);
                        shopper_email = session.getString("user_email");
                        shopper_fname = session.getString("user_firstname");
                        shopper_lname = session.getString("user_surname");
                        shopper_address = session.getString("user_address");
                        shopper_image = session.getString("user_image");
                        String name = shopper_fname + ' ' + shopper_lname;
                        orderArrayList.add(new OrderCard(shopper_email, name, shopper_address, shopper_image));
                    }
                } else {
                    orderArrayList.add(new OrderCard("","No available sessions","", "https://bit.ly/3MHPwS8"));
                }
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseRV.setHasFixedSize(true);
                            OrderListMaker orderListMaker = new OrderListMaker(getActivity(), orderArrayList);
                            courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            courseRV.setAdapter(orderListMaker);
                            // Stop the progressBar
                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        Button refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues params = new ContentValues();

                Requests.request(getActivity(), "findSessions", params, response -> {
                    try {
                        JSONArray sessions = new JSONArray(response);
                        courseRV = view.findViewById(R.id.idRVCourse);
                        orderArrayList = new ArrayList<>();

                        if (sessions.getJSONObject(0).getString("message").equals("Found")) {
                            for (int i = 0; i < sessions.length(); ++i) {
                                JSONObject session = sessions.getJSONObject(i);
                                shopper_email = session.getString("user_email");
                                shopper_fname = session.getString("user_firstname");
                                shopper_lname = session.getString("user_surname");
                                shopper_address = session.getString("user_address");
                                shopper_image = session.getString("user_image");
                                String name = shopper_fname + ' ' + shopper_lname;
                                orderArrayList.add(new OrderCard(shopper_email, name, shopper_address, shopper_image));
                            }
                        } else {
                            orderArrayList.add(new OrderCard("","No available sessions","", "https://bit.ly/3MHPwS8"));
                        }
                        if (isAdded()) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    courseRV.setHasFixedSize(true);
                                    OrderListMaker orderListMaker = new OrderListMaker(getActivity(), orderArrayList);
                                    courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    courseRV.setAdapter(orderListMaker);
                                    // Stop the progressBar
                                    view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
            }
        });

        return view;
    }

}