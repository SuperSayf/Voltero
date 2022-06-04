package com.voltero;

import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteer_Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteer_Profile_Fragment extends Fragment {

    public static CircleImageView pfp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Volunteer_Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Volunteer_Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Volunteer_Profile_Fragment newInstance(String param1, String param2) {
        Volunteer_Profile_Fragment fragment = new Volunteer_Profile_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_volunteer__profile_, container, false);

        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);

        Requests.request(requireActivity(), "getImage", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("success").equals("true")) {
                    String encodedImage = jsonObject.getString("user_image");
                    // Convert encoded image to bitmap
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    // convert byte array back to original image
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pfp.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        pfp = view.findViewById(R.id.profile_image);
        pfp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Do stuff
                ImagePicker.with(requireActivity())
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        return view;
    }


}