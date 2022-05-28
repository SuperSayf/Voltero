package com.voltero;

import android.content.ContentValues;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import render.animations.Attention;
import render.animations.Bounce;
import render.animations.Render;

public class MainActivity extends AppCompatActivity {

    // Global variables
    public static String user_email = "";
    public static String user_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = findViewById(R.id.MainAct);
        AnimationDrawable animation = (AnimationDrawable) layout.getBackground();
        animation.setEnterFadeDuration(2500);
        animation.setExitFadeDuration(5000);
        animation.start();
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

    public void doLogin(View v) {
        user_email = ((TextView) findViewById(R.id.txtLoginEmail)).getText().toString();
        String password = ((TextView) findViewById(R.id.txtLoginPassword)).getText().toString();

        if (user_email.equals("")) {
            Render render = new Render(MainActivity.this);
            render.setAnimation(Attention.Shake((TextView) findViewById(R.id.txtLoginEmail)));
            render.start();
            Requests.showMessage(this, "Please enter your email");
            return;
        } else if (password.equals("")) {
            Render render = new Render(MainActivity.this);
            render.setAnimation(Attention.Shake((TextView) findViewById(R.id.txtLoginPassword)));
            render.start();
            Requests.showMessage(this, "Please enter your password");
            return;
        }

        ContentValues params = new ContentValues();
        params.put("user_email", user_email);
        params.put("user_password", password);

        Requests.request(this, "userLogin", params, response -> {
            try {
                // Get the response
                JSONObject jsonObject = new JSONObject(response);
                user_type = jsonObject.getString("user_type");
                if (user_type.equals("1")) {
                    // Open the shopper activity
                    Intent intent = new Intent(this, HomeShopper.class);
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

        ContentValues sessionParams = new ContentValues();
        sessionParams.put("user_email", user_email);

        Requests.request(this, "openSession", sessionParams, response -> {    });

    }

}