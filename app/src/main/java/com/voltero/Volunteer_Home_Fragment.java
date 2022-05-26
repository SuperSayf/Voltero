package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_Home_Fragment extends Fragment {

    public static String session_id;
    public static String shopper_email;

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<CardBuilder> cardBuilderArrayList;

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
                //TODO: change categories
                JSONArray sessions = new JSONArray(response);
                courseRV = view.findViewById(R.id.idRVCourse);
                cardBuilderArrayList = new ArrayList<>();

                if (sessions.getJSONObject(0).getString("message").equals("Found")) {
                    for (int i = 0; i < sessions.length(); ++i) {
                        JSONObject session = sessions.getJSONObject(i);
                        session_id = session.getString("session_id");
                        shopper_email = session.getString("user_email");
                        // TODO: Update the session_with to the volunteer email
                        cardBuilderArrayList.add(new CardBuilder(shopper_email, "https://bit.ly/3wGTdRm"));
                    }
                } else {
                    cardBuilderArrayList.add(new CardBuilder("No sessions available", "https://bit.ly/3MHPwS8"));
                }
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseRV.setHasFixedSize(true);
                            LinearCardListMaker linearCardListMaker = new LinearCardListMaker(getActivity(), cardBuilderArrayList);
                            courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            courseRV.setAdapter(linearCardListMaker);
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