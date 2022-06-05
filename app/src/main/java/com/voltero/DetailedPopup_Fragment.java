package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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
 * Use the {@link DetailedPopup_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPopup_Fragment extends Fragment {

    private RecyclerView courseRV;

    public Button acceptSession;
    public String user_fname;
    public String user_lname;
    public String user_address;
    public String user_cell;
    public String user_image;

    JSONArray orderItems;
    DetailedOrderListMaker linearCartListMaker;

    // Arraylist for storing data
    private ArrayList<CartItemCard> cardBuilderArrayList;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedPopup_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedPopup_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedPopup_Fragment newInstance(String param1, String param2) {
        DetailedPopup_Fragment fragment = new DetailedPopup_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed_popup, container, false);


        TextView shopper_name = (TextView)view.findViewById(R.id.volName);
        TextView shopper_address = (TextView)view.findViewById(R.id.volEmail);
        TextView shopper_cell = (TextView)view.findViewById(R.id.shopperCell);

        ContentValues params3 = new ContentValues();
        params3.put("user_email", HomeVolunteer.shopper_email);

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
        params.put("user_email", HomeVolunteer.shopper_email);

        Requests.request(getActivity(), "getCartItems", params, response -> {
            try {
                //TODO: change categories
                orderItems = new JSONArray(response);
                courseRV = view.findViewById(R.id.idRVCourse);
                cardBuilderArrayList = new ArrayList<>();
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
        params2.put("user_email", HomeVolunteer.shopper_email);
        params2.put("session_with", MainActivity.user_email);

        acceptSession = (Button)view.findViewById(R.id.accept);

        acceptSession.setOnClickListener(v -> {
            Requests.request(activity, "acceptSession", params2, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, HomeVolunteer.shopper_email + "'s order accepted", Toast.LENGTH_LONG).show();
                                HomeVolunteer.session_started = true;
                                HomeVolunteer.session_initialized = false;
                                HomeVolunteer.isInSession = "true";
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
                } catch (JSONException e){
                    Requests.showMessage(activity, "Error with request");
                }
            });
        });


        return view;
    }



}