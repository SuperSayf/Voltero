package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        // Intent to the LoginActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToRegister(View v) {
        // Intent to the RegisterActivity
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void goToChat(View v) {
        // Intent to the ChatActivity
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
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