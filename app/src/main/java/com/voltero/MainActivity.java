package com.voltero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

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

                    // todo: Hash the password

                    // Add the values to the url
                    urlBuilder.addQueryParameter("user_firstname", txtFirstName.getText().toString());
                    urlBuilder.addQueryParameter("user_surname", txtSurname.getText().toString());
                    urlBuilder.addQueryParameter("user_email", txtEmail.getText().toString());
                    urlBuilder.addQueryParameter("user_password", txtPassword.getText().toString());
                    urlBuilder.addQueryParameter("user_address", txtAddress.getText().toString());
                    urlBuilder.addQueryParameter("user_type", "1");

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
}