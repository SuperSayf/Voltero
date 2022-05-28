package com.voltero;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.geojson.Point;
import com.mapbox.navigation.dropin.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_Map_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_Map_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Volunteer_Map_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Volunteer_Map_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Volunteer_Map_Fragment newInstance(String param1, String param2) {
        Volunteer_Map_Fragment fragment = new Volunteer_Map_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_volunteer__map_, container, false);

        // Points
        Point origin = Point.fromLngLat(28.075418, -26.1640314);
        Point destination = Point.fromLngLat(28.02479119803452, -26.1888766);

        NavigationView navigationView = view.findViewById(R.id.navigationView);

        // Now set the origin and destination and request a route

        return view;
    }

}