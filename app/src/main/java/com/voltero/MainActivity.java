package com.voltero;

import android.app.Activity;
import android.content.ContentValues;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.royrodriguez.transitionbutton.TransitionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import render.animations.Attention;
import render.animations.Render;

public class MainActivity extends AppCompatActivity {

    // Global variables
    public static String user_email = "";
    public static String user_type = "";

    private TransitionButton transitionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout layout = findViewById(R.id.MainAct);
        AnimationDrawable animation = (AnimationDrawable) layout.getBackground();
        animation.setEnterFadeDuration(1000);
        animation.setExitFadeDuration(2000);
        animation.start();
    }

    public void goToRegister(View v) {
        // Intent to the RegisterActivity
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }



    public void doLogin(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        transitionButton = findViewById(R.id.btnLogin);
        transitionButton.startAnimation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                user_email = ((TextView) findViewById(R.id.txtLoginEmail)).getText().toString();
                String password = ((TextView) findViewById(R.id.txtLoginPassword)).getText().toString();

                if (user_email.equals("")) {
                    Render render = new Render(MainActivity.this);
                    render.setAnimation(Attention.Shake((TextView) findViewById(R.id.txtLoginEmail)));
                    render.start();
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Requests.showMessage(MainActivity.this, "Email is required");
                    return;
                } else if (password.equals("")) {
                    Render render = new Render(MainActivity.this);
                    render.setAnimation(Attention.Shake((TextView) findViewById(R.id.txtLoginPassword)));
                    render.start();
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Requests.showMessage(MainActivity.this, "Password is required");
                    return;
                }

                ContentValues params = new ContentValues();
                params.put("user_email", user_email);
                params.put("user_password", password);

                ContentValues sessionParams = new ContentValues();
                sessionParams.put("user_email", user_email);

                Requests.request(MainActivity.this, "userLogin", params, response -> {
                    try {
                        // Get the response
                        JSONObject jsonObject = new JSONObject(response);
                        user_type = jsonObject.getString("user_type");
                        if (user_type.equals("1")) {
                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                @Override
                                public void onAnimationStopEnd() {
                                    Intent intent = new Intent(getBaseContext(), HomeShopper.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }
                            });
                        } else if (user_type.equals("0")) {
                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                @Override
                                public void onAnimationStopEnd() {
                                    Intent intent = new Intent(getBaseContext(), HomeVolunteer.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                            }
                        });
                        Requests.showMessage(MainActivity.this, "Email or password is incorrect");
                    }
                });

                if(user_type.equals("1"))
                    Requests.request(MainActivity.this, "openSession", sessionParams, response2 -> {    });
            }
        }, 2000);
    }



}