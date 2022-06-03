package com.voltero;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shopper_Categories_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shopper_Categories_Fragment extends Fragment {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<GroceryCard> groceryCardArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shopper_Categories_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shopper_Categories_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shopper_Categories_Fragment newInstance(String param1, String param2) {
        Shopper_Categories_Fragment fragment = new Shopper_Categories_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_shopper__categories_, container, false);

        ContentValues params = new ContentValues();

        Requests.request(getActivity(), "getCategories", params, response -> {
            try {
                JSONArray categories = new JSONArray(response);
                courseRV = view.findViewById(R.id.idRVCourse);
                groceryCardArrayList = new ArrayList<>();
                for (int i = 0; i < categories.length(); ++i) {
                    JSONObject object = categories.getJSONObject(i);
                    String cat_name = object.getString("cat_name");
                    String cat_image = object.getString("cat_image");
                    groceryCardArrayList.add(new GroceryCard(cat_name, cat_image));
                }
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseRV.setHasFixedSize(true);
                            CategoriesCardListMaker categoriesCardListMaker = new CategoriesCardListMaker(getActivity(), groceryCardArrayList);
                            courseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                            courseRV.setAdapter(categoriesCardListMaker);
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