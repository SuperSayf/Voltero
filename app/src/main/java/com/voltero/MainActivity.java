package com.voltero;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/addUser.php")).newBuilder();

                    // Get TextView txtFirstName, txtSurname, txtEmail, txtPassword and txtAddress from the layout
                    TextView txtFirstName = findViewById(R.id.txtFirstName);
                    TextView txtSurname = findViewById(R.id.txtSurname);
                    TextView txtEmail = findViewById(R.id.txtEmail);
                    TextView txtPassword = findViewById(R.id.txtPassword);
                    TextView txtAddress = findViewById(R.id.txtAddress);
                    TextView txtCell = findViewById(R.id.txtCell);

                    // Hash the password
                    String password = txtPassword.getText().toString();
                    String bcryptedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

                    // Add the values to the url
                    urlBuilder.addQueryParameter("user_firstname", txtFirstName.getText().toString());
                    urlBuilder.addQueryParameter("user_surname", txtSurname.getText().toString());
                    urlBuilder.addQueryParameter("user_email", txtEmail.getText().toString());
                    urlBuilder.addQueryParameter("user_password", bcryptedPassword);
                    urlBuilder.addQueryParameter("user_address", txtAddress.getText().toString());
                    urlBuilder.addQueryParameter("user_type", "1");
                    urlBuilder.addQueryParameter("user_cell", txtCell.getText().toString());

                    // Build the url
                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    final String result = Objects.requireNonNull(response.body()).string();

                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          TextView textView = findViewById(R.id.txtOutput);
                                          textView.setText(result);
                                      }
                                  }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    String FormChoice = "";

    public void doLogin(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2430888/userLogin.php")).newBuilder();

                    // Get TextView txtEmail and txtPassword from the layout
                    TextView txtEmail = findViewById(R.id.txtLoginEmail);
                    TextView txtPassword = findViewById(R.id.txtLoginPassword);

                    // Add the values to the url
                    urlBuilder.addQueryParameter("user_email", txtEmail.getText().toString());
                    urlBuilder.addQueryParameter("user_password", txtPassword.getText().toString());

                    // Build the url
                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    final String result = Objects.requireNonNull(response.body()).string();

                    FormChoice = result;

                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          TextView textView = findViewById(R.id.txtLoginOutput);
                                          textView.setText(result);

                                          if (FormChoice.equals("1")) {
                                              setContentView(R.layout.activity_home_shopper);

                                          } else if (FormChoice.equals("0")) {
                                              setContentView(R.layout.activity_home_volunteer);
                                          }
                                      }
                                  }
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}