package com.voltero;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Global variables
    public static String user_email = "";
    public static String user_type = "";

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
        user_email = ((TextView) findViewById(R.id.txtLoginEmail)).getText().toString();

        ContentValues params = new ContentValues();
        params.put("user_email", user_email);
        params.put("user_password", ((TextView) findViewById(R.id.txtLoginPassword)).getText().toString());

        Requests.request(this, "userLogin", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                user_type = jsonObject.getString("user_type");
                if (user_type.equals("1")) {
                    // TODO: change back to ShopperHome
                    // Open the shopper activity
                    Intent intent = new Intent(this, Categories.class);
                    startActivity(intent);
                } else if (user_type.equals("0")) {
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