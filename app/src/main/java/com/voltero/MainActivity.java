package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToLogin(View v) {
        setContentView(R.layout.activity_main);
    }

    public void goToRegister(View v) {
        setContentView(R.layout.activity_register);
    }

    public void doRegistration(View v) {
        // Get TextView txtFirstName, txtSurname, txtEmail, txtPassword and txtAddress from the layout
        TextView txtFirstName = findViewById(R.id.txtFirstName);
        TextView txtSurname = findViewById(R.id.txtSurname);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPassword = findViewById(R.id.txtPassword);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtCell = findViewById(R.id.txtCell);

        // Hash the password
        String password = txtPassword.getText().toString();
        String bPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // Create the ContentValues
        ContentValues params = new ContentValues();
        params.put("user_firstname", txtFirstName.getText().toString());
        params.put("user_surname", txtSurname.getText().toString());
        params.put("user_email", txtEmail.getText().toString());
        params.put("user_password", bPassword);
        params.put("user_address", txtAddress.getText().toString());
        params.put("user_type", "1");
        params.put("user_cell", txtCell.getText().toString());

        // Create the request
        Requests.request(this, "addUser", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getString("success").equals("true")) {
                    // Show the activity_main and close this activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Show the error
                    Requests.showMessage(this, jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void doLogin(View v) {
        ContentValues params = new ContentValues();
        params.put("user_email", ((TextView) findViewById(R.id.txtLoginEmail)).getText().toString());
        params.put("user_password", ((TextView) findViewById(R.id.txtLoginPassword)).getText().toString());

        Requests.request(this, "userLogin", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("user_type").equals("1")) {
                    // Open the shopper activity
                    Intent intent = new Intent(this, HomeShopper.class);
                    startActivity(intent);
                } else if (jsonObject.getString("user_type").equals("0")) {
                    // Open the volunteer activity
                    Intent intent = new Intent(this, HomeVolunteer.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                Requests.showMessage(this, "Error logging in");
            }
        });
    }


}