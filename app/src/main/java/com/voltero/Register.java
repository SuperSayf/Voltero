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

import org.json.JSONException;
import org.json.JSONObject;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Setup spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    String SelectedItem = "";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SelectedItem = parent.getItemAtPosition(position).toString();
        //Toast
        Toast.makeText(parent.getContext(), "Selected: " + SelectedItem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Set the string to false
        SelectedItem = "";
    }

    public void doRegistration(View v) {
        // Get TextView txtFirstName, txtSurname, txtEmail, txtPassword and txtAddress from the layout
        TextView txtFirstName = findViewById(R.id.txtFirstName);
        TextView txtSurname = findViewById(R.id.txtSurname);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPassword = findViewById(R.id.txtPassword);
        TextView txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtCell = findViewById(R.id.txtCell);

        // First do a validation check
        if (txtFirstName.getText().toString().isEmpty() || txtSurname.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty() || txtAddress.getText().toString().isEmpty() || txtCell.getText().toString().isEmpty() || SelectedItem.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the password and confirm password match
        if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash the password
        String password = txtPassword.getText().toString();
        String bPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // If SelectedItem is "Shopper", then make the string "1"
        if (SelectedItem.equals("Shopper")) {
            SelectedItem = "1";
        } else {
            SelectedItem = "0";
        }
        // Create the ContentValues
        ContentValues params = new ContentValues();
        params.put("user_firstname", txtFirstName.getText().toString());
        params.put("user_surname", txtSurname.getText().toString());
        params.put("user_email", txtEmail.getText().toString());
        params.put("user_password", bPassword);
        params.put("user_address", txtAddress.getText().toString());
        params.put("user_type", SelectedItem);
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
}