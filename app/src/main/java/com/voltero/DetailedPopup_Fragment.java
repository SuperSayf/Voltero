package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
 * Use the {@link DetailedPopup_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPopup_Fragment extends Fragment {

    private RecyclerView courseRV;

    public Button button;

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

        return view;
    }
}