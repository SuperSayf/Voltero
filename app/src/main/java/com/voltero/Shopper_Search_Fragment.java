package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shopper_Search_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shopper_Search_Fragment extends Fragment {

    public EditText itemName;
    public EditText itemBrand;
    public EditText itemSize;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shopper_Search_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shopper_Search_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shopper_Search_Fragment newInstance(String param1, String param2) {
        Shopper_Search_Fragment fragment = new Shopper_Search_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_shopper__search_, container, false);

        Button addToCart = (Button) view.findViewById(R.id.addButton);
        addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                itemName = (EditText)view.findViewById(R.id.itemName);
                itemBrand = (EditText)view.findViewById(R.id.itemBrand);
                itemSize = (EditText)view.findViewById(R.id.itemSize);

                String name = itemName.getText().toString();
                String brand = itemBrand.getText().toString();
                String size = itemSize.getText().toString();
                String grocName = name + " " + brand + " " + size;

                ContentValues params = new ContentValues();
                params.put("user_email", MainActivity.user_email);
                params.put("grc_name", grocName);
                params.put("grc_image", "https://bit.ly/3GDLiIQ");
                params.put("change_type", "add");

                Requests.request(getActivity(), "cartItems", params, response -> {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), grocName + " added to cart", Toast.LENGTH_LONG).show();
                        }
                    });
                });
            }
        });

        return view;
    }
}