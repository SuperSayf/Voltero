package com.voltero;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Shopper_Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Shopper_Profile_Fragment extends Fragment {

    public static CircleImageView pfp;

    public Button btnUpdate;

    public String txt_address;
    public String txt_cell;
    public String txt_fname;
    public String txt_lname;
    public String txt_type;
    public String txt_image;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Shopper_Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Shopper_Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Shopper_Profile_Fragment newInstance(String param1, String param2) {
        Shopper_Profile_Fragment fragment = new Shopper_Profile_Fragment();
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopper__profile_, container, false);

        EditText user_email = (EditText)view.findViewById(R.id.txtEmail);
        EditText user_address = (EditText)view.findViewById(R.id.txtAddress);
        EditText user_cell = (EditText)view.findViewById(R.id.txtCell);
        TextView user_name = (TextView)view.findViewById(R.id.txtSurname);
        TextView user_type = (TextView)view.findViewById(R.id.txtAccountType);


        ContentValues params = new ContentValues();
        params.put("user_email", MainActivity.user_email);

        Requests.request(requireActivity(), "getDetails", params, response -> {
            try {
                JSONArray user_details = new JSONArray(response);
                for (int i = 0; i < user_details.length(); ++i) {
                    JSONObject object = user_details.getJSONObject(i);
                    txt_fname = object.getString("user_firstname");
                    txt_lname = object.getString("user_surname");
                    txt_address = object.getString("user_address");
                    txt_type = object.getString("user_type");
                    txt_cell = object.getString("user_cell");
                    txt_image = object.getString("user_image");
                }
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String name =txt_fname + ' ' + txt_lname;
                            user_name.setText(name);
                            user_email.setText("Email: " + MainActivity.user_email);
                            user_address.setText("Address: " + txt_address);
                            user_cell.setText("Cell: " + txt_cell);
                            if (txt_type.equals("1")) {
                                user_type.setText("User Type: Shopper");
                            } else {
                                user_type.setText("User Type: Volunteer");
                            }


                        }
                    });
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

        btnUpdate = (Button)view.findViewById(R.id.btnUpdate);

        user_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnUpdate.setVisibility(View.VISIBLE);
                // Clear the text field
                user_email.setText("");
                return false;
            }
        });
        user_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnUpdate.setVisibility(View.VISIBLE);
                // Clear the text field
                user_address.setText("");
                return false;
            }
        });
        user_cell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnUpdate.setVisibility(View.VISIBLE);
                // Clear the text field
                user_cell.setText("");
                return false;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ContentValues params2 = new ContentValues();
                params2.put("user_email", MainActivity.user_email);
                params2.put("user_address", user_address.getText().toString());
                Requests.request(requireActivity(), "changeAddress", params2, response -> {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Details updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                ContentValues params3 = new ContentValues();
                params3.put("user_email", MainActivity.user_email);
                params3.put("user_cell", user_cell.getText().toString());
                Requests.request(requireActivity(), "changeCell", params3, response -> {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Details updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                ContentValues params4 = new ContentValues();
                params4.put("user_email", MainActivity.user_email);
                params4.put("user_email", user_email.getText().toString());
                Requests.request(requireActivity(), "changeEmail", params4, response -> {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Details updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });


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

        Button btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.user_email = "";
                MainActivity.user_type = "";
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        pfp = view.findViewById(R.id.profile_image);
        FloatingActionButton changeImage = view.findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener()
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