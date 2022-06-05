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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shopper_Map_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shopper_Map_Fragment extends Fragment {

    public static String rator_email;
    public static String rator_fname;
    public static String rator_lname;
    public static String rator_comment;
    public static String rator_rating;
    public static String rator_image;

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<RatingCard> ratingArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shopper_Map_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shopper_Map_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shopper_Map_Fragment newInstance(String param1, String param2) {
        Shopper_Map_Fragment fragment = new Shopper_Map_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_shopper__map_, container, false);

        ContentValues params = new ContentValues();
        params.put("user_email", HomeShopper.session_email);
        Log.e("session email", HomeShopper.session_email);

        Requests.request(getActivity(), "findRatings", params, response -> {
            try {
                JSONArray ratings = new JSONArray(response);
                courseRV = view.findViewById(R.id.idRVCourse);
                ratingArrayList = new ArrayList<>();

                    for (int i = 0; i < ratings.length(); ++i) {
                        JSONObject rating = ratings.getJSONObject(i);
                        rator_email = rating.getString("rating_from");
                        rator_fname = rating.getString("rators_firstname");
                        rator_lname = rating.getString("rators_surname");
                        rator_comment = rating.getString("rating_comment");
                        rator_image = rating.getString("rators_image");
                        rator_rating = rating.getString("rating_stars");
                        String name = rator_fname + ' ' + rator_lname;
                        ratingArrayList.add(new RatingCard(rator_email, rator_rating, name, rator_comment, rator_image));
                    }

                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseRV.setHasFixedSize(true);
                            RatingListMaker ratingListMaker = new RatingListMaker(getActivity(), ratingArrayList);
                            courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            courseRV.setAdapter(ratingListMaker);
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